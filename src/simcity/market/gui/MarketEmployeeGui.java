package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.ImageIcon;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.market.MarketEmployeeRole;

public class MarketEmployeeGui implements Gui {
	private MarketEmployeeRole role = null;

	private int xPos = -20, yPos = 20;//default Employee position
	private int xDestination = -20, yDestination = 20;//default Employee destination
	private int xHome = 380, yHome = 20; // Employee home position
	private List<ItemOrder> items;
	private Queue<Point> destinations = new LinkedList<Point>();
	private static Map<String, Point> foodLocations = new HashMap<String, Point>();
	static {
		foodLocations.put("Car", new Point(340, 20));
		foodLocations.put("Salad", new Point(340, 40));
		foodLocations.put("Steak", new Point(340, 80));
		foodLocations.put("Pizza", new Point(340, 100));
		foodLocations.put("Chicken", new Point(340, 140));
		foodLocations.put("Spaghetti", new Point(340, 160));
		foodLocations.put("Lasagna", new Point(340, 200));
		foodLocations.put("Garlic Bread", new Point(340, 220));
		foodLocations.put("Ribs", new Point(340, 260));
		foodLocations.put("Burger", new Point(340, 280));
		foodLocations.put("Enchiladas", new Point(340, 320));
		foodLocations.put("Tacos", new Point(340, 340));
		foodLocations.put("Pozole", new Point(340, 380));
		foodLocations.put("Horchata", new Point(340, 400));
	}
	public enum GuiState {nothing, gathering, cashier, leaving};
	GuiState gS = GuiState.nothing;
	
	Image employeeImage;
	List<ImageIcon> upAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> rightAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> downAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> leftAnimation = new ArrayList<ImageIcon>();
	
	public MarketEmployeeGui(MarketEmployeeRole r) {
		this.role = r;

		ImageIcon normal_maleU1 = new ImageIcon(this.getClass().getResource("images/market_employee/market_employee_male_u1.png"));
		ImageIcon normal_maleU2 = new ImageIcon(this.getClass().getResource("images/market_employee/market_employee_male_u2.png"));
		ImageIcon normal_maleR1 = new ImageIcon(this.getClass().getResource("images/market_employee/market_employee_male_r1.png"));
		ImageIcon normal_maleR2 = new ImageIcon(this.getClass().getResource("images/market_employee/market_employee_male_r2.png"));
		ImageIcon normal_maleD1 = new ImageIcon(this.getClass().getResource("images/market_employee/market_employee_male_d1.png"));
		ImageIcon normal_maleD2 = new ImageIcon(this.getClass().getResource("images/market_employee/market_employee_male_d2.png"));
		ImageIcon normal_maleL1 = new ImageIcon(this.getClass().getResource("images/market_employee/market_employee_male_l1.png"));
		ImageIcon normal_maleL2 = new ImageIcon(this.getClass().getResource("images/market_employee/market_employee_male_l2.png"));
		upAnimation.add(normal_maleU1);
		upAnimation.add(normal_maleU2);
		rightAnimation.add(normal_maleR1);
		rightAnimation.add(normal_maleR2);
		downAnimation.add(normal_maleD1);
		downAnimation.add(normal_maleD2);
		leftAnimation.add(normal_maleL1);
		leftAnimation.add(normal_maleL2);
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos+=20;
			employeeImage = rightAnimation.get(0).getImage();
			Collections.rotate(rightAnimation, 1);
		}
		else if (xPos > xDestination) {
			xPos-=20;
			employeeImage = leftAnimation.get(0).getImage();
			Collections.rotate(leftAnimation, 1);
		}

		else if (yPos < yDestination) {
			yPos+=20;
			employeeImage = downAnimation.get(0).getImage();
			Collections.rotate(downAnimation, 1);
		}
		else if (yPos > yDestination) {
			yPos-=20;
			employeeImage = upAnimation.get(0).getImage();
			Collections.rotate(upAnimation, 1);
		}
		
		if(xPos == xDestination && yPos == yDestination) {
			if(xDestination == xHome && yDestination == yHome) {
				employeeImage = leftAnimation.get(0).getImage();
			}
			if(gS == GuiState.gathering) {
				destinations.poll();
				if(destinations.size() != 0) {
					xDestination = destinations.peek().x;
					yDestination = destinations.peek().y;
				}
				else {
					role.msgHaveItems();
					gS = GuiState.nothing;
				}
			}
			if(gS == GuiState.cashier) {
				role.msgAtCashier();
				xDestination = xHome;
				yDestination = yHome;
				gS = GuiState.nothing;
			}
			if(gS == GuiState.leaving) {
				role.left();
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.drawImage(employeeImage, xPos, yPos, null);
	}

	public boolean isPresent() {
		return true;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
	
	/* Role Functions */
	public void work() {
		xDestination = xHome;
		yDestination = yHome;
	}
	public void GetItems(List<ItemOrder> i) {
		items = i;
		for(ItemOrder iO: i) {
			destinations.add(foodLocations.get(iO.getFoodItem()));
		}
		gS = GuiState.gathering;
		xDestination = destinations.peek().x;
		yDestination = destinations.peek().y;
	}
	public void GoToCashier() {
		xDestination = 140;
		yDestination = 20;
		gS = GuiState.cashier;
	}
	public void leave() {
		xDestination = -20;
		yDestination = 20;
		gS = GuiState.leaving;
	}
}