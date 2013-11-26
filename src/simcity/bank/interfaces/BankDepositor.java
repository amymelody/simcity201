package simcity.bank.interfaces;


/**
 * Restaurant Cashier Agent, Scenario 1
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios
public interface BankDepositor {
	
/////MESSAGES////////
	public abstract void msgMakeDeposit(int cash);
	
	public abstract void msgMakeWithdrawal(int cash);
	
	public abstract void msgMakeRequest(BankTeller t);
	
	public abstract void msgCannotMakeTransaction();
	
	public abstract void msgTransactionComplete();
	
	
}