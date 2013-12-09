package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import simcity.ItemOrder;
import simcity.gui.DelivererGui;
import simcity.gui.Gui;
import simcity.market.MarketDelivererRole;
import simcity.market.gui.MarketEmployeeGui.GuiState;

public class MarketDelivererGui implements Gui {
	private MarketDelivererRole role = null;
	private String location;

	private int xPos = -20, yPos = 20;//default Deliverer position
	private int xDestination = -20, yDestination = 20;//default Deliverer destination
	private int xHome = 400, yHome = 20; // Deliverer home position
	
	public enum GuiState {nothing, delivering, cashier, leaving}
	public GuiState gS = GuiState.nothing;
	
	Image delivererImage;
	List<ImageIcon> upAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> rightAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> downAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> leftAnimation = new ArrayList<ImageIcon>();
	
	DelivererGui dG;
	
	public MarketDelivererGui(MarketDelivererRole r) {
		this.role = r;

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
	public void setDelivererGui(DelivererGui g) {
		dG = g;
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos+=20;
			delivererImage = rightAnimation.get(0).getImage();
			Collections.rotate(rightAnimation, 1);
		}
		else if (xPos > xDestination) {
			xPos-=20;
			delivererImage = leftAnimation.get(0).getImage();
			Collections.rotate(leftAnimation, 1);
		}

		else if (yPos < yDestination) {
			yPos+=20;
			delivererImage = downAnimation.get(0).getImage();
			Collections.rotate(downAnimation, 1);
		}
		else if (yPos > yDestination) {
			yPos-=20;
			delivererImage = upAnimation.get(0).getImage();
			Collections.rotate(upAnimation, 1);
		}
		
		if(xPos == xDestination && yPos == yDestination) {
			if(xDestination == xHome && yDestination == yHome) {
				delivererImage = rightAnimation.get(0).getImage();
			}
			if(gS == GuiState.delivering) {
				dG.deliver(location);
				gS = GuiState.nothing;
			}
			if(gS == GuiState.cashier) {
				role.msgArrivedBack();
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
	public void work() {
		xDestination = xHome;
		yDestination = yHome;
	}
	public void Deliver(String l) {
		location = l;
		xDestination = 520;
		yDestination = 20;
		gS = GuiState.delivering;
	}
	public void Outside() {
		role.msgArrived();
	}
	public void GoToCashier() {
		dG.goBack();
	}
	public void Inside() {
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