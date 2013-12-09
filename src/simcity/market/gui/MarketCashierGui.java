package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import simcity.gui.Gui;
import simcity.market.MarketCashierRole;

public class MarketCashierGui implements Gui {
	private MarketCashierRole role = null;

	private int xPos = -20, yPos = 20; // initial Cashier position
	private int xDestination = -20, yDestination = 20;// initial Cashier destination
	private int xHome = 120, yHome = 20; // Cashier's Desk
	private int xLeave = -20, yLeave = 20; // Cashier's Leaving
	boolean leaving = false;

	Image cashierImage;
	List<ImageIcon> upAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> rightAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> downAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> leftAnimation = new ArrayList<ImageIcon>();
	
	public MarketCashierGui(MarketCashierRole r) {
		this.role = r;

		ImageIcon normal_maleU1 = new ImageIcon(this.getClass().getResource("images/market_cashier/market_cashier_male_u1.png"));
		ImageIcon normal_maleU2 = new ImageIcon(this.getClass().getResource("images/market_cashier/market_cashier_male_u2.png"));
		ImageIcon normal_maleR1 = new ImageIcon(this.getClass().getResource("images/market_cashier/market_cashier_male_r1.png"));
		ImageIcon normal_maleR2 = new ImageIcon(this.getClass().getResource("images/market_cashier/market_cashier_male_r2.png"));
		ImageIcon normal_maleD1 = new ImageIcon(this.getClass().getResource("images/market_cashier/market_cashier_male_d1.png"));
		ImageIcon normal_maleD2 = new ImageIcon(this.getClass().getResource("images/market_cashier/market_cashier_male_d2.png"));
		ImageIcon normal_maleL1 = new ImageIcon(this.getClass().getResource("images/market_cashier/market_cashier_male_l1.png"));
		ImageIcon normal_maleL2 = new ImageIcon(this.getClass().getResource("images/market_cashier/market_cashier_male_l2.png"));
		upAnimation.add(normal_maleU1);
		upAnimation.add(normal_maleU2);
		rightAnimation.add(normal_maleR1);
		rightAnimation.add(normal_maleR2);
		downAnimation.add(normal_maleD1);
		downAnimation.add(normal_maleD2);
		leftAnimation.add(normal_maleL1);
		leftAnimation.add(normal_maleL2);
		
		cashierImage = downAnimation.get(0).getImage();
		Collections.rotate(downAnimation, 1);
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos+=20;
			cashierImage = rightAnimation.get(0).getImage();
			Collections.rotate(rightAnimation, 1);
		}
		else if (xPos > xDestination) {
			xPos-=20;
			cashierImage = leftAnimation.get(0).getImage();
			Collections.rotate(leftAnimation, 1);
		}

		else if (yPos < yDestination) {
			yPos+=20;
			cashierImage = downAnimation.get(0).getImage();
			Collections.rotate(downAnimation, 1);
		}
		else if (yPos > yDestination) {
			yPos-=20;
			cashierImage = upAnimation.get(0).getImage();
			Collections.rotate(upAnimation, 1);
		}
		if(xPos == xDestination && yPos == yDestination) {
			if(xDestination == xHome && yDestination == yHome) {
				cashierImage = leftAnimation.get(0).getImage();
			}
		}
		if(xPos == -20 && yPos == 20 && leaving) {
			role.left();
			leaving = false;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawImage(cashierImage, xPos, yPos, null);
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
	public void leave() {
		xDestination = xLeave;
		yDestination = yLeave;
		leaving = true;
	}
}
