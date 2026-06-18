package com.banking.exception;

/**
 * User-defined custom exception representing an error when a transaction (withdrawal or transfer)
 * is attempted on a bank account with insufficient funds.
 * 
 * Demonstrates the requirement: At least one user-defined exception.
 */
public class InsufficientFundsException extends Exception {
    
    private final String accountNumber;
    private final double currentBalance;
    private final double attemptedAmount;

    /**
     * Constructs a new InsufficientFundsException with detailed context.
     *
     * @param message          The detailed error message.
     * @param accountNumber    The account number where the transaction failed.
     * @param currentBalance   The current balance of the account.
     * @param attemptedAmount  The amount that was attempted to be withdrawn or transferred.
     */
    public InsufficientFundsException(String message, String accountNumber, double currentBalance, double attemptedAmount) {
        super(message);
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.attemptedAmount = attemptedAmount;
    }

    /**
     * Gets the account number associated with the exception.
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Gets the balance of the account when the error occurred.
     * @return current balance
     */
    public double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Gets the amount that was attempted.
     * @return attempted amount
     */
    public double getAttemptedAmount() {
        return attemptedAmount;
    }

    @Override
    public String toString() {
        return "InsufficientFundsException{" +
                "message='" + getMessage() + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", currentBalance=$" + String.format("%.2f", currentBalance) +
                ", attemptedAmount=$" + String.format("%.2f", attemptedAmount) +
                '}';
    }
}
