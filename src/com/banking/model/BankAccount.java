package com.banking.model;

import com.banking.exception.InsufficientFundsException;

/**
 * Represents a bank account which is a shared resource accessed by multiple concurrent threads.
 * Uses synchronized methods and blocks to protect balance updates and prevent race conditions.
 *
 * Demonstrates requirements:
 * - Classes and Objects (OOP Design)
 * - Encapsulation (Private fields with public getters)
 * - Shared Resource (Bank Account accessed concurrently)
 * - Synchronization (Synchronized access)
 */
public class BankAccount {
    
    private final String accountNumber;
    private final String ownerName;
    private double balance;

    /**
     * Constructs a new BankAccount.
     *
     * @param accountNumber The unique identifier for this account.
     * @param ownerName     The name of the account holder.
     * @param initialBalance The starting balance.
     */
    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
    }

    /**
     * Gets the account number (encapsulated).
     * @return account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Gets the owner's name (encapsulated).
     * @return owner name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Thread-safe retrieval of the account balance.
     *
     * @return the current balance
     */
    public synchronized double getBalance() {
        return balance;
    }

    /**
     * Thread-safe deposit operation.
     *
     * @param amount the amount to deposit.
     */
    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            System.out.printf("[%s] Invalid deposit amount: $%.2f%n", Thread.currentThread().getName(), amount);
            return;
        }
        double oldBalance = this.balance;
        this.balance += amount;
        System.out.printf("[%s] DEPOSIT SUCCESS | Account: %s (%s) | Amount: +$%.2f | Old Balance: $%.2f | New Balance: $%.2f%n",
                Thread.currentThread().getName(), accountNumber, ownerName, amount, oldBalance, this.balance);
    }

    /**
     * Thread-safe withdrawal operation.
     *
     * @param amount the amount to withdraw.
     * @throws InsufficientFundsException if the balance is less than the withdrawal amount.
     */
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.printf("[%s] Invalid withdrawal amount: $%.2f%n", Thread.currentThread().getName(), amount);
            return;
        }
        if (this.balance < amount) {
            throw new InsufficientFundsException(
                    "Attempted withdrawal exceeded available balance.",
                    accountNumber, this.balance, amount
            );
        }
        double oldBalance = this.balance;
        this.balance -= amount;
        System.out.printf("[%s] WITHDRAW SUCCESS | Account: %s (%s) | Amount: -$%.2f | Old Balance: $%.2f | New Balance: $%.2f%n",
                Thread.currentThread().getName(), accountNumber, ownerName, amount, oldBalance, this.balance);
    }

    /**
     * Thread-safe transfer of funds from this account to a target account.
     * Implements a strict lock ordering strategy based on the account numbers to prevent deadlocks.
     *
     * @param target the target BankAccount to receive funds.
     * @param amount the amount to transfer.
     * @throws InsufficientFundsException if this account has insufficient funds.
     */
    public void transfer(BankAccount target, double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.printf("[%s] Invalid transfer amount: $%.2f%n", Thread.currentThread().getName(), amount);
            return;
        }

        // Deadlock Prevention: Order locks deterministically by comparing account numbers.
        // This ensures that if Thread 1 transfers A -> B and Thread 2 transfers B -> A,
        // they both attempt to lock in the exact same order (e.g., A then B), preventing circular wait.
        BankAccount firstLock = this.accountNumber.compareTo(target.accountNumber) < 0 ? this : target;
        BankAccount secondLock = this.accountNumber.compareTo(target.accountNumber) < 0 ? target : this;

        System.out.printf("[%s] TRANSFER ATTEMPT | From %s to %s | Amount: $%.2f | Requesting Locks...%n",
                Thread.currentThread().getName(), this.accountNumber, target.accountNumber, amount);

        synchronized (firstLock) {
            synchronized (secondLock) {
                // Once both locks are acquired, perform the transfer atomically
                System.out.printf("[%s] TRANSFER LOCKS ACQUIRED | Performing transfer of $%.2f from %s to %s%n",
                        Thread.currentThread().getName(), amount, this.accountNumber, target.accountNumber);
                
                // Withdraw from source (throws InsufficientFundsException if insufficient)
                this.withdraw(amount);
                
                // Deposit to target
                target.deposit(amount);

                System.out.printf("[%s] TRANSFER SUCCESS | From: %s | To: %s | Amount: $%.2f | Source New Balance: $%.2f | Target New Balance: $%.2f%n",
                        Thread.currentThread().getName(), this.accountNumber, target.accountNumber, amount, this.getBalance(), target.getBalance());
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Account[%s, Owner: %s, Balance: $%.2f]", accountNumber, ownerName, balance);
    }
}
