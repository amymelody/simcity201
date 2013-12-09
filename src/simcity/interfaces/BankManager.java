package simcity.interfaces;
<<<<<<< HEAD:src/simcity/interfaces/BankManager.java
=======


>>>>>>> anjali:src/simcity/interfaces/BankManager.java



public interface BankManager {

	
public abstract void msgStartShift();

public abstract void msgEndShift();

public abstract void msgDoneForTheDay();

public abstract void msgHired(BankTeller t, int salary);

public abstract void msgWereOpen();

public abstract void msgTransaction(BankDepositor c);

public abstract void msgProcessTransaction(BankTeller t, BankDepositor c, int cash);

public abstract void msgProcessLoanRequest(BankTeller t, BankDepositor c);

public abstract void msgImRobbingYourBank(BankDepositor c, int cash);

public abstract void msgHeresYourMoneyBack(BankDepositor c, int cash);
	
}