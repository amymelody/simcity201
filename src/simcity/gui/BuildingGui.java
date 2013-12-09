package simcity.gui;

import javax.swing.JPanel;

import simcity.CityDirectory;
import simcity.market.gui.MarketAnimationPanel;
import simcity.market.gui.MarketInputPanel;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public abstract class BuildingGui
{
	String name;
	protected CityDirectory cityDirectory;
	protected BuildingsGui buildingsGui;

	/**
	 * Constructor
	 */
	public BuildingGui(String n, BuildingsGui bG, CityDirectory cD)
	{
		cityDirectory = cD;
		buildingsGui = bG;
		name = n;
	}
	public String getName() {
		return name;
	}
	
	public abstract void changeView(boolean visible);
}