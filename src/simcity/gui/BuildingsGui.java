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
public class BuildingsGui extends JFrame
{
	private CityGui cityGui;
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 700;

	/**
	 * Constructor
	 */
	public BuildingsGui(CityDirectory cd, CityGui cG)
	{
		cityGui = cG;
		
		setBounds(BUFFERSIDE, BUFFERTOP, WINDOWX, WINDOWY);

		// creation of buildings
		MarketGui market1 = new MarketGui("Market 1", this, cd);
		cityGui.animationPanel.addBuilding(market1);
		MarketGui market2 = new MarketGui("Market 2", this, cd);
		cityGui.animationPanel.addBuilding(market2);
		MarketGui market3 = new MarketGui("Market 3", this, cd);
		cityGui.animationPanel.addBuilding(market3);
		BankGui bank = new BankGui("Bank", this, cd);
		cityGui.animationPanel.addBuilding(bank);
		/*HousingGui house1 = new HousingGui("House 1", this, cd);
		cityGui.animationPanel.addBuilding(house1);
		HousingGui house2 = new HousingGui("House 2", this, cd);
		cityGui.animationPanel.addBuilding(house2);
		HousingGui house3 = new HousingGui("House 3", this, cd);
		cityGui.animationPanel.addBuilding(house3);
		HousingGui apartment11 = new HousingGui("Apartment 1-1", this, cd);
		cityGui.animationPanel.addBuilding(apartment11);
		HousingGui apartment12 = new HousingGui("Apartment 1-2", this, cd);
		cityGui.animationPanel.addBuilding(apartment12);
		HousingGui apartment13 = new HousingGui("Apartment 1-3", this, cd);
		cityGui.animationPanel.addBuilding(apartment13);
		HousingGui apartment14 = new HousingGui("Apartment 1-4", this, cd);
		cityGui.animationPanel.addBuilding(apartment14);
		HousingGui apartment15 = new HousingGui("Apartment 1-5", this, cd);
		cityGui.animationPanel.addBuilding(apartment15);
		HousingGui apartment16 = new HousingGui("Apartment 1-6", this, cd);
		cityGui.animationPanel.addBuilding(apartment16);
		HousingGui apartment17 = new HousingGui("Apartment 1-7", this, cd);
		cityGui.animationPanel.addBuilding(apartment17);
		HousingGui apartment18 = new HousingGui("Apartment 1-8", this, cd);
		cityGui.animationPanel.addBuilding(apartment18);
		HousingGui apartment21 = new HousingGui("Apartment 2-1", this, cd);
		cityGui.animationPanel.addBuilding(apartment21);
		HousingGui apartment22 = new HousingGui("Apartment 2-2", this, cd);
		cityGui.animationPanel.addBuilding(apartment22);
		HousingGui apartment23 = new HousingGui("Apartment 2-3", this, cd);
		cityGui.animationPanel.addBuilding(apartment23);
		HousingGui apartment24 = new HousingGui("Apartment 2-4", this, cd);
		cityGui.animationPanel.addBuilding(apartment24);
		HousingGui apartment25 = new HousingGui("Apartment 2-5", this, cd);
		cityGui.animationPanel.addBuilding(apartment25);
		HousingGui apartment26 = new HousingGui("Apartment 2-6", this, cd);
		cityGui.animationPanel.addBuilding(apartment26);
		HousingGui apartment27 = new HousingGui("Apartment 2-7", this, cd);
		cityGui.animationPanel.addBuilding(apartment27);
		HousingGui apartment28 = new HousingGui("Apartment 2-8", this, cd);
		cityGui.animationPanel.addBuilding(apartment28);*/
		JoshRestaurantGui joshRestaurant = new JoshRestaurantGui("Josh's Restaurant", this, cd);
		cityGui.animationPanel.addBuilding(joshRestaurant);
		/*CherysRestaurantGui cherysRestaurant = new CherysRestaurantGui("Cherys's Restaurant", this, cd);
		cityGui.animationPanel.addBuilding(cherysRestaurant);
		AnjaliRestaurantGui anjaliRestaurant = new AnjaliRestaurantGui("Anjali's Restaurant", this, cd);
		cityGui.animationPanel.addBuilding(anjaliRestaurant);
		AlfredRestaurantGui alfredRestaurant = new AlfredRestaurantGui("Alfred's Restaurant", this, cd);
		cityGui.animationPanel.addBuilding(alfredRestaurant);
		JesusRestaurantGui jesusRestaurant = new JesusRestaurantGui("Jesus's Restaurant", this, cd);
		cityGui.animationPanel.addBuilding(jesusRestaurant);*/

	}

}