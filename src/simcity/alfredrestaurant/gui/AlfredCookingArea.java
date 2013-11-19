package restaurant.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import restaurant.CookAgent;
import restaurant.CustomerAgent;

public class CookingArea extends JPanel{
	
	RestauranGUI restaurantGui;
	CherysCookRole cookAgent;
	CookerGui cookerGui;
	
	public static int xRefrigeratorPos;
	public static int yRefrigeratorPos;
	public static int xCookingPos;
	public static int yCookingPos;
	public static int xPlatingPos;
	public static int yPlatingPos;
	
	public CookingArea(RestauranGUI restaurantGui){
		this.restaurantGui = restaurantGui;
		setBorder(BorderFactory.createTitledBorder("cooking area"));
		cookAgent = restaurantGui.controlRestaurantPanel.host.getCook();
		cookerGui = restaurantGui.controlRestaurantPanel.getRestPanel().getCookerGui();
		cookerGui.setAgent(cookAgent);
		cookAgent.setCookGui(cookerGui);
	}	
	
	public void paintComponent(Graphics g) {
		xRefrigeratorPos = getWidth() - 40;
		yRefrigeratorPos = 30;
		xCookingPos = getWidth() - 90;
		yCookingPos = getHeight() - 70;
		xPlatingPos = 10;
		yPlatingPos = getHeight()/2 - 20;
		
		Graphics2D g2 = (Graphics2D) g;
		g.clearRect(0 , 0, getWidth(), getHeight());
		
		
		//draw refrigerator
		g.setColor(Color.BLUE);
		g.fillRect(getWidth() - 15, 40, 10, 20);
		g.setColor(Color.BLACK);
		
		//draw cooking area to cook food
		g.setColor(Color.GREEN);
		g.fillRect(getWidth() - 90, getHeight() - 30, 70, 10);
		g.setColor(Color.BLACK);
		
		cookerGui.updatePosition();
		cookerGui.draw(g2);
	}
}
