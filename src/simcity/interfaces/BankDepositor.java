package simcity.interfaces;

public interface BankDepositor {

	public abstract void msgMakeDeposit(int amount);
	
	public abstract void msgMakeWithdrawal(int amount);

}