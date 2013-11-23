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

import simcity.role.Role;


public class BankTellerRole extends Role   {

	private String name;
	private class myCustomer{
		BankCustomerRole c;
		CustomerState cS;
		String name;
		double money;
		
		myCustomer(BankCustomerRole c, CustomerState state, int cashInBank){
			this.c = c;
			this.cS = state;
			this.name = c.getName();
			this.money = cashInBank;
			
		}
		public CustomerState getCustomerState(){
			return cS;
		}
	}
	
	public enum CustomerState{waitingForTeller, makingRequest, waiting, leaving}
	
	private BankManagerRole manager;
	
	List<myCustomer> customers = new ArrayList<myCustomer>();
	
	public BankTellerRole(String name){
		this.name = name;
	}
	
	public void setManager(BankManagerRole manager){
		this.manager = manager;
	}



////MESSAGES/////
public void msgHelpCustomer(BankCustomerRole c, double cash){
	customers.add(new myCustomer(c, CustomerState.waitingForTeller, cash));
	stateChanged();
}

///SCHEDULER
public boolean pickAndExecuteAnAction(){
	for(myCustomer c : customers){
		if(c.getCustomerState() == CustomerState.waitingForTeller){
			helpCustomer(this, c.c);
		}
	}
	return false;
}

///ACTIONS////
private void helpCustomer(BankTellerRole t, BankCustomerRole c){
	c.msgMakeRequest(this);
}
	
}

