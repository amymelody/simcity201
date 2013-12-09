package simcity.market.gui;

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
import simcity.market.MarketCustomerRole;

public class MarketCustomerGui implements Gui {
	private MarketCustomerRole role = null;

	private int xPos = -20, yPos = 20;//default Customer position
	private int xDestination = -20, yDestination = 20;//default Customer destination
	private int xHome = 20, yHome = 20; // Customer home position

	Image customerImage;
	List<ImageIcon> upAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> rightAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> downAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> leftAnimation = new ArrayList<ImageIcon>();
	
	public enum GuiState {nothing, toCashier, toWaiting, pickingUp, exiting};
	GuiState gS = GuiState.nothing;
	
	public MarketCustomerGui(MarketCustomerRole r) {
		this.role = r;

		ImageIcon normal_maleU1 = new ImageIcon(this.getClass().getResource("images/market_customer/customer_male_u1.png"));
		ImageIcon normal_maleU2 = new ImageIcon(this.getClass().getResource("images/market_customer/customer_male_u2.png"));
		ImageIcon normal_maleR1 = new ImageIcon(this.getClass().getResource("images/market_customer/customer_male_r1.png"));
		ImageIcon normal_maleR2 = new ImageIcon(this.getClass().getResource("images/market_customer/customer_male_r2.png"));
		ImageIcon normal_maleD1 = new ImageIcon(this.getClass().getResource("images/market_customer/customer_male_d1.png"));
		ImageIcon normal_maleD2 = new ImageIcon(this.getClass().getResource("images/market_customer/customer_male_d2.png"));
		ImageIcon normal_maleL1 = new ImageIcon(this.getClass().getResource("images/market_customer/customer_male_l1.png"));
		ImageIcon normal_maleL2 = new ImageIcon(this.getClass().getResource("images/market_customer/customer_male_l2.png"));
		upAnimation.add(normal_maleU1);
		upAnimation.add(normal_maleU2);
		rightAnimation.add(normal_maleR1);
		rightAnimation.add(normal_maleR2);
		downAnimation.add(normal_maleD1);
		downAnimation.add(normal_maleD2);
		leftAnimation.add(normal_maleL1);
		leftAnimation.add(normal_maleL2);
		
		customerImage = downAnimation.get(0).getImage();
		Collections.rotate(downAnimation, 1);
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos+=20;
			customerImage = rightAnimation.get(0).getImage();
			Collections.rotate(rightAnimation, 1);
		}
		else if (xPos > xDestination) {
			xPos-=20;
			customerImage = leftAnimation.get(0).getImage();
			Collections.rotate(leftAnimation, 1);
		}

		if (yPos < yDestination) {
			yPos+=20;
			customerImage = downAnimation.get(0).getImage();
			Collections.rotate(downAnimation, 1);
		}
		else if (yPos > yDestination) {
			yPos-=20;
			customerImage = upAnimation.get(0).getImage();
			Collections.rotate(upAnimation, 1);
		}
		
		if(xPos == xDestination && yPos == yDestination) {
			if(xDestination == 80 && yDestination == 20) {
				if(gS == GuiState.toCashier) {
					role.msgAtCashier();
					gS = GuiState.nothing;
				}
				else if(gS == GuiState.pickingUp) {
					role.msgAtPickUp();
					gS = GuiState.nothing;
				}
			}
			else if(xDestination == -20 && yDestination == 20 && gS == GuiState.exiting) {
				role.msgOut();
				gS = GuiState.nothing;
			}
			else if(xDestination == xHome && yDestination == yHome) {
				if(xHome == 20) {
					customerImage = rightAnimation.get(0).getImage();
				}
				else if(xHome == 80) {
					customerImage = leftAnimation.get(0).getImage();
				}
				else {
					customerImage = upAnimation.get(0).getImage();
				}
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.drawImage(customerImage, xPos, yPos, null);
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
	public void GoToCashier() {
		xDestination = 80;
		yDestination = 20;
		gS = GuiState.toCashier;
		
	}
	public void GoToWaitingArea(int wX, int wY) {
		xDestination = wX;
		yDestination = wY;
		xHome = wX;
		yHome = wY;
		gS = GuiState.toWaiting;
	}
	public void PickUpItems() {
		xDestination = 80;
		yDestination = 20;
		gS = GuiState.pickingUp;
	}
	public void ExitMarket() {
		xDestination = -20;
		yDestination = 20;
		gS = GuiState.exiting;
	}
}