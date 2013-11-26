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

		// creation of buildings
		MarketGui market1 = new MarketGui("Market 1", this, cd);
		animationPanel.addBuilding(market1);
		MarketGui market2 = new MarketGui("Market 2", this, cd);
		animationPanel.addBuilding(market2);
		MarketGui market3 = new MarketGui("Market 3", this, cd);
		animationPanel.addBuilding(market3);
		BankGui bank = new BankGui("Bank", this, cd);
		animationPanel.addBuilding(bank);
		HousingGui house1 = new HousingGui("House 1", this, cd);
		animationPanel.addBuilding(house1);
		HousingGui house2 = new HousingGui("House 2", this, cd);
		animationPanel.addBuilding(house2);
		HousingGui house3 = new HousingGui("House 3", this, cd);
		animationPanel.addBuilding(house3);
		HousingGui apartment11 = new HousingGui("Apartment 1-1", this, cd);
		animationPanel.addBuilding(apartment11);
		HousingGui apartment12 = new HousingGui("Apartment 1-2", this, cd);
		animationPanel.addBuilding(apartment12);
		HousingGui apartment13 = new HousingGui("Apartment 1-3", this, cd);
		animationPanel.addBuilding(apartment13);
		HousingGui apartment14 = new HousingGui("Apartment 1-4", this, cd);
		animationPanel.addBuilding(apartment14);
		HousingGui apartment15 = new HousingGui("Apartment 1-5", this, cd);
		animationPanel.addBuilding(apartment15);
		HousingGui apartment16 = new HousingGui("Apartment 1-6", this, cd);
		animationPanel.addBuilding(apartment16);
		HousingGui apartment17 = new HousingGui("Apartment 1-7", this, cd);
		animationPanel.addBuilding(apartment17);
		HousingGui apartment18 = new HousingGui("Apartment 1-8", this, cd);
		animationPanel.addBuilding(apartment18);
		HousingGui apartment21 = new HousingGui("Apartment 2-1", this, cd);
		animationPanel.addBuilding(apartment21);
		HousingGui apartment22 = new HousingGui("Apartment 2-2", this, cd);
		animationPanel.addBuilding(apartment22);
		HousingGui apartment23 = new HousingGui("Apartment 2-3", this, cd);
		animationPanel.addBuilding(apartment23);
		HousingGui apartment24 = new HousingGui("Apartment 2-4", this, cd);
		animationPanel.addBuilding(apartment24);
		HousingGui apartment25 = new HousingGui("Apartment 2-5", this, cd);
		animationPanel.addBuilding(apartment25);
		HousingGui apartment26 = new HousingGui("Apartment 2-6", this, cd);
		animationPanel.addBuilding(apartment26);
		HousingGui apartment27 = new HousingGui("Apartment 2-7", this, cd);
		animationPanel.addBuilding(apartment27);
		HousingGui apartment28 = new HousingGui("Apartment 2-8", this, cd);
		animationPanel.addBuilding(apartment28);
		JoshRestaurantGui joshRestaurant = new JoshRestaurantGui("Josh's Restaurant", this, cd);
		animationPanel.addBuilding(joshRestaurant);
		//CherysRestaurantGui cherysRestaurant = new CherysRestaurantGui("Cherys's Restaurant", this, cd);
		//animationPanel.addBuilding(cherysRestaurant);
		//AnjaliRestaurantGui anjaliRestaurant = new AnjaliRestaurantGui("Anjali's Restaurant", this, cd);
		//animationPanel.addBuilding(anjaliRestaurant);
		//AlfredRestaurantGui alfredRestaurant = new AlfredRestaurantGui("Alfred's Restaurant", this, cd);
		//animationPanel.addBuilding(alfredRestaurant);
		//JesusRestaurantGui jesusRestaurant = new JesusRestaurantGui("Jesus's Restaurant", this, cd);
		//animationPanel.addBuilding(jesusRestaurant);

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
}