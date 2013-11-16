package simcity.market.gui;

import javax.swing.JPanel;

import simcity.gui.BuildingGui;

public class MarketInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel
	
	BuildingGui gui;
	
	public MarketInputPanel(BuildingGui g)
	{
		gui = g;
	}
}
