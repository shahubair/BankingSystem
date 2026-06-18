package com.banking.task;

import com.banking.model.BankAccount;
import java.util.List;

/**
 * Concrete task that generates account statements for a list of BankAccounts.
 * Enforces task dependencies using the join() method on other transaction threads.
 *
 * Demonstrates requirements:
 * - Join Method (Enforces task dependencies using join())
 * - Transition of this thread into WAITING state while waiting for dependencies
 */
public class StatementGenerationTask extends BaseBankTask {
    
    private final List<BankAccount> accounts;
    private final List<Thread> dependencyThreads;

    /**
     * Constructs a StatementGenerationTask.
     *
     * @param taskName          The display name of the task.
     * @param accounts          The list of bank accounts to generate reports for.
     * @param dependencyThreads The threads that must finish execution before the statement is generated.
     * @param delayMs           Simulated delay in milliseconds.
     */
    public StatementGenerationTask(String taskName, List<BankAccount> accounts, List<Thread> dependencyThreads, long delayMs) {
        super(taskName, delayMs);
        this.accounts = accounts;
        this.dependencyThreads = dependencyThreads;
    }

    @Override
    public void execute() throws Exception {
        String threadName = Thread.currentThread().getName();
        
        System.out.printf("[%s] StatementGenerationTask is entering WAITING state via join() on dependency threads...%n", 
                threadName);

        // Enforce dependencies using join() (Requirement 8)
        for (Thread thread : dependencyThreads) {
            if (thread != null) {
                System.out.printf("[%s] StatementGenerationTask waiting on thread '%s' to complete. Current dependency state: %s%n", 
                        threadName, thread.getName(), thread.getState());
                
                // This call puts the statement-generation thread into the WAITING state.
                // The MonitorThread will capture this WAITING state.
                thread.join();
                
                System.out.printf("[%s] Dependency thread '%s' joined successfully. Finished in state: %s%n", 
                        threadName, thread.getName(), thread.getState());
            }
        }

        // Generate and print statements once all dependencies are complete
        System.out.printf("%n=== [%s] GENERATING CONSOLIDATED BANKING STATEMENT ===%n", threadName);
        for (BankAccount account : accounts) {
            // Synchronized block to ensure consistent view of the final balance
            synchronized (account) {
                System.out.printf("  - %s%n", account.toString());
            }
        }
        System.out.printf("========================================================%n%n");
    }
}
