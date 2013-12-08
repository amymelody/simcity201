package simcity.bank.interfaces;



public interface BankManager {
	
/////MESSAGES////////

public abstract void msgStartShift();

public abstract void msgEndShift();

public abstract void msgDoneForTheDay();

public abstract void msgHired(BankTeller t, int salary);

public abstract void msgWereOpen();

public abstract void msgTransaction(BankDepositor c);

public abstract void msgProcessTransaction(BankTeller t, BankDepositor c, int cash);

public abstract void msgProcessLoanRequest(BankTeller t, BankDepositor c);

}