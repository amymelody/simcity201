package simcity.housing.gui;

import javax.swing.JPanel;

import simcity.gui.BuildingGui;

public class HousingInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel
	
	BuildingGui gui;
	
	public HousingInputPanel(BuildingGui g)
	{
		gui = g;
	}
}
