package simcity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import simcity.gui.Gui;
import simcity.market.gui.MarketDelivererGui;
import simcity.market.gui.MarketGui;

public class DelivererGui implements Gui {
	private MarketDelivererGui gui;
	private MarketGui mG;
	Point marketPnt, destPnt;
	
	private int xPos, yPos;//default Deliverer position
	private int xDestination, yDestination;//default Deliverer destination
	private int xHome, yHome; // Deliverer home poMap<K, V>ion
	private static Map<String, Point> locations = new HashMap<String, Point>();
	static {
		locations.put("market1", new Point(370, 280));
		locations.put("market2", new Point(370, 420));
		locations.put("joshRestaurant", new Point(280, 280));
		locations.put("cherysRestaurant", new Point(420, 280));
		locations.put("anjaliRestaurant", new Point(420, 420));
		locations.put("jesusRestaurant", new Point(280, 420));
	}
	
	public enum GuiState {nothing, delivering, cashier}
	public GuiState gS = GuiState.nothing;
	
	Image delivererImage;
	List<ImageIcon> upAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> rightAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> downAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> leftAnimation = new ArrayList<ImageIcon>();
	
	public DelivererGui(MarketDelivererGui g, String nameOfMarket) {
		this.gui = g;
	
		marketPnt = locations.get(nameOfMarket);
		xPos = 520;
		yPos = marketPnt.y;
		xDestination = xPos;
		yDestination = yPos;
		xHome = marketPnt.x;
		yHome = marketPnt.y;
		
		ImageIcon normal_maleU1 = new ImageIcon(this.getClass().getResource("images/market_deliverer/market_deliverer_male_u1.png"));
		ImageIcon normal_maleU2 = new ImageIcon(this.getClass().getResource("images/market_deliverer/market_deliverer_male_u2.png"));
		ImageIcon normal_maleR1 = new ImageIcon(this.getClass().getResource("images/market_deliverer/market_deliverer_male_r1.png"));
		ImageIcon normal_maleR2 = new ImageIcon(this.getClass().getResource("images/market_deliverer/market_deliverer_male_r2.png"));
		ImageIcon normal_maleD1 = new ImageIcon(this.getClass().getResource("images/market_deliverer/market_deliverer_male_d1.png"));
		ImageIcon normal_maleD2 = new ImageIcon(this.getClass().getResource("images/market_deliverer/market_deliverer_male_d2.png"));
		ImageIcon normal_maleL1 = new ImageIcon(this.getClass().getResource("images/market_deliverer/market_deliverer_male_l1.png"));
		ImageIcon normal_maleL2 = new ImageIcon(this.getClass().getResource("images/market_deliverer/market_deliverer_male_l2.png"));
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
			xPos+=10;
			delivererImage = rightAnimation.get(0).getImage();
			Collections.rotate(rightAnimation, 1);
		}
		else if (xPos > xDestination) {
			xPos-=10;
			delivererImage = leftAnimation.get(0).getImage();
			Collections.rotate(leftAnimation, 1);
		}

		else if (yPos < yDestination) {
			yPos+=10;
			delivererImage = downAnimation.get(0).getImage();
			Collections.rotate(downAnimation, 1);
		}
		else if (yPos > yDestination) {
			yPos-=10;
			delivererImage = upAnimation.get(0).getImage();
			Collections.rotate(upAnimation, 1);
		}
		if(xPos == xDestination && yPos == yDestination) {
			if(gS == GuiState.delivering) {
				gui.Outside();
				xPos = 520;
				xDestination=520;
				gS = GuiState.nothing;
			}
			if(gS == GuiState.cashier) {
				gui.Inside();
				xPos = 520;
				xDestination = 520;
				gS = GuiState.nothing;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.drawImage(delivererImage, xPos, yPos, null);
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
	public void deliver(String l) {
		xPos = marketPnt.x;
		yPos = marketPnt.y;
		destPnt = locations.get(l);
		xDestination = destPnt.x;
		yDestination = destPnt.y;
		gS = GuiState.delivering;
	}
	public void goBack() {
		xPos = destPnt.x;
		yPos = destPnt.y;
		xDestination = xHome;
		yDestination = yHome;
		gS = GuiState.cashier;
	}
}