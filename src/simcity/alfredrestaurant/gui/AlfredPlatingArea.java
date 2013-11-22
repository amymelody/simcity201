package simcity.alfredrestaurant.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ConcurrentModificationException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simcity.alfredrestaurant.AlfredCustomerRole;
import simcity.alfredrestaurant.AlfredWaiterRole;

public class AlfredPlatingArea extends JPanel{
	
	AlfredRestaurantGUI restaurantGui;
	
	public AlfredPlatingArea(AlfredRestaurantGUI restaurantGui){
		this.restaurantGui = restaurantGui;
		setBorder(BorderFactory.createTitledBorder("plating area"));
	}	

	public void paintComponent(Graphics g) {
		g.clearRect(0 , 0, getWidth(), getHeight());
		
		//draw ready table
//		g.setColor(Color.PINK);
//		g.fillRect(getWidth() - 15, 40, 10, 70);
		
		Graphics2D g2 = (Graphics2D) g;
		int xPos = 10;
		int yPos = 30;
		try{
			for (AlfredWaiterRole waiterAgent: restaurantGui.controlRestaurantPanel.host.getWaiters()){
				if (waiterAgent.isWaitingCooking()){
					g.drawImage(waiterAgent.getWaiterGui().getImage(), xPos, yPos, null);
					g.setColor(Color.BLACK);
					g.drawString("(" + waiterAgent.getIndex() + ")", xPos, yPos + 10);
					xPos += 40;
					if (xPos > 150){
						xPos = 10;
						yPos += 40;
					}
				}
			}
		}catch(ConcurrentModificationException e){
			
		}	
	}
}
