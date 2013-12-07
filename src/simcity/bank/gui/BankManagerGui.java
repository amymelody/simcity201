package simcity.bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.bank.BankManagerRole;
import simcity.bank.BankTellerRole;

public class BankManagerGui implements Gui {
	private BankManagerRole role = null;
	
	private int xPos = 40, yPos = 40;
	private int xDestination = 40, yDestination = 40;
	private int xHome = -20, yHome = -20;
	boolean releaseIt = false;
	BankGui gui;
	public BankManagerGui(BankManagerRole r){
		this.role = r;
		xPos = 45;
		yPos = 40;
		xDestination = 45;
		yDestination = 40;
		this.gui = gui;
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
		
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, 30, 30);
		g.drawString("Manager", xPos, yPos);
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
	
	public void ExitBank(){
		xDestination = -20;
		yDestination = 20;
	}
}
	