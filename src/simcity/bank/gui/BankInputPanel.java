package simcity.bank.gui;

import java.util.Vector;

import javax.swing.JPanel;

import simcity.bank.BankManagerRole;
import simcity.bank.BankTellerRole;
import simcity.bank.BankDepositorRole;

public class BankInputPanel extends JPanel 
{
	public String name;
	public BankGui bankGui;
	
	private BankManagerRole manager;
	private BankManagerGui managerGui;
	private Vector<BankTellerRole> tellers = new Vector<BankTellerRole>();
	private Vector<BankDepositorRole> depositors = new Vector<BankDepositorRole>();
	
	
	public BankInputPanel(BankGui g, BankManagerRole c) {
		bankGui = g;
		manager = c;
		
		BankManagerGui managerGui = new BankManagerGui(manager);
		bankGui.animationPanel.addGui(managerGui);
		manager.setGui(managerGui);
	}
	
	public void setManager(BankManagerRole manager) {
		this.manager = manager;
	}
	
	public void addTeller(BankTellerRole t) {
		BankTellerGui g = new BankTellerGui(t);
		bankGui.animationPanel.addGui(g);
		t.setManager(manager);
		t.setGui(g);
		tellers.add(t);
		manager.addTeller(t);
	}
	
	public void addDepositor(BankDepositorRole d) {
		BankDepositorGui g = new BankDepositorGui(d);
		bankGui.animationPanel.addGui(g);
		d.setManager(manager);
		d.setGui(g);
		depositors.add(d);
	}
}
