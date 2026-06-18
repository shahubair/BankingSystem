package com.banking.task;

import com.banking.model.BankAccount;

/**
 * Concrete task that performs a deposit operation on a BankAccount.
 * Inherits template execution structure from BaseBankTask.
 */
public class DepositTask extends BaseBankTask {
    
    private final BankAccount account;
    private final double amount;

    /**
     * Constructs a DepositTask.
     *
     * @param taskName The display name of the task.
     * @param account  The bank account to deposit funds to.
     * @param amount   The deposit amount.
     * @param delayMs  Simulated delay in milliseconds.
     */
    public DepositTask(String taskName, BankAccount account, double amount, long delayMs) {
        super(taskName, delayMs);
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws Exception {
        System.out.printf("[%s] Executing deposit of $%.2f to account %s...%n", 
                Thread.currentThread().getName(), amount, account.getAccountNumber());
        account.deposit(amount);
    }
}
