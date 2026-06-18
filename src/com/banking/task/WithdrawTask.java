package com.banking.task;

import com.banking.model.BankAccount;

/**
 * Concrete task that performs a withdrawal operation on a BankAccount.
 * Inherits template execution structure from BaseBankTask.
 * May throw InsufficientFundsException which is handled by the framework.
 */
public class WithdrawTask extends BaseBankTask {
    
    private final BankAccount account;
    private final double amount;

    /**
     * Constructs a WithdrawTask.
     *
     * @param taskName The display name of the task.
     * @param account  The bank account to withdraw funds from.
     * @param amount   The withdrawal amount.
     * @param delayMs  Simulated delay in milliseconds.
     */
    public WithdrawTask(String taskName, BankAccount account, double amount, long delayMs) {
        super(taskName, delayMs);
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws Exception {
        System.out.printf("[%s] Executing withdrawal of $%.2f from account %s...%n", 
                Thread.currentThread().getName(), amount, account.getAccountNumber());
        // Throws InsufficientFundsException if the account has insufficient balance
        account.withdraw(amount);
    }
}
