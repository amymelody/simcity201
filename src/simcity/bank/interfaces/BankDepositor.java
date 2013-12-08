package simcity.bank.interfaces;



public interface BankDepositor {
	
/////MESSAGES////////
	public abstract void msgMakeDeposit(int cash);
	
	public abstract void msgMakeWithdrawal(int cash);
	
	public abstract void msgBusinessDeposit(int cash);
	
	public abstract void msgMakeRequest(BankTeller t);
	
	public abstract void msgCannotMakeTransaction();
	
	public abstract void msgTransactionComplete();

	public abstract void msgGoToTellerDesk();

	public abstract void msgLoanDenied();
	
	public abstract void msgYoureDead();
	
	public abstract void msgLeaveMyBank();
	
	public abstract String getName();

	
}