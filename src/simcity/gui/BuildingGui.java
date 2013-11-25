package simcity.gui;

import javax.swing.JPanel;

import simcity.CityDirectory;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public abstract class BuildingGui
{
	String name;
	private CityGui cityGui;
	private CityDirectory cityDirectory;

	/**
	 * Constructor
	 */
	public BuildingGui(String n, CityGui cG, CityDirectory cD)
	{
		cityGui = cG;
		cityDirectory = cD;
		name = n;
	}
	public String getName() {
		return name;
	}
	public abstract void setVisible(boolean visible);
}