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
	protected CityGui cityGui;
	protected CityDirectory cityDirectory;
	protected MarketAnimationPanel animationPanel;
	protected MarketInputPanel inputPanel;

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
	
	//public abstract void changeView(boolean visible);
	
	public void changeCityView() {
		cityGui.changeView();
	}
}