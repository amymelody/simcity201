package simcity.market.gui;

import javax.swing.*;

import simcity.CityDirectory;
import simcity.gui.BuildingGui;
import simcity.gui.BuildingsGui;
import simcity.gui.CityGui;
import simcity.market.MarketCashierRole;
import simcity.market.MarketEmployeeRole;
import simcity.market.MarketDelivererRole;
import simcity.market.MarketCustomerRole;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class MarketGui extends BuildingGui
{
	MarketAnimationPanel animationPanel;
	private MarketInputPanel inputPanel;
	
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;

	/**
	 * Constructor
	 */
	public MarketGui(String n, BuildingsGui bG, CityDirectory cD)
	{
		super(n, bG, cD);
		
		MarketCashierRole cashier;
		if (n.equals("market1")) {
			cashier = cD.getMarketCashiers().get(0);
		} else {
			cashier = cD.getMarketCashiers().get(1);
		} 
		
		animationPanel = new MarketAnimationPanel();
		inputPanel = new MarketInputPanel(this, n, cashier);

		//input panel
		double inputFractionOfWindow = 150 / 650;
		Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setVisible(false);
		bG.add(inputPanel);

		//animation panel
		double animationFractionOfWindow = 500 / 650;
		Dimension animDim = new Dimension((int)(WINDOWX * animationFractionOfWindow), WINDOWY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		animationPanel.setVisible(false);
		bG.add(animationPanel);
		
		
	}
	
	public void changeView(boolean visible) {
		animationPanel.setVisible(visible);
		inputPanel.setVisible(visible);
	}
	
	public void addMarketEmployee(MarketEmployeeRole e) {
		inputPanel.addEmployee(e);
	}
	
	public void addMarketDeliverer(MarketDelivererRole d) {
		inputPanel.addDeliverer(d);
	}
	
	public void addMarketCustomer(MarketCustomerRole c) {
		inputPanel.addCustomer(c);
	}
	
}