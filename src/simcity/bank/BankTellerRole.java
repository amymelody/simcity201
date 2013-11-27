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
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.bank.gui.BankGui;
import simcity.bank.gui.BankTellerGui;
import simcity.bank.interfaces.BankDepositor;
import simcity.bank.interfaces.BankManager;
import simcity.bank.interfaces.BankTeller;
import simcity.role.JobRole;
import simcity.role.Role;

//
public class BankTellerRole extends JobRole implements BankTeller   {

	private String name;
	boolean working;
	private class myCustomer{
		BankDepositor c;
		CustomerState cS;
		String name;
		int money;
		
		myCustomer(BankDepositor c, CustomerState state, int cashInBank){
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
	
	private BankManager manager;
	
	List<myCustomer> customers = new ArrayList<myCustomer>();
	
	
	public BankTellerRole(){
		super();
	}
	public BankTellerRole(String name){
		this.name = name;
	}
	public String getMaitreDName(){
		return name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPerson(PersonAgent p){
		super.setPerson(p);
		name = p.getName();
	}
	
	public void setManager(BankManager manager){
		this.manager = manager;
	}
	public void setDepositor(BankDepositor depositor){
		this.depositor = depositor;
	}
	private BankDepositor depositor;
	
	private Semaphore tellerAnimation = new Semaphore(0,true);
	BankTellerGui gui = new BankTellerGui(this);

////MESSAGES/////

	public void msgStartShift(){
		working = true;
	}
	public void msgEndShift(){
		working = false;
	}
	public void msgPay(){
		person.msgEndShift();
	}
public void msgHelpCustomer(BankDepositor c, int cash){
	Do("Teller is assigned to customer");
	customers.add(new myCustomer(c, CustomerState.waitingForTeller, cash));
	stateChanged();
}

public void msgMakeWithdrawal(BankDepositor c, int transaction){
	Do("Teller is processing withdrawal");
	if(findCustomer(c).money<transaction){
		findCustomer(c).cS = CustomerState.broke;
	}
	else{
		findCustomer(c).money += -transaction;
		findCustomer(c).cS = CustomerState.makingRequest;
	}
	stateChanged();
}
public void msgMakeDeposit(BankDepositor c, int transaction){
	Do("Teller is processing deposit");
	findCustomer(c).money += transaction;
	findCustomer(c).cS = CustomerState.makingRequest;
	stateChanged();
}
public void msgTransactionComplete(BankDepositor c){
	Do("Teller received confirmation from manager that transaction was successful");
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
private void helpCustomer(BankDepositor c){
	gui.GoToCustomer();
	try {
		tellerAnimation.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	c.msgMakeRequest(this);
}
private void noMoney(BankDepositor c){
	
	c.msgCannotMakeTransaction();
	
}
private void makeTransaction(BankDepositor c){
	manager.msgProcessTransaction(this, c, findCustomer(c).money);
}

private void transactionComplete(BankDepositor c){
	c.msgTransactionComplete();
	Do("Your new bank balance is: $" + findCustomer(c).money);
}

private myCustomer findCustomer(BankDepositor c){
	
	for(myCustomer mc : customers){
		if(mc.c == c){
			return mc;
		}
	}
	return null;
}

public BankTellerGui getGui(){
	return gui;
}

public void setBankGui(BankGui g){
	gui.setGui(g);
}
}

