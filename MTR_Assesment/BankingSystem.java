package day6;
import java.util.*;
import java.time.LocalDateTime;

class InvalidAmountException extends Exception{
	public InvalidAmountException(String message) {
		super(message);
	}
}
class InsufficientBalanceException extends Exception{
	public InsufficientBalanceException(String message) {
		super(message);
	}
}
class Transaction{
	private final double amount;
	private final String type;
	private final LocalDateTime timestamp;
	private final String userId;
	public Transaction(double amount, String type, String userId) {
		this.amount = amount;
		this.type = type;
		this.userId = userId;
		this.timestamp = LocalDateTime.now();
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	@Override
	public String toString() {
		return timestamp + " | "+ type + " | "+ amount + " | User: "+ userId;
	}
}
abstract class BankAccount{
	private double balance;
	private final String userId;
	protected List<Transaction>transactions;
	public BankAccouunt(String userId) {
		this.userId = userId;
		this.transactions = new ArrayList<>();
	}
	public void deposit(int amount)
throws InvalidAmountException{
		deposit((double)amount);
	}
	public void deposit(double amount)
throws InvalidAmountException{
		if(amount <= 0)
			throw new InvalidAmountException("Amount must be positive.");
		balance += amount;
		transactions.add(new Transaction(amount, "deposit", userId));
	}
	public abstract void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException;
	protected void deduct(double amount) {
		balance -= amount;
	}
	public double getBalance() {
		return balance;
	}
	public String getUserId() {
		return userId;
	}
	public void printStatement() {
		System.out.println("Balance for"+userId+": "+balance);
	}
	public void printTransactionHistory() {
		transactions.stream().sorted(Comparator.comparing(Transaction::getTimestamp)).forEach(System.out::println);
	}
}
class RegularAccount extends BankAccount{
	public RegularAccount(String userId) {
		super(userId);
	}
	@Override
	public void withdraw(double amount)
	throws InvalidAmountException, InsufficientBalanceException{
		if(amount <= 0)
			throw new InvalidAmountException("Withdrawal must be positive.");
		if(amount <= 0)
			throw new InsufficientBalanceException("Insufficient balance.");
		deduct(amount);
		transactions.add(new Transaction(-amount,"withdraw",getUserId()));
	}
}
class PremiumAccount extends BankAccount{
	public PremiumAccount(String userId) {
		super(userId);
	}
	@Override
	public void withdraw(double amount)
	throws InvalidAmountException{
		if(amount <= 0)
			throw new InvalidAmountException("Withdrawal must be positive.");
		deduct(amount);
		transactions.add(new Transaction(-amount, "withdraw", getUserId()));
	}
}
public class BankingSystem {
	public static void main(String[]args) {
		try {
			BankAccount acc1 = new RegularAccount("user123");
			acc1.deduct(500);
			acc1.withdraw(200);
			acc1.printStatement();
		    System.out.println("Transaction History for Regular Account:");
		    acc1.printTransactionHistory();
		    System.out.println("\n---------------------------------\n");
		    BankAccount acc2 = PremiumAccount("user456");
		    acc2.deposit(300);
		    acc2.withdraw(400);
		    acc2.printStatement();
		    System.out.println("Transaction History for Premium Account:");
		    acc2.printTransactionHistory();
		}catch (InvalidAmountException | InsufficientBalanceException e) {
			System.out.println("Transaction failed:"+e.getMessage());
		}
	}

	private static BankAccount PremiumAccount(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
