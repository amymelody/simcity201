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
		MarketGui market1 = new MarketGui("market1", this, cd);
		cityGui.animationPanel.addBuilding(market1);
		MarketGui market2 = new MarketGui("market2", this, cd);
		cityGui.animationPanel.addBuilding(market2);
		MarketGui market3 = new MarketGui("market3", this, cd);
		cityGui.animationPanel.addBuilding(market3);
		BankGui bank = new BankGui("bank1", this, cd);
		cityGui.animationPanel.addBuilding(bank);
		HousingGui house1 = new HousingGui("house1", this, cd);
		cityGui.animationPanel.addBuilding(house1);
		HousingGui house2 = new HousingGui("house2", this, cd);
		cityGui.animationPanel.addBuilding(house2);
		HousingGui house3 = new HousingGui("house3", this, cd);
		cityGui.animationPanel.addBuilding(house3);
		HousingGui apartment10 = new HousingGui("apartment10", this, cd);
		cityGui.animationPanel.addBuilding(apartment10);
		HousingGui apartment11 = new HousingGui("apartment11", this, cd);
		cityGui.animationPanel.addBuilding(apartment11);
		HousingGui apartment12 = new HousingGui("apartment12", this, cd);
		cityGui.animationPanel.addBuilding(apartment12);
		HousingGui apartment13 = new HousingGui("apartment13", this, cd);
		cityGui.animationPanel.addBuilding(apartment13);
		HousingGui apartment14 = new HousingGui("apartment14", this, cd);
		cityGui.animationPanel.addBuilding(apartment14);
		HousingGui apartment15 = new HousingGui("apartment15", this, cd);
		cityGui.animationPanel.addBuilding(apartment15);
		HousingGui apartment16 = new HousingGui("apartment16", this, cd);
		cityGui.animationPanel.addBuilding(apartment16);
		HousingGui apartment17 = new HousingGui("apartment17", this, cd);
		cityGui.animationPanel.addBuilding(apartment17);
		HousingGui apartment20 = new HousingGui("apartment20", this, cd);
		cityGui.animationPanel.addBuilding(apartment20);
		HousingGui apartment21 = new HousingGui("apartment21", this, cd);
		cityGui.animationPanel.addBuilding(apartment21);
		HousingGui apartment22 = new HousingGui("apartment22", this, cd);
		cityGui.animationPanel.addBuilding(apartment22);
		HousingGui apartment23 = new HousingGui("apartment23", this, cd);
		cityGui.animationPanel.addBuilding(apartment23);
		HousingGui apartment24 = new HousingGui("apartment24", this, cd);
		cityGui.animationPanel.addBuilding(apartment24);
		HousingGui apartment25 = new HousingGui("apartment25", this, cd);
		cityGui.animationPanel.addBuilding(apartment25);
		HousingGui apartment26 = new HousingGui("apartment26", this, cd);
		cityGui.animationPanel.addBuilding(apartment26);
		HousingGui apartment27 = new HousingGui("apartment27", this, cd);
		cityGui.animationPanel.addBuilding(apartment27);
		JoshRestaurantGui joshRestaurant = new JoshRestaurantGui("joshRestaurant", this, cd);
		cityGui.animationPanel.addBuilding(joshRestaurant);
		/*CherysRestaurantGui cherysRestaurant = new CherysRestaurantGui("cherysRestaurant", this, cd);
		cityGui.animationPanel.addBuilding(cherysRestaurant);
		AnjaliRestaurantGui anjaliRestaurant = new AnjaliRestaurantGui("anjaliRestaurant", this, cd);
		cityGui.animationPanel.addBuilding(anjaliRestaurant);
		JesusRestaurantGui jesusRestaurant = new JesusRestaurantGui("jesusRestaurant", this, cd);
		cityGui.animationPanel.addBuilding(jesusRestaurant);*/

	}

}