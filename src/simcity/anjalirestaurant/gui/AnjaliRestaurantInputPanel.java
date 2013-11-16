package simcity.anjalirestaurant.gui;

import javax.swing.JPanel;

import simcity.gui.BuildingGui;

public class AnjaliRestaurantInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel
	
	BuildingGui gui;
	
	public AnjaliRestaurantInputPanel(BuildingGui g)
	{
		gui = g;
	}
}
