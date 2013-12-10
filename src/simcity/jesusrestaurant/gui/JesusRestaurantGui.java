package simcity.jesusrestaurant.gui;

import simcity.CityDirectory;
import simcity.gui.BuildingGui;
import simcity.gui.BuildingsGui;
import simcity.jesusrestaurant.JesusCustomerRole;
import simcity.jesusrestaurant.JesusNormalWaiterRole;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.joshrestaurant.JoshWaiterRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class JesusRestaurantGui extends BuildingGui {
	//static declarations
	static final int LSPACE = 10;
 
	public JesusRestaurantInputPanel inputPanel;
	public JesusRestaurantAnimationPanel animationPanel;
	
	/**
	 * Constructor for RestaurantGui class.
	 * Sets up all the gui components.
	 */
	public JesusRestaurantGui(String n, BuildingsGui bG, CityDirectory cD) {
		super(n, bG, cD);
		
		animationPanel = new JesusRestaurantAnimationPanel();
		inputPanel = new JesusRestaurantInputPanel(this, cD.getJesusCashier(), cD.getJesusCook(), cD.getJesusHost(), cD.getMarketCashiers());
		
		Dimension animDim = new Dimension((int) (500.0), (int) (500.0));
		animationPanel.setPreferredSize(animDim);
		bG.add(animationPanel, BorderLayout.CENTER);
	
		Dimension infoDim = new Dimension((int) (150.0), (int) (500.0));
		inputPanel.setPreferredSize(infoDim);
		bG.add(inputPanel, BorderLayout.WEST);
	}

	public void changeView(boolean visible) {
		animationPanel.setVisible(visible);
		inputPanel.setVisible(visible);
	}
	
	public void addCustomer(JesusCustomerRole c) {
    	inputPanel.addCustomer(c);
    }
    
    public void addWaiter(JesusNormalWaiterRole w) {
    	inputPanel.addWaiter(w);
    }
	
}