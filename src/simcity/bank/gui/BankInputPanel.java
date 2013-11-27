package simcity.bank.gui;

import java.util.Vector;

import javax.swing.JPanel;

import simcity.bank.BankManagerRole;
import simcity.bank.BankTellerRole;
import simcity.bank.BankDepositorRole;

public class BankInputPanel extends JPanel 
{
	private BankManagerRole manager;
	private Vector<BankTellerRole> tellers = new Vector<BankTellerRole>();
	private Vector<BankDepositorRole> depositors = new Vector<BankDepositorRole>();
	private BankGui gui;
	
	public BankInputPanel(BankGui g, BankManagerRole m) {
		gui = g;
		manager = m;
		
		BankManagerGui managerGui = new BankManagerGui(manager);
		gui.animationPanel.addGui(managerGui);
		manager.setGui(managerGui);
	}
	
	public void setManager(BankManagerRole manager) {
		this.manager = manager;
	}
	
	public void addTeller(BankTellerRole t) {
		BankTellerGui g = new BankTellerGui(t);
		gui.animationPanel.addGui(g);
		t.setManager(manager);
		t.setGui(g);
		tellers.add(t);
		manager.addTeller(t);
	}
	
	public void addDepositor(BankDepositorRole d) {
		BankDepositorGui g = new BankDepositorGui(d);
		gui.animationPanel.addGui(g);
		d.setManager(manager);
		d.setGui(g);
		depositors.add(d);
	}
}
