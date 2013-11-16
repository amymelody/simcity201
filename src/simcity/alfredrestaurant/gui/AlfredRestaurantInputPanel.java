package simcity.alfredrestaurant.gui;

import javax.swing.JPanel;

import simcity.gui.BuildingGui;

public class AlfredRestaurantInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel

	BuildingGui gui;
	
	public AlfredRestaurantInputPanel(BuildingGui g)
	{
		gui = g;
	}
}
