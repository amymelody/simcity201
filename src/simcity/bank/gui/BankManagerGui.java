package simcity.bank.gui;

import javax.swing.JPanel;

import simcity.gui.BuildingGui;

public class BankManagerGui extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel
	
	BuildingGui gui;
	
	public BankManagerGui(BuildingGui g)
	{
		gui = g;
	}
}
