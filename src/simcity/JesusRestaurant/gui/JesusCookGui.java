package simcity.JesusRestaurant.gui;


import simcity.cherysrestaurant.CookAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

public class JesusCookGui implements JesusGui {

	private CookAgent agent = null;

	private int xPos = 400, yPos = 400;//default cook position
	private int xDestination = 400, yDestination = 400;//default cook destination
	private int xHome = 400, yHome = 400;//cook home position

	Image cookImage;
	static Image pizza, steak, salad;
	Image fImage = pizza;
	public List<fImage> foods = Collections.synchronizedList(new ArrayList<fImage>());
	public enum ImageState {none, goingToCook, cooking, goingToPlate, plating, done};
	int cookxloc = 16, cookyloc = 21;
	int cookwxloc = 17, cookwyloc = 20;
	int platewxloc = 19, platewyloc = 16;
	int platexloc = 19, plateyloc = 16;
	public static final Map<String, Image> foodImages = new HashMap<String, Image>();
	
	public JesusCookGui(CookAgent agent) {
		this.agent = agent;

		ImageIcon cookIcon = new ImageIcon(this.getClass().getResource("images/luigi.png"));
		cookImage = cookIcon.getImage();
		
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
		
		if(xDestination == xPos && yDestination == yPos && xPos == cookwxloc*20 && yPos == cookwyloc*20) {
			synchronized(foods) {
				for(fImage fI: foods) {
					if(fI.s == ImageState.goingToCook) {
						fI.p.x = cookxloc*20;
						fI.p.y = cookyloc*20;
						fI.s = ImageState.cooking;
					}
				}
			}
		}
		
		if(xDestination == xPos && yDestination == yPos && xPos == platewxloc*20 && yPos == platewyloc*20) {
			xDestination = xHome;
			yDestination = yHome;
			synchronized(foods) {
				for(fImage fI: foods) {
					if(fI.s == ImageState.goingToPlate) {
						fI.p.x = platexloc*20;
						fI.p.y = plateyloc*20;
						fI.s = ImageState.plating;
					}
				}
			}
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(cookImage, xPos, yPos, null);
		synchronized(foods){
			for(fImage fI: foods) {
				g.drawImage(fI.image, fI.p.x, fI.p.y, null);
			}
		}
	}

	public boolean isPresent() {
		return true;
	}

	public void DoCookFood(String foodItem) {
		foods.add(new fImage(foodItem, foodImages.get(foodItem)));
		xDestination = cookwxloc*20;
		yDestination = cookwyloc*20;
	}
	
	public void DoPlateFood(String foodItem) {
		xDestination = platewxloc*20;
		yDestination = platewyloc*20;
		synchronized(foods) {
			for(fImage fI: foods) {
				if(fI.s == ImageState.cooking) {
					fI.p.x = -40;
					fI.p.y = -40;
					fI.s = ImageState.goingToPlate;
				}
			}
		}
	}
	
	public void GotFood(String foodItem) {
		synchronized(foods) {
			for(fImage fI: foods) {
				if(fI.s == ImageState.plating) {
					fI.p.x = -40;
					fI.p.y = -40;
					fI.s = ImageState.done;
					break;
				}
			}
		}
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
	public void checkInventory() {
		agent.msgCheckInventory();
	}

	public void updateInventory(Integer stI, Integer sI, Integer pI) {
		agent.updateInventory(stI, sI, pI);
	}
	
	public class fImage {
		String choice;
		Image image;
		Point p;
		ImageState s;
		
		public fImage(String c, Image im) {
			choice = c;
			image = im;
			p = new Point(-40,-40);
			s = ImageState.goingToCook;
		}
	}
}
