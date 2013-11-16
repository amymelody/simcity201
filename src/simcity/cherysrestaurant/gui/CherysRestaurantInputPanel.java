package simcity.cherysrestaurant.gui;

import javax.swing.JPanel;

import simcity.gui.BuildingGui;

public class CherysRestaurantInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel
	
	BuildingGui gui;
	
	public CherysRestaurantInputPanel(BuildingGui g)
	{
		gui = g;
	}
}
