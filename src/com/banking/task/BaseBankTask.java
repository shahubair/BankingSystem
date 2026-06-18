package com.banking.task;

/**
 * Abstract base class that implements BankTask and provides template execution logic.
 * Wraps execution in standard log outputs, sleep delays, and exception handlers.
 *
 * Demonstrates requirements:
 * - Inheritance (OOP Design)
 * - Sleep Method (Uses Thread.sleep() to simulate latency)
 * - Runtime Polymorphism (Call to abstract execute() method)
 * - Exception Handling (Handles InterruptedException and general Exceptions)
 */
public abstract class BaseBankTask implements BankTask {
    
    protected final String taskName;
    protected final long delayMs;

    /**
     * Initializes the base bank task.
     *
     * @param taskName The display name of the task.
     * @param delayMs  Simulated network or database delay in milliseconds.
     */
    protected BaseBankTask(String taskName, long delayMs) {
        this.taskName = taskName;
        this.delayMs = delayMs;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }

    /**
     * Final implementation of Runnable.run() using the Template Method pattern.
     * Invokes the concrete execute() logic after processing delays.
     */
    @Override
    public final void run() {
        String threadName = Thread.currentThread().getName();
        System.out.printf("[%s] ---> Task Started: %s%n", threadName, taskName);
        
        try {
            // Simulate processing delay (Requirement 7)
            if (delayMs > 0) {
                System.out.printf("[%s] Task '%s' simulating delay, sleeping for %d ms...%n", 
                        threadName, taskName, delayMs);
                Thread.sleep(delayMs);
            }
            
            // Execute polymorphic task logic
            execute();
            
        } catch (InterruptedException e) {
            // Handle InterruptedException (Requirement 11)
            System.err.printf("[%s] !!! InterruptedException caught in Task '%s': %s%n", 
                    threadName, taskName, e.getMessage());
            // Restore interrupted status so callers or manager can react if needed
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // Handle other exceptions (e.g., custom InsufficientFundsException) (Requirement 11)
            System.err.printf("[%s] !!! Exception caught in Task '%s': %s%n", 
                    threadName, taskName, e.getMessage());
        } finally {
            System.out.printf("[%s] <--- Task Finished: %s%n", threadName, taskName);
        }
    }
}
