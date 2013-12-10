package simcity.jesusrestaurant.gui;

import simcity.gui.Gui;
import simcity.jesusrestaurant.JesusCustomerRole;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public class JesusCustomerGui implements Gui {

	Timer timer = new Timer();
	
	private JesusCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	JesusRestaurantGui gui;

	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(40, 160));
		tableLocations.put(2, new Point(200, 160));
		tableLocations.put(3, new Point(40, 320));
		tableLocations.put(4, new Point(200, 320));
	}
	
	static Image mPizza, mSteak, mSalad;
	static Image pizza, steak, salad;
	Image bImage = mPizza;
	Image fImage = pizza;
	int bx = -40;
	int by = -40;
	int fx = -40;
	int fy = -40;
	int cashierxloc = 0, cashieryloc = 4;
	
	public static final Map<String, Image> orderBubbles = new HashMap<String, Image>();
	public static final Map<String, Image> foodImages = new HashMap<String, Image>();
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	private int xHome, yHome;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	Image customerImage;
	
	public JesusCustomerGui(JesusCustomerRole c, JesusRestaurantGui gui, int xW, int yW){
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		xHome = xW;
		yHome = yW;
		//maitreD = m;
		this.gui = gui;
		
		ImageIcon customerIcon = new ImageIcon(this.getClass().getResource("images/goomba.png"));
		customerImage = customerIcon.getImage();
		ImageIcon mPizzaIcon = new ImageIcon(this.getClass().getResource("images/mPizza.png"));
		mPizza = mPizzaIcon.getImage();
		ImageIcon mSteakIcon = new ImageIcon(this.getClass().getResource("images/mSteak.png"));
		mSteak = mSteakIcon.getImage();
		ImageIcon mSaladIcon = new ImageIcon(this.getClass().getResource("images/mSalad.png"));
		mSalad = mSaladIcon.getImage();
		
		orderBubbles.put("Steak", mSteak);
		orderBubbles.put("Salad", mSalad);
		orderBubbles.put("Pizza", mPizza);

		ImageIcon pizzaIcon = new ImageIcon(this.getClass().getResource("images/pizza.png"));
		pizza = pizzaIcon.getImage();
		ImageIcon steakIcon = new ImageIcon(this.getClass().getResource("images/steak.png"));
		steak = steakIcon.getImage();
		ImageIcon saladIcon = new ImageIcon(this.getClass().getResource("images/salad.png"));
		salad = saladIcon.getImage();
		
		foodImages.put("Steak", steak);
		foodImages.put("Salad", salad);
		foodImages.put("Pizza", pizza);
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
			}
			else if(xPos == cashierxloc && yPos == cashieryloc) {
				agent.msgAnimationFinishedGoToCashier();
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(customerImage, xPos, yPos, null);
		g.drawImage(bImage, bx, by, null);
		g.drawImage(fImage, fx, fy, null);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
		xDestination = xHome;
		yDestination = yHome;
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void vait() {
		xDestination = xHome;
		yDestination = yHome;
	}
	
	public void DoGoToSeat(int seatnumber) {
		xDestination = (tableLocations.get(seatnumber).x);
		yDestination = (tableLocations.get(seatnumber).y-20);
	}

	public void DoOrder(int tableN, String foodChoice) {
		bImage = orderBubbles.get(foodChoice);
		bx = tableLocations.get(tableN).x - 30;
		by = tableLocations.get(tableN).y - 30;
	}
	
	public void DoReceivedOrder(int tableN, String foodChoice) {
		bx = -40;
		by = -40;
		fImage = foodImages.get(foodChoice);
		fx = tableLocations.get(tableN).x;
		fy = tableLocations.get(tableN).y;
	}
	
	public void DoGoToCashier() {
		xDestination = cashierxloc;
		yDestination = cashieryloc;
		fx = -40;
		fy = -40;
	}
	
	public void DoExitRestaurant() {
		bx = -40;
		by = -40;
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}

}
