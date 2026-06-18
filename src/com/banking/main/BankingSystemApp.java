package com.banking.main;

import com.banking.exception.InsufficientFundsException;
import com.banking.model.BankAccount;
import com.banking.monitor.MonitorThread;
import com.banking.task.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main application class that acts as the driver for the Multithreaded Banking System.
 * It demonstrates Java Multithreading concepts, Thread Life Cycle, Synchronization,
 * Naming, Priorities, Exceptions, and Thread Joins.
 *
 * Requirements demonstrated:
 * 1. Object-Oriented Design (Classes, encapsulation, inheritance, interfaces, runtime polymorphism)
 * 2. Runnable-Based Task Design (DepositTask, WithdrawTask, TransferTask, InterestCalculationTask, StatementGenerationTask)
 * 3. Multiple Threads (Executes 6 threads concurrently)
 * 4. Thread Life Cycle (Captures and prints NEW, RUNNABLE, WAITING, TERMINATED states)
 * 5. Thread Naming (Uses setName() and getName())
 * 6. Thread Priorities (Uses MAX_PRIORITY, NORM_PRIORITY, MIN_PRIORITY)
 * 7. Sleep Method (Thread.sleep() inside tasks to simulate network latency)
 * 8. Join Method (Statement thread joins transaction threads; main thread joins all)
 * 9. Shared Resource (Multiple threads access BankAccount concurrently)
 * 10. Synchronization (Access protected by synchronized methods/blocks)
 * 11. Exception Handling (try-catch, InterruptedException, InsufficientFundsException)
 * 12. Monitoring Thread (MonitorThread polls and displays statuses)
 */
public class BankingSystemApp {

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println("  STARTING OBJECT-ORIENTED MULTITHREADED BANKING SYSTEM FRAMEWORK");
        System.out.println("=========================================================================");

        // -------------------------------------------------------------------------
        // 1. Initialize Shared Resources (BankAccount)
        // -------------------------------------------------------------------------
        BankAccount account1 = new BankAccount("ACC-001", "Alice Smith", 1000.00);
        BankAccount account2 = new BankAccount("ACC-002", "Bob Jones", 500.00);
        List<BankAccount> allAccounts = Arrays.asList(account1, account2);

        System.out.println("\nInitial Account Statuses:");
        System.out.println("  " + account1);
        System.out.println("  " + account2);
        System.out.println("-------------------------------------------------------------------------");

        // -------------------------------------------------------------------------
        // 2. Create Concrete Tasks (Runtime Polymorphism using BankTask interface)
        // -------------------------------------------------------------------------
        // We instantiate 5 distinct transactions + 1 statement generation task
        // We use polymorphism: BankTask variables holding instances of concrete task classes
        BankTask depositTask = new DepositTask("Deposit Alice", account1, 500.00, 400);
        BankTask withdrawTask = new WithdrawTask("Withdraw Alice", account1, 300.00, 600);
        BankTask transferTask = new TransferTask("Transfer Alice to Bob", account1, account2, 400.00, 800);
        BankTask invalidWithdrawTask = new WithdrawTask("Overdraft Alice", account1, 3000.00, 500); // Will trigger custom exception
        BankTask interestTask = new InterestCalculationTask("Interest Calc Alice", account1, 0.02, 1000); // 2% Interest

        // -------------------------------------------------------------------------
        // 3. Create Worker Threads and Assign Names and Priorities
        // -------------------------------------------------------------------------
        Thread t1 = new Thread(depositTask);
        t1.setName("Alice-Deposit-Thread-1");
        t1.setPriority(Thread.MAX_PRIORITY); // Priority 10 - Urgent Deposit

        Thread t2 = new Thread(withdrawTask);
        t2.setName("Alice-Withdraw-Thread-2");
        t2.setPriority(Thread.MAX_PRIORITY); // Priority 10 - Urgent Withdrawal

        Thread t3 = new Thread(transferTask);
        t3.setName("Alice-Transfer-Thread-3");
        t3.setPriority(Thread.NORM_PRIORITY); // Priority 5 - Normal Transfer

        Thread t4 = new Thread(invalidWithdrawTask);
        t4.setName("Alice-Overdraft-Thread-4");
        t4.setPriority(Thread.NORM_PRIORITY); // Priority 5 - Normal Overdraft

        Thread t5 = new Thread(interestTask);
        t5.setName("Alice-Interest-Thread-5");
        t5.setPriority(Thread.MIN_PRIORITY); // Priority 1 - Background Interest Calculation

        // Gather all transaction threads
        List<Thread> transactionThreads = Arrays.asList(t1, t2, t3, t4, t5);

        // Create the Statement Generation Task which depends on transaction threads (uses join())
        StatementGenerationTask statementGenTask = new StatementGenerationTask(
                "Statement Gen Task", allAccounts, transactionThreads, 200);
        Thread t6 = new Thread(statementGenTask);
        t6.setName("Statement-Gen-Thread-6");
        t6.setPriority(Thread.NORM_PRIORITY); // Priority 5 - Normal Statement Generation

        // -------------------------------------------------------------------------
        // 4. Demonstrate Thread States (NEW State)
        // -------------------------------------------------------------------------
        System.out.println("\nDemonstrating Thread State: NEW");
        System.out.printf("  [Main] Thread '%s' state before start(): %s%n", t1.getName(), t1.getState());
        System.out.printf("  [Main] Thread '%s' state before start(): %s%n", t6.getName(), t6.getState());
        System.out.println("-------------------------------------------------------------------------");

        // -------------------------------------------------------------------------
        // 5. Start the Monitoring Thread
        // -------------------------------------------------------------------------
        List<Thread> activeThreads = new ArrayList<>(transactionThreads);
        activeThreads.add(t6);
        
        // Spawn MonitorThread to poll thread states every 300ms
        MonitorThread monitor = new MonitorThread(activeThreads, 300);
        monitor.start();

        // Let the monitor start and print its initial report
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // -------------------------------------------------------------------------
        // 6. Execute Multiple Threads Concurrently (RUNNABLE / TIMED_WAITING)
        // -------------------------------------------------------------------------
        System.out.println("\n[Main] Starting all transaction worker threads concurrently...");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        System.out.println("[Main] Starting statement generation thread (which will join and wait for transactions)...");
        t6.start();

        // -------------------------------------------------------------------------
        // 7. Demonstrate WAITING State and JOIN
        // -------------------------------------------------------------------------
        // We sleep the main thread briefly to allow the tasks to run, and allow
        // the Statement-Gen-Thread-6 to execute join() and enter the WAITING state.
        try {
            // Give workers time to launch and Statement-Gen-Thread-6 to block on join()
            Thread.sleep(500);
            
            System.out.println("\n[Main] Actively checking Statement Thread status while transaction threads are running:");
            System.out.printf("  [Main] Thread '%s' state: %s (Priority: %d)%n", 
                    t6.getName(), t6.getState(), t6.getPriority());
            System.out.println("  (Notice that the statement thread is in WAITING state because it is calling join() on transactions)%n");
            
        } catch (InterruptedException e) {
            System.err.println("[Main] Main thread interrupted during sleep.");
            Thread.currentThread().interrupt();
        }

        // -------------------------------------------------------------------------
        // 8. Join all threads to verify completions (Synchronizing main thread output)
        // -------------------------------------------------------------------------
        try {
            System.out.println("\n[Main] Main thread joining Statement Thread to block until everything is complete...");
            t6.join(); // Main thread waits for the statement thread, which in turn waits for transaction threads
            System.out.println("[Main] Statement Thread completed. Checking final states...");
        } catch (InterruptedException e) {
            System.err.println("[Main] Main thread interrupted during final join.");
            Thread.currentThread().interrupt();
        }

        // Shutdown the monitor thread
        monitor.shutdown();

        // -------------------------------------------------------------------------
        // 9. Demonstrate TERMINATED State
        // -------------------------------------------------------------------------
        System.out.println("\nDemonstrating Thread State: TERMINATED");
        System.out.printf("  [Main] Thread '%s' final state: %s%n", t1.getName(), t1.getState());
        System.out.printf("  [Main] Thread '%s' final state: %s%n", t6.getName(), t6.getState());
        System.out.println("-------------------------------------------------------------------------");

        // -------------------------------------------------------------------------
        // 10. Discuss Thread Priorities (Observed Behavior)
        // -------------------------------------------------------------------------
        System.out.println("\n=========================================================================");
        System.out.println("  DISCUSSION OF OBSERVED BEHAVIOR (THREAD PRIORITIES)");
        System.out.println("=========================================================================");
        System.out.println("1. Thread Priorities (MIN_PRIORITY=1, NORM_PRIORITY=5, MAX_PRIORITY=10) are hints");
        System.out.println("   to the Java Virtual Machine (JVM) thread scheduler regarding scheduling urgency.");
        System.out.println("2. In modern operating systems (like Windows, Linux, macOS), the JVM maps thread");
        System.out.println("   priorities directly to native OS thread priorities, but the OS thread scheduler");
        System.out.println("   retains final control. Under typical systems, the scheduler uses time-slicing");
        System.out.println("   and preemptive scheduling to prevent low-priority thread starvation.");
        System.out.println("3. Therefore, while high-priority threads (MAX_PRIORITY like Deposit-Thread-1) are");
        System.out.println("   statistically favored to get CPU time first, they do not block lower priority");
        System.out.println("   threads (like Interest-Thread-5) entirely. This explains why thread execution order");
        System.out.println("   may not be perfectly sequential in terms of priority numbers, especially with");
        System.out.println("   varying processing sleep delays.");
        System.out.println("=========================================================================\n");

        System.out.println("=========================================================================");
        System.out.println("  MULTITHREADED BANKING SYSTEM FRAMEWORK TEST COMPLETED");
        System.out.println("=========================================================================");
    }
}
