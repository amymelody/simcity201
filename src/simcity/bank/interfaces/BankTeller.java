package simcity.bank.interfaces;



public interface BankTeller {
	
/////MESSAGES////////
public abstract void msgStartShift();

public abstract void msgEndShift();

public abstract void msgPay();

public abstract void msgHelpCustomer(BankDepositor c, int cash);

public abstract void msgMakeWithdrawal(BankDepositor c, int transaction);

public abstract void msgMakeDeposit(BankDepositor c, int transaction);

public abstract void msgTransactionComplete(BankDepositor c);
	
}