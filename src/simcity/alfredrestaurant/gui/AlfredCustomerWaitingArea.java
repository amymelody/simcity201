package simcity.alfredrestaurant.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simcity.alfredrestaurant.AlfredCustomerRole;

public class AlfredCustomerWaitingArea extends JPanel{
	
	AlfredRestaurantGUI restaurantGui;
	
	public AlfredCustomerWaitingArea(AlfredRestaurantGUI restaurantGui){
		this.restaurantGui = restaurantGui;
		setBorder(BorderFactory.createTitledBorder("customer waiting area"));
	}	

	public void paintComponent(Graphics g) {
		g.clearRect(0 , 0, getWidth(), getHeight());
		
		Graphics2D g2 = (Graphics2D) g;
		int xPos = 10;
		int yPos = 30;
		synchronized (restaurantGui.controlRestaurantPanel.getRestPanel().getCustomers()) {
			for (AlfredCustomerRole agent: restaurantGui.controlRestaurantPanel.getRestPanel().getCustomers()){
				if (agent.isWaiting()){
					g.drawImage(agent.getGui().getImage(), xPos, yPos, null);
					xPos += 40;
					if (xPos > 210){
						xPos = 10;
						yPos += 40;
					}
				}
			}
			restaurantGui.controlRestaurantPanel.getRestPanel().getCustomers().notifyAll();
		}	
	}
}
