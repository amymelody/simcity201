package simcity.bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.bank.BankDepositorRole;

public class BankDepositorGui implements Gui {
	private BankDepositorRole role = null;
	
	private int xPos = -20, yPos = 20;
	boolean releaseIt = false;
	private int xDestination = -20, yDestination = -20;
	private int xHome = 20, yHome = 20;
	BankGui gui;
	public BankDepositorGui(BankDepositorRole r){
		this.role =r;
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
		if(xPos == xDestination && yPos == yDestination){
			if(xDestination == 38 && yDestination == 38){
				role.msgAtManager();
			}
				else if(xDestination == 52 && yDestination == 52){
					role.msgAtTeller();
				}
				else if(xDestination == -20 && yDestination == -20){
					role.msgLeft();
			
				}
				
			}
		}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 30, 30);
		g.drawString("Depositor", xPos, yPos);
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
		xDestination = 38;
		yDestination = 38;
		releaseIt = true;
	}
	public void GoToTeller(){
		xDestination = 52;
		yDestination = 52;
		releaseIt = true;
	}
	
	public void ExitBank(){
		xDestination = -20;
		yDestination = -20;
		releaseIt = true;
	}
	public void AtDestination(){
		releaseIt = false;
	}

	public void setGui(BankGui g) {
		this.gui = gui;
	}
}