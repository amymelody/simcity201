package simcity.jesusrestaurant.gui;

import javax.swing.JPanel;

import simcity.gui.BuildingGui;

public class JesusRestaurantInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel
	
	BuildingGui gui;
	
	public JesusRestaurantInputPanel(BuildingGui g)
	{
		gui = g;
	}
}
