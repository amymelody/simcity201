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
		int request;
		
		myCustomer(BankDepositor c, CustomerState state){
			this.c = c;
			this.cS = state;
			
		}
		public CustomerState getCustomerState(){
			return cS;
		}
	}
	
	public enum CustomerState{waitingForTeller, makingRequest,broke, 
		transactionComplete, waiting, leaving}
	
	private BankManager manager;
	
	public List<myCustomer> customers = new ArrayList<myCustomer>();
	
	
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
		manager.msgHired(this, person.getSalary());
	}
	public void setDepositor(BankDepositor depositor){
		this.depositor = depositor;
	}
	public void setGui(BankTellerGui g) {
		gui = g;
	}
	private BankDepositor depositor;
	
	private Semaphore tellerAnimation = new Semaphore(0,true);
	BankTellerGui gui;

////MESSAGES/////

	public void msgAtDestination(){
		tellerAnimation.release();
	}
	public void msgStartShift(){
		working = true;
	}
	public void msgEndShift(){
		working = false;
	}
	public void msgPay(){
		person.msgEndShift();
	}
public void msgHelpCustomer(BankDepositor c){
	Do("Teller is assigned to customer");
	customers.add(new myCustomer(c, CustomerState.waitingForTeller));
	stateChanged();
}

public void msgMakeRequest(BankDepositor c, int transaction){
		findCustomer(c).request = transaction;
		findCustomer(c).cS = CustomerState.makingRequest;
		stateChanged();
}

public void msgTransactionComplete(BankDepositor c, int newCash){
	Do("Teller received confirmation from manager that transaction was successful");
	findCustomer(c).money = newCash;
	findCustomer(c).cS = CustomerState.transactionComplete;
	stateChanged();
}
public void msgTransactionDenied(BankDepositor c){
	Do("Sorry, your transaction was denied because you do not have enough funds to make this deposit");
	findCustomer(c).cS = CustomerState.broke;
	stateChanged();
}

///SCHEDULER
public boolean pickAndExecuteAnAction(){
	for(myCustomer c : customers){
		if(c.cS == CustomerState.waitingForTeller){
			c.cS = CustomerState.waiting;
			helpCustomer(c.c);
			
		}
	
	}
	for(myCustomer j : customers){
		if(j.cS == CustomerState.broke){
			j.cS = CustomerState.waiting;
			transactionDenied(j.c);
		}
	}
	for(myCustomer x : customers){
		if(x.cS == CustomerState.makingRequest){
			x.cS = CustomerState.waiting;
			makeTransaction(x.c);
		}
	}
	for(myCustomer l : customers){
		if(l.cS == CustomerState.transactionComplete){
			l.cS = CustomerState.waiting;
			transactionComplete(l.c, l.money);
		}
	}

	
	return false;
}

///ACTIONS////
private void helpCustomer(BankDepositor c){
	gui.GoToManager();
	try {
		tellerAnimation.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	gui.GoToDesk();
	c.msgGoToTellerDesk();
	try {
		tellerAnimation.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	c.msgMakeRequest(this);
	
}

private void makeTransaction(BankDepositor c){
	manager.msgProcessTransaction(this, c, findCustomer(c).request);
}

private void transactionComplete(BankDepositor c, int newCash){
	Do("Your new bank balance is: $" + newCash);
	c.msgTransactionComplete();
}

private void transactionDenied(BankDepositor c){
	
	c.msgCannotMakeTransaction();
	
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

