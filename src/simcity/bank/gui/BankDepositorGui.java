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
	
	
	

	public BankDepositorGui(BankDepositorRole d) {
		this.role = d;
		xPos = -20;
		yPos = -20;
		xDestination = -20;
		yDestination = -20;
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
		if(xPos == xDestination && yPos == yDestination){
			{
				if(releaseIt){
					AtDestination();
				}
			
				}
				
			}
		}

	public void draw(Graphics2D g) {
	if(role.getRobberStatus() == false){
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 30, 30);
		g.drawString("Depositor", xPos, yPos);
	}
	if(role.getRobberStatus()){
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, 30, 30);
		g.drawString("Robber", xPos, yPos);
	}
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
		xDestination = 100;
		yDestination = 200;
		releaseIt = true;
	}
	
	public void RobBank(){
		xDestination = 75;
		yDestination = 70;
		releaseIt = true;
	}
	
	public void ExitBank(){
		xDestination = -20;
		yDestination = -20;
		releaseIt = true;
	}
	public void AtDestination(){
		role.msgAtDestination();
		releaseIt = false;
	}

	public void setGui(BankGui g) {
		this.gui = gui;
	}
}