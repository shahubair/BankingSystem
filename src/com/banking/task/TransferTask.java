package com.banking.task;

import com.banking.model.BankAccount;

/**
 * Concrete task that performs a funds transfer between two BankAccounts.
 * Inherits template execution structure from BaseBankTask.
 */
public class TransferTask extends BaseBankTask {
    
    private final BankAccount sourceAccount;
    private final BankAccount targetAccount;
    private final double amount;

    /**
     * Constructs a TransferTask.
     *
     * @param taskName      The display name of the task.
     * @param sourceAccount The bank account to transfer funds from.
     * @param targetAccount The bank account to transfer funds to.
     * @param amount        The transfer amount.
     * @param delayMs       Simulated delay in milliseconds.
     */
    public TransferTask(String taskName, BankAccount sourceAccount, BankAccount targetAccount, double amount, long delayMs) {
        super(taskName, delayMs);
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
    }

    @Override
    public void execute() throws Exception {
        System.out.printf("[%s] Executing transfer of $%.2f from account %s to account %s...%n", 
                Thread.currentThread().getName(), amount, sourceAccount.getAccountNumber(), targetAccount.getAccountNumber());
        // Transfer funds using deadlock prevention strategy defined in BankAccount.transfer
        sourceAccount.transfer(targetAccount, amount);
    }
}
