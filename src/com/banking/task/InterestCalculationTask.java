package com.banking.task;

import com.banking.model.BankAccount;

/**
 * Concrete task that calculates and applies monthly interest on a BankAccount.
 * Demonstrates a low-priority background task.
 * Inherits template execution structure from BaseBankTask.
 */
public class InterestCalculationTask extends BaseBankTask {
    
    private final BankAccount account;
    private final double interestRate; // e.g. 0.05 for 5%

    /**
     * Constructs an InterestCalculationTask.
     *
     * @param taskName     The display name of the task.
     * @param account      The bank account to apply interest to.
     * @param interestRate The annual/monthly interest rate.
     * @param delayMs      Simulated delay in milliseconds.
     */
    public InterestCalculationTask(String taskName, BankAccount account, double interestRate, long delayMs) {
        super(taskName, delayMs);
        this.account = account;
        this.interestRate = interestRate;
    }

    @Override
    public void execute() throws Exception {
        // Synchronized block to fetch balance and perform atomic interest calculation and deposit
        synchronized (account) {
            double currentBalance = account.getBalance();
            double interestAmount = currentBalance * interestRate;
            System.out.printf("[%s] Calculating interest on account %s | Balance: $%.2f | Rate: %.1f%% | Interest: $%.2f%n", 
                    Thread.currentThread().getName(), account.getAccountNumber(), currentBalance, interestRate * 100, interestAmount);
            if (interestAmount > 0) {
                account.deposit(interestAmount);
            } else {
                System.out.printf("[%s] Interest calculation: No positive interest to apply to account %s.%n",
                        Thread.currentThread().getName(), account.getAccountNumber());
            }
        }
    }
}
