/**
 * 
 */
/**
 * @author anjaliahuja2012
 *
 */
package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.bank.BankManagerRole.myCustomer;
import simcity.role.JobRole;
import simcity.role.Role;

//
public class BankTellerRole extends JobRole   {

	private String name;
	private class myCustomer{
		BankDepositorRole c;
		CustomerState cS;
		String name;
		int money;
		
		myCustomer(BankDepositorRole c, CustomerState state, int cashInBank){
			this.c = c;
			this.cS = state;
			this.name = c.getName();
			this.money = cashInBank;
			
		}
		public CustomerState getCustomerState(){
			return cS;
		}
	}
	
	public enum CustomerState{waitingForTeller, makingRequest,broke, transactionComplete, waiting, leaving}
	
	private BankManagerRole manager;
	
	List<myCustomer> customers = new ArrayList<myCustomer>();
	
	public BankTellerRole(String name){
		this.name = name;
	}
	
	public void setManager(BankManagerRole manager){
		this.manager = manager;
	}



////MESSAGES/////

	public void msgPay(){
		person.msgEndShift();
	}
public void msgHelpCustomer(BankDepositorRole c, int cash){
	customers.add(new myCustomer(c, CustomerState.waitingForTeller, cash));
	stateChanged();
}

public void msgMakeWithdrawal(BankDepositorRole c, int transaction){
	if(findCustomer(c).money<transaction){
		findCustomer(c).cS = CustomerState.broke;
	}
	else{
		findCustomer(c).money += transaction;
		findCustomer(c).cS = CustomerState.makingRequest;
	}
	stateChanged();
}
public void msgMakeDeposit(BankDepositorRole c, int transaction){
	findCustomer(c).money += transaction;
	findCustomer(c).cS = CustomerState.makingRequest;
	stateChanged();
}
public void msgTransactionComplete(BankDepositorRole c){
	findCustomer(c).cS = CustomerState.transactionComplete;
	stateChanged();
}

///SCHEDULER
public boolean pickAndExecuteAnAction(){
	for(myCustomer c : customers){
		if(c.getCustomerState() == CustomerState.waitingForTeller){
			helpCustomer(c.c);
		}
	
	}
	for(myCustomer j : customers){
		if(j.getCustomerState() == CustomerState.broke){
			noMoney(j.c);
		}
	}
	for(myCustomer x : customers){
		if(x.getCustomerState() == CustomerState.makingRequest){
			makeTransaction(x.c);
		}
	}
	for(myCustomer l : customers){
		if(l.getCustomerState() == CustomerState.transactionComplete){
			transactionComplete(l.c);
		}
	}
	return false;
}

///ACTIONS////
private void helpCustomer(BankDepositorRole c){
	c.msgMakeRequest(this);
}
private void noMoney(BankDepositorRole c){
	c.msgCannotMakeTransaction();
	
}
private void makeTransaction(BankDepositorRole c){
	manager.msgProcessTransaction(this, c, findCustomer(c).money);
}

private void transactionComplete(BankDepositorRole c){
	c.msgTransactionComplete();
	Do("Your new bank balance is: $" + findCustomer(c).money);
}

@Override
public void msgStartShift() {
	// TODO Auto-generated method stub
	
}

@Override
public void msgEndShift() {
	// TODO Auto-generated method stub
	
}
	
private myCustomer findCustomer(BankDepositorRole c){
	
	for(myCustomer mc : customers){
		if(mc.c == c){
			return mc;
		}
	}
	return null;
}
}

