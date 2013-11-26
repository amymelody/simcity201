package simcity.interfaces;

import simcity.bank.interfaces.BankTeller;

public interface BankDepositor {

	
	public abstract void msgMakeDeposit(int amount);
	
	public abstract void msgMakeWithdrawal(int amount);
	
	public abstract void msgMarketDeposit(int cash);
	
	public abstract void msgCannotMakeTransaction();
	
	public abstract void msgMakeRequest(BankTeller t);
	
	public abstract void msgTransactionComplete();
	
	
}