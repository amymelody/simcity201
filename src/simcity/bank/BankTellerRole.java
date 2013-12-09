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
import simcity.interfaces.BankDepositor;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;

import simcity.interfaces.Person;

import simcity.role.JobRole;
import simcity.role.Role;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

//
public class BankTellerRole extends JobRole implements BankTeller   {

	private String name;
	boolean working;
	
	private class myCustomer{
		BankDepositor c;
		CustomerState cS;
		String name;
		int money;
		int loanAmount;
		int request;
		
		myCustomer(BankDepositor c, CustomerState state){
			this.c = c;
			this.cS = state;
			
		}
		public CustomerState getCustomerState(){
			return cS;
		}
	}
	
	public enum CustomerState{waitingForTeller, makingRequest,broke, makingLoanRequest, 
		transactionComplete, loanApproved, loanDenied, waiting, leaving}
	
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
	
	public void setPerson(Person p){
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
	AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I've been assigned a customer");
	customers.add(new myCustomer(c, CustomerState.waitingForTeller));
	stateChanged();
}

public void msgMakeRequest(BankDepositor c, int transaction){
	AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I've received my customers request.");

		findCustomer(c).request = transaction;
		findCustomer(c).cS = CustomerState.makingRequest;
		stateChanged();
}
public void msgMakeLoanRequest(BankDepositor c){
	AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I've received my customers request to make a loan.");
	findCustomer(c).cS = CustomerState.makingLoanRequest;
	stateChanged();
}

public void msgTransactionComplete(BankDepositor c, int newCash){
	AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I've received confirmation from manager that transaction was successful.");
	findCustomer(c).money = newCash;
	findCustomer(c).cS = CustomerState.transactionComplete;
	stateChanged();
}
public void msgTransactionDenied(BankDepositor c){
	AlertLog.getInstance().logMessage(AlertTag.BANK, name, "Your transaction was denied. You should make a loan.");
	findCustomer(c).cS = CustomerState.broke;
	stateChanged();
}

public void msgLoanApproved(BankDepositor c, int newBalance){
	findCustomer(c).money = newBalance;
	findCustomer(c).loanAmount = newBalance;
	findCustomer(c).cS = CustomerState.loanApproved;
	stateChanged();
}

public void msgLoanDenied(BankDepositor c){
	findCustomer(c).cS = CustomerState.loanDenied;
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
	for(myCustomer b : customers){
		if(b.cS == CustomerState.makingLoanRequest){
			b.cS = CustomerState.waiting;
			makeLoan(b.c);
		}
	}
	for(myCustomer a : customers){
		if(a.cS == CustomerState.loanApproved){
			a.cS = CustomerState.waiting;
			loanApproved(a.c);
		}
	}
	for(myCustomer d : customers){
		if(d.cS == CustomerState.loanDenied){
			d.cS = CustomerState.waiting;
			loanDenied(d.c);
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
	AlertLog.getInstance().logMessage(AlertTag.BANK, name, "Your transaction was successful. Your new bank balance is: $" + newCash);
	c.msgTransactionComplete();
}

private void transactionDenied(BankDepositor c){
	c.msgCannotMakeTransaction();
	
}

private void makeLoan(BankDepositor c){
	manager.msgProcessLoanRequest(this, c);
}

private void loanApproved(BankDepositor c){
	AlertLog.getInstance().logMessage(AlertTag.BANK, name, "Your loan has been approved. Your new bank balance is $" 
			+ findCustomer(c).money + ". You have two years to repay this loan on 3% interest");
	c.msgTransactionComplete();
}

private void loanDenied(BankDepositor c){
	AlertLog.getInstance().logMessage(AlertTag.BANK, name, "Your loan has been denied, the bank does not have enough funds. Come back later. Sorry.");
	c.msgLoanDenied();
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

