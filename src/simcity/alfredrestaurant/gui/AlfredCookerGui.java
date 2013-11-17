package restaurant.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import restaurant.CookAgent;

public class CookerGui implements Gui {

	private final int xDoNothingPosition = 50;
	private final int yDoNothingPosition = 20;
	
	private final String IMAGE_FILE = "C:/Users/AlfredKoshan/Documents/GitHub/restaurant_akoshan/cooker.jpg";
	private BufferedImage image;
	private int xPos = xDoNothingPosition, yPos = yDoNothingPosition;
	private int xDestination = xDoNothingPosition, yDestination = yDoNothingPosition;
	private CookAgent agent;
	
	private enum Command {
		noCommand, GoToRefrigerator, GoToCookingArea, GoToPlatingArea, GoToNoCommandArea
	};

	private Command command = Command.noCommand;
	
	public CookerGui(){
		try {
			image = ImageIO.read(new File(IMAGE_FILE));
		} catch (IOException ex) {
			// handle exception...
		}
	}
	@Override
	public void updatePosition() {
		if (xPos < xDestination) {
			xPos++;
		} else if (xPos > xDestination) {
			xPos--;
		}

		if (yPos < yDestination) {
			yPos++;
		} else if (yPos > yDestination) {
			yPos--;
		}		

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.GoToRefrigerator){
				agent.msgComeRefrigerator();
			}else if (command == Command.GoToCookingArea){
				agent.msgComeCookingArea();
			}else if (command == Command.GoToPlatingArea){
				agent.msgComePlatingArea();
			}else if (command == Command.GoToNoCommandArea){
				agent.msgComeDoNothingArea();
			}
		}
		
	}
	
	public boolean isAtCookingArea(){
		return xPos == CookingArea.xCookingPos && yPos == CookingArea.yCookingPos;
	}
	public boolean isAtDoNothingArea(){
		return xPos == xDoNothingPosition && yPos == yDoNothingPosition;
	}
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, xPos, yPos, null);			
	}
	
	public void msgGoToRefrigerator(){
		xDestination = CookingArea.xRefrigeratorPos;
		yDestination = CookingArea.yRefrigeratorPos;
		command = Command.GoToRefrigerator;
	}
	
	public void msgGoToCookingArea(){
		xDestination = CookingArea.xCookingPos;
		yDestination = CookingArea.yCookingPos;
		command = Command.GoToCookingArea;
	}
	
	public void msgGoToPlatingArea(){
		xDestination = CookingArea.xPlatingPos;
		yDestination = CookingArea.yPlatingPos;
		command = Command.GoToPlatingArea;
	}
	
	public void msgGoToNoCommandArea(){
		xDestination = xDoNothingPosition;
		yDestination = yDoNothingPosition;
		command = Command.GoToNoCommandArea;
	}

	@Override
	public boolean isPresent() {		
		return false;
	}
	/**
	 * @param agent the agent to set
	 */
	public void setAgent(CookAgent agent) {
		this.agent = agent;
	}

	
}
