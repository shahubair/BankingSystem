package com.banking.task;

/**
 * Core interface for tasks in the Banking Task Management Framework.
 * Extends Runnable to represent task execution in threads.
 *
 * Demonstrates requirements:
 * - Interfaces (OOP Design)
 * - Runnable-Based Task Design (via extension of Runnable)
 */
public interface BankTask extends Runnable {
    
    /**
     * Gets the meaningful name of the task.
     * @return the name of the task.
     */
    String getTaskName();

    /**
     * Execution logic hook where concrete tasks define their actions.
     * Allows throwing general exceptions which are handled by the base task template.
     *
     * @throws Exception if a business logic or runtime error occurs.
     */
    void execute() throws Exception;
}
