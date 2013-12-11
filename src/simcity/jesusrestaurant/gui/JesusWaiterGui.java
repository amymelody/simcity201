package simcity.jesusrestaurant.gui;

import simcity.gui.Gui;
import simcity.jesusrestaurant.JesusNormalWaiterRole;
import simcity.jesusrestaurant.interfaces.JesusCustomer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class JesusWaiterGui implements Gui {

	private JesusNormalWaiterRole role = null;

	private int xPos = -20, yPos = 20;//default waiter position
	private int xDestination, yDestination;//default waiter destination
	private int xHome, yHome;//waiter home position
	private int xCook = 380, yCook = 280;
	private int xBreak = 460, yBreak = 0;
	
	boolean onBreak = false;

	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(40, 160));
		tableLocations.put(2, new Point(200, 160));
		tableLocations.put(3, new Point(40, 320));
		tableLocations.put(4, new Point(200, 320));
	}
	
	static Image pizza, steak, salad;
	
	Image fImage = pizza;
	int fx = -40;
	int fy = -40;

	public static final Map<String, Image> foodImages = new HashMap<String, Image>();
	
	Image waiterImage;
	
	JesusRestaurantGui gui;

	public enum state {none, seating, ordering, sending, pickUp, handing, clearing, goingBack, customer};
	state gS = state.none;
	boolean breakDeciding = false;

	boolean leave;
	
	public JesusWaiterGui(JesusNormalWaiterRole role, JesusRestaurantGui g, int xh, int yh) {
		this.role = role;
		gui = g;
		
		xHome = xh;
		yHome = yh;
		xDestination = -20;
		yDestination = 20;
		
		ImageIcon waiterIcon = new ImageIcon(this.getClass().getResource("images/yoshi.png"));
		waiterImage = waiterIcon.getImage();
		
		ImageIcon pizzaIcon = new ImageIcon(this.getClass().getResource("images/pizza.png"));
		pizza = pizzaIcon.getImage();
		ImageIcon steakIcon = new ImageIcon(this.getClass().getResource("images/steak.png"));
		steak = steakIcon.getImage();
		ImageIcon saladIcon = new ImageIcon(this.getClass().getResource("images/salad.png"));
		salad = saladIcon.getImage();
		
		foodImages.put("Steak", steak);
		foodImages.put("Salad", salad);
		foodImages.put("Pizza", pizza);
		
		fx = -40;
		fy = -40;
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos+= 20;
			if(gS == state.handing)
				fx = xPos - 10;
		}
		else if (xPos > xDestination) {
			xPos-= 20;
			if(gS == state.handing)
				fx = xPos - 10;
		}
		else if (yPos < yDestination) {
			yPos+=20;
			if(gS == state.handing)
				fy = yPos - 10;
		}
		else if (yPos > yDestination) {
			yPos-=20;
			if(gS == state.handing)
				fy = yPos - 10;
		}
		
		for(Point p : tableLocations.values()) {
			if (xPos == xDestination && yPos == yDestination
					& xDestination == p.x + 20 && yDestination == p.y - 40) {
				if(gS == state.ordering) {
					role.msgFinishedGoForOrder();
				}
				else if(gS == state.handing) {
					fx = -40;
					fy = -40;
					role.msgFinishedGoWithPlate();
					DoLeaveCustomer();
					gS = state.none;
				}
				else if(gS == state.clearing) {
					role.msgFinishedClearing();
					DoLeaveCustomer();
					gS = state.none;
				}
				else {
					DoLeaveCustomer();
				}
			}
		}
		if(xPos == xHome && yPos == yHome) {
			if(gS == state.seating) {
				role.msgAtTable();
				xDestination = xHome;
				yDestination = yHome;
			}
		}
		if(xPos == xCook*20 && yPos == yCook*20) {
			if(gS == state.sending) {
				role.msgFinishedGoToCook();
				xDestination = xHome;
				yDestination = yHome;
			}
		}
		if(xPos == xCook*20 && yPos == yCook*20) {
			if(gS == state.pickUp) {
				role.msgFinishedPickUp();
			}
		}
		if(xPos == 120 && yPos == 40) {
			if(gS == state.customer) {
				role.msgAtVait();
			}
		}
		if(xDestination == xPos && yDestination == yPos && leave) {
			role.left();
		}
	}


	public void draw(Graphics2D g) {
		g.drawImage(waiterImage, xPos, yPos, null);
		g.drawImage(fImage, fx, fy, null);
	}

	public boolean isPresent() {
		return true;
	}

	public void leave() {
		xDestination = -20;
		yDestination = 20;
		leave = true;
	}
	public void work() {
		xDestination = xHome;
		yDestination = yHome;
	}
	public void breakDecision(boolean p) {
		breakDeciding = false;
		onBreak = p;
		if(p) {
			xDestination = xBreak;
			yDestination = yBreak;
		}
	}
	public void goOnBreak() {
		breakDeciding = true;
		if(onBreak) {
			role.msgReturnToWork();
			xDestination = xHome;
			yDestination = yHome;
		}
		else {
			role.msgGoOnBreak();
			
		}
	}
	public String getOnBreak() {
		if (onBreak) {
			return "On Break";
		}
		else {
			return "Working";
		}
	}
	public void DoGoToVait() {
		xDestination = 120;
		yDestination = 40;
		gS = state.customer;
	}
	public void DoBringToTable(JesusCustomer customer, int tableNumber) {
		xDestination = (tableLocations.get(tableNumber).x) + 20;
		yDestination = (tableLocations.get(tableNumber).y) - 40;
		gS = state.seating;
	}
	public void DoTakeOrder(JesusCustomer cust, int tableN) {
		xDestination = (tableLocations.get(tableN).x) + 20;
		yDestination = (tableLocations.get(tableN).y) - 40;
		gS = state.ordering;
	}
	public void DoGoToCook() {
		xDestination = xCook;
		yDestination = yCook;
		gS = state.sending;
	}
	public void DoGetPlate(String foodChoice) {
		xDestination = xCook;
		yDestination = yCook;
		fImage = foodImages.get(foodChoice);
		gS = state.pickUp;
	}
	public void DoHandPlate(int tableN, String foodChoice) {
		xDestination = tableLocations.get(tableN).x + 20;
		yDestination = tableLocations.get(tableN).y - 40;
		fx = xPos - 10;
		fy = yPos - 10;
		gS = state.handing;
	}
	public void DoWalkToClear(int tableN) {
		xDestination = tableLocations.get(tableN).x + 20;
		yDestination = tableLocations.get(tableN).y - 40;
		gS = state.clearing;
	}
	public void DoLeaveCustomer() {
		xDestination = xHome;
		yDestination = yHome;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}
