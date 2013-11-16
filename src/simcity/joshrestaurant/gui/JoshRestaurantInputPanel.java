package simcity.joshrestaurant.gui;

import javax.swing.JPanel;

import simcity.gui.BuildingGui;

public class JoshRestaurantInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel
	
	BuildingGui gui;
	
	public JoshRestaurantInputPanel(BuildingGui g)
	{
		gui = g;
	}
}
