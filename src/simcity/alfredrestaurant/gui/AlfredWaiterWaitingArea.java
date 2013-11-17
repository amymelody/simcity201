package restaurant.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ConcurrentModificationException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

public class WaiterWaitingArea extends JPanel{
	
	RestauranGUI restaurantGui;
	
	public WaiterWaitingArea(RestauranGUI restaurantGui){
		this.restaurantGui = restaurantGui;
		setBorder(BorderFactory.createTitledBorder("waiter waiting area"));
	}	

	public void paintComponent(Graphics g) {
		g.clearRect(0 , 0, getWidth(), getHeight());
		
		Graphics2D g2 = (Graphics2D) g;
		int xPos = 10;
		int yPos = 30;
		try{
			for (WaiterAgent waiterAgent: restaurantGui.controlRestaurantPanel.host.getWaiters()){
				if (waiterAgent.isWaiting()){
					g.drawImage(waiterAgent.getWaiterGui().getImage(), xPos, yPos, null);
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
