package simcity.bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.bank.BankTellerRole;

public class BankTellerGui implements Gui {
	private BankTellerRole role = null;
	
	private int xPos = -20, yPos = -20;
	private int xDestination = -20, yDestination = -20;
	private int xHome = -20, yHome = -20;
	
	public BankTellerGui(BankTellerRole r){
		this.role = r;
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
		g.setColor(Color.YELLOW);
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
	
	public void GoToManager(){
		
	}
	
	public void ExitBank(){
		
	}
}