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
import simcity.anjalirestaurant.gui.AnjaliRestaurantGui;
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
	private final int BUFFERTOP = 20;
	private final int BUFFERSIDE = 680;

	/**
	 * Constructor
	 */
	public BuildingsGui(CityDirectory cd, CityGui cG)
	{
		cityGui = cG;
		
		setBounds(BUFFERSIDE, BUFFERTOP, WINDOWX, WINDOWY);
		BorderLayout frameLayout = new BorderLayout();
		setLayout(frameLayout);

		/* Creation of Buildings */
		
		// Markets
		MarketGui market1 = new MarketGui("market1", this, cd);
		cityGui.animationPanel.addBuilding(market1);
		MarketGui market2 = new MarketGui("market2", this, cd);
		cityGui.animationPanel.addBuilding(market2);
		
		// Banks
		BankGui bank1 = new BankGui("bank1", this, cd);
		cityGui.animationPanel.addBuilding(bank1);
		BankGui bank2 = new BankGui("bank2", this, cd);
		cityGui.animationPanel.addBuilding(bank2);
		
		// Houses
		HousingGui house1 = new HousingGui("house1", this, cd);
		cityGui.animationPanel.addBuilding(house1);
		HousingGui house2 = new HousingGui("house2", this, cd);
		cityGui.animationPanel.addBuilding(house2);
		HousingGui house3 = new HousingGui("house3", this, cd);
		cityGui.animationPanel.addBuilding(house3);
		HousingGui house4 = new HousingGui("house4", this, cd);
		cityGui.animationPanel.addBuilding(house4);
		
		// Apartment1
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
		HousingGui apartment18 = new HousingGui("apartment18", this, cd);
		cityGui.animationPanel.addBuilding(apartment18);
		HousingGui apartment19 = new HousingGui("apartment19", this, cd);
		cityGui.animationPanel.addBuilding(apartment19);
		HousingGui apartment110 = new HousingGui("apartment110", this, cd);
		cityGui.animationPanel.addBuilding(apartment110);
		HousingGui apartment111 = new HousingGui("apartment111", this, cd);
		cityGui.animationPanel.addBuilding(apartment111);
		HousingGui apartment112 = new HousingGui("apartment112", this, cd);
		cityGui.animationPanel.addBuilding(apartment112);
		HousingGui apartment113 = new HousingGui("apartment113", this, cd);
		cityGui.animationPanel.addBuilding(apartment113);
		
		// Apartment2
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
		HousingGui apartment28 = new HousingGui("apartment28", this, cd);
		cityGui.animationPanel.addBuilding(apartment28);
		HousingGui apartment29 = new HousingGui("apartment29", this, cd);
		cityGui.animationPanel.addBuilding(apartment29);
		HousingGui apartment210 = new HousingGui("apartment210", this, cd);
		cityGui.animationPanel.addBuilding(apartment210);
		HousingGui apartment211 = new HousingGui("apartment211", this, cd);
		cityGui.animationPanel.addBuilding(apartment211);
		HousingGui apartment212 = new HousingGui("apartment212", this, cd);
		cityGui.animationPanel.addBuilding(apartment212);
		HousingGui apartment213 = new HousingGui("apartment213", this, cd);
		cityGui.animationPanel.addBuilding(apartment213);
		
		// Apartment3
		HousingGui apartment30 = new HousingGui("apartment30", this, cd);
		cityGui.animationPanel.addBuilding(apartment30);
		HousingGui apartment31 = new HousingGui("apartment31", this, cd);
		cityGui.animationPanel.addBuilding(apartment31);
		HousingGui apartment32 = new HousingGui("apartment32", this, cd);
		cityGui.animationPanel.addBuilding(apartment32);
		HousingGui apartment33 = new HousingGui("apartment33", this, cd);
		cityGui.animationPanel.addBuilding(apartment33);
		HousingGui apartment34 = new HousingGui("apartment34", this, cd);
		cityGui.animationPanel.addBuilding(apartment34);
		HousingGui apartment35 = new HousingGui("apartment35", this, cd);
		cityGui.animationPanel.addBuilding(apartment35);
		HousingGui apartment36 = new HousingGui("apartment36", this, cd);
		cityGui.animationPanel.addBuilding(apartment36);
		HousingGui apartment37 = new HousingGui("apartment37", this, cd);
		cityGui.animationPanel.addBuilding(apartment37);
		HousingGui apartment38 = new HousingGui("apartment38", this, cd);
		cityGui.animationPanel.addBuilding(apartment38);
		HousingGui apartment39 = new HousingGui("apartment39", this, cd);
		cityGui.animationPanel.addBuilding(apartment39);
		HousingGui apartment310 = new HousingGui("apartment310", this, cd);
		cityGui.animationPanel.addBuilding(apartment310);
		HousingGui apartment311 = new HousingGui("apartment311", this, cd);
		cityGui.animationPanel.addBuilding(apartment311);
		HousingGui apartment312 = new HousingGui("apartment312", this, cd);
		cityGui.animationPanel.addBuilding(apartment312);
		HousingGui apartment313 = new HousingGui("apartment313", this, cd);
		cityGui.animationPanel.addBuilding(apartment313);
		
		// Apartment4
		HousingGui apartment40 = new HousingGui("apartment40", this, cd);
		cityGui.animationPanel.addBuilding(apartment40);
		HousingGui apartment41 = new HousingGui("apartment41", this, cd);
		cityGui.animationPanel.addBuilding(apartment41);
		HousingGui apartment42 = new HousingGui("apartment42", this, cd);
		cityGui.animationPanel.addBuilding(apartment42);
		HousingGui apartment43 = new HousingGui("apartment43", this, cd);
		cityGui.animationPanel.addBuilding(apartment43);
		HousingGui apartment44 = new HousingGui("apartment44", this, cd);
		cityGui.animationPanel.addBuilding(apartment44);
		HousingGui apartment45 = new HousingGui("apartment45", this, cd);
		cityGui.animationPanel.addBuilding(apartment45);
		HousingGui apartment46 = new HousingGui("apartment46", this, cd);
		cityGui.animationPanel.addBuilding(apartment46);
		HousingGui apartment47 = new HousingGui("apartment47", this, cd);
		cityGui.animationPanel.addBuilding(apartment47);
		HousingGui apartment48 = new HousingGui("apartment48", this, cd);
		cityGui.animationPanel.addBuilding(apartment48);
		HousingGui apartment49 = new HousingGui("apartment49", this, cd);
		cityGui.animationPanel.addBuilding(apartment49);
		HousingGui apartment410 = new HousingGui("apartment410", this, cd);
		cityGui.animationPanel.addBuilding(apartment410);
		HousingGui apartment411 = new HousingGui("apartment411", this, cd);
		cityGui.animationPanel.addBuilding(apartment411);
		HousingGui apartment412 = new HousingGui("apartment412", this, cd);
		cityGui.animationPanel.addBuilding(apartment412);
		HousingGui apartment413 = new HousingGui("apartment413", this, cd);
		cityGui.animationPanel.addBuilding(apartment413);
		
		// Restaurants
		JoshRestaurantGui joshRestaurant = new JoshRestaurantGui("joshRestaurant", this, cd);
		cityGui.animationPanel.addBuilding(joshRestaurant);
//		CherysRestaurantGui cherysRestaurant = new CherysRestaurantGui("cherysRestaurant", this, cd);
//		cityGui.animationPanel.addBuilding(cherysRestaurant);
		AnjaliRestaurantGui anjaliRestaurant = new AnjaliRestaurantGui("anjaliRestaurant", this, cd);
		cityGui.animationPanel.addBuilding(anjaliRestaurant);
//		JesusRestaurantGui jesusRestaurant = new JesusRestaurantGui("jesusRestaurant", this, cd);
//		cityGui.animationPanel.addBuilding(jesusRestaurant);

	}

	public void addDelivererGui(DelivererGui dG) {
		cityGui.animationPanel.addGui(dG);
	}

}