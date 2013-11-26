package simcity.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import simcity.CityDirectory;
import simcity.market.gui.MarketGui;
import simcity.bank.gui.BankGui;
import simcity.housing.LandlordRole;
import simcity.housing.ResidentRole;
import simcity.housing.gui.HousingGui;
import simcity.joshrestaurant.gui.JoshRestaurantGui;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.joshrestaurant.JoshNormalWaiterRole;
import simcity.joshrestaurant.JoshSharedDataWaiterRole;
import simcity.RestWaiterRole;
import simcity.RestCustomerRole;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class CityGui extends JFrame
{
	private JFrame animationFrame = new JFrame("City Animation");
	CityAnimationPanel animationPanel;
	private CityInputPanel inputPanel;
	private static CityDirectory cityDirectory;

	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;

	/**
	 * Constructor
	 */
	public CityGui(CityDirectory cd)
	{		
		animationPanel = new CityAnimationPanel(this);
		inputPanel = new CityInputPanel(this, cd, null);

		setBounds(BUFFERSIDE, BUFFERTOP, WINDOWX, WINDOWY);
		BorderLayout frameLayout = new BorderLayout();
		setLayout(frameLayout);

		// input panel
		double inputFractionOfWindow = 150.0 / 650.0;
		Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		add(inputPanel, frameLayout.WEST);

		// animation panel
		double animationFractionOfWindow = 500.0 / 650.0;
		Dimension animDim = new Dimension((int)(WINDOWX * animationFractionOfWindow), WINDOWY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		add(animationPanel, frameLayout.CENTER);
	}

	public void addGui(PersonGui g) {
		animationPanel.addGui(g);
	}

	public void addRestCustomer(RestCustomerRole c) {
		animationPanel.addRestCustomer(c);
	}

	public void addRestWaiter(RestWaiterRole w) {
		animationPanel.addRestWaiter(w);
	}
	
	public void addResident(ResidentRole r, String homeName, String ownerHomeName) {
		animationPanel.addResident(r, homeName, ownerHomeName);
	}
	
	public void addLandlord(LandlordRole l, String buildingName) {
		animationPanel.addLandlord(l, buildingName);
	}
	
	/*public void addMarketCustomer(MarketCustomerRole c) {
		animationPanel.addMarketCustomer(c);
	}
	
	public void addMarketEmployee(MarketEmployeeRole e) {
		animationPanel.addMarketEmployee(e);
	}
	
	public void addMarketDeliverer(MarketDelivererRole d) {
		animationPanel.addMarketDeliverer(d);
	}
	
	public void addBankDepositor(BankDepositorRole d) {
		animationPanel.addBankDepositor(d);
	}
	
	public void addBankTeller(BankTellerRole t) {
		animationPanel.addBankTeller(t);
	}*/
	
	public void readConfig() {
		inputPanel.readConfig();
	}
}