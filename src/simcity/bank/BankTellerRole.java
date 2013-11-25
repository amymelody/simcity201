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

import simcity.role.JobRole;
import simcity.role.Role;

//
public class BankTellerRole extends JobRole   {

	private String name;
	private class myCustomer{
		BankDepositorRole c;
		CustomerState cS;
		String name;
		double money;
		
		myCustomer(BankDepositorRole c, CustomerState state, double cashInBank){
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
public void msgHelpCustomer(BankDepositorRole c, double cash){
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
private void helpCustomer(BankTellerRole t, BankDepositorRole c){
	c.msgMakeRequest(this);
}

@Override
public void msgStartShift() {
	// TODO Auto-generated method stub
	
}

@Override
public void msgEndShift() {
	// TODO Auto-generated method stub
	
}
	
}

