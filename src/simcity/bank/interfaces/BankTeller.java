package simcity.bank.interfaces;



public interface BankTeller {
	
/////MESSAGES////////
public abstract void msgStartShift();

public abstract void msgEndShift();

public abstract void msgPay();

public abstract void msgHelpCustomer(BankDepositor c);


public abstract void msgTransactionComplete(BankDepositor c, int cashInBank);

public abstract void msgMakeRequest(BankDepositor c,
		int transactionAmount);

public abstract void msgMakeLoanRequest(BankDepositor c);

public abstract void msgLoanApproved(BankDepositor c, int newBalance);

public abstract void msgLoanDenied(BankDepositor c);
public abstract void msgTransactionDenied(BankDepositor c);
	
}