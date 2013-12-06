package simcity.gui;

import java.awt.*;

import simcity.CityDirectory;
import simcity.BusAgent;
import simcity.interfaces.BusStop;
import simcity.PersonAgent;
import simcity.TrafficNode;


public class BusGui implements Gui {
	private BusAgent agent = null;
	private CityGui gui;
	private CityDirectory city;

	private static final int width = 20;
	private static final int height = 20;
	private int xPos = 40, yPos = 80;
	private int xGoal = 40, yGoal = 80;
	private String destination;
	
	private enum Command {noCommand, GoToDestination};
	private Command command=Command.noCommand;
	
	public BusGui(BusAgent b, CityGui g, CityDirectory c) {
		agent = b;
		gui = g;
		city = c;
	}

	public void updatePosition() {
		
//		if (city != null && destination != null && xPos == currentNode.x && yPos == currentNode.y) {
//			if (city.getBuildingOrientation(destination).equals("horizontal")) {
//				if (yGoal < yPos) {
//					currentNode = currentNode.getNorthNeighbor();
//				} else if (yGoal > yPos) {
//					currentNode = currentNode.getSouthNeighbor();
//				} else if (xGoal < xPos) {
//					currentNode = currentNode.getWestNeighbor();
//				} else {
//					currentNode = currentNode.getEastNeighbor();
//				}
//			}
//			if (city.getBuildingOrientation(destination).equals("vertical")) {
//				if (xGoal < xPos) {
//					currentNode = currentNode.getWestNeighbor();
//				} else if (xGoal > xPos) {
//					currentNode = currentNode.getEastNeighbor();
//				} else if (yGoal < yPos) {
//					currentNode = currentNode.getNorthNeighbor();
//				} else {
//					currentNode = currentNode.getSouthNeighbor();
//				}
//			}
//		}
//		
		if (destination != null) {
			if (destination.equals("busStop1")) {
				if (xPos > xGoal) {
					if (gui.getMoveBox(xPos-20, yPos).getOpen() && gui.getMoveBox(xPos-10, yPos).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+10).getOpen() && gui.getMoveBox(xPos-10, yPos+10).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+20).getOpen() && gui.getMoveBox(xPos-10, yPos+20).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+30).getOpen() && gui.getMoveBox(xPos-10, yPos+30).getOpen()) {
						xPos-=20;
					}
				} else if (yPos > yGoal) {
					if (gui.getMoveBox(xPos, yPos-20).getOpen() && gui.getMoveBox(xPos, yPos-10).getOpen()
							&& gui.getMoveBox(xPos+10, yPos-20).getOpen() && gui.getMoveBox(xPos+10, yPos-10).getOpen()
							&& gui.getMoveBox(xPos-10, yPos-20).getOpen() && gui.getMoveBox(xPos-10, yPos-10).getOpen()
							&& gui.getMoveBox(xPos-20, yPos-20).getOpen() && gui.getMoveBox(xPos-20, yPos-10).getOpen()) {
						yPos-=20;
					}
				} 
			}
			if (destination.equals("busStop2")) {
				if (yPos > yGoal) {
					if (gui.getMoveBox(xPos, yPos-20).getOpen() && gui.getMoveBox(xPos, yPos-10).getOpen()
							&& gui.getMoveBox(xPos+10, yPos-20).getOpen() && gui.getMoveBox(xPos+10, yPos-10).getOpen()
							&& gui.getMoveBox(xPos-10, yPos-20).getOpen() && gui.getMoveBox(xPos-10, yPos-10).getOpen()
							&& gui.getMoveBox(xPos-20, yPos-20).getOpen() && gui.getMoveBox(xPos-20, yPos-10).getOpen()) {
						yPos-=20;
					}
				} else if (xPos < xGoal) {
					if (gui.getMoveBox(xPos+20, yPos).getOpen() && gui.getMoveBox(xPos+30, yPos).getOpen()
							&& gui.getMoveBox(xPos+20, yPos+10).getOpen() && gui.getMoveBox(xPos+30, yPos+10).getOpen()
							&& gui.getMoveBox(xPos+20, yPos-10).getOpen() && gui.getMoveBox(xPos+30, yPos-10).getOpen()
							&& gui.getMoveBox(xPos+20, yPos-20).getOpen() && gui.getMoveBox(xPos+30, yPos-20).getOpen()) {
						xPos+=20;
					}
				} 
			}
			if (destination.equals("busStop3")) {
				if (xPos < xGoal) {
					if (gui.getMoveBox(xPos+20, yPos).getOpen() && gui.getMoveBox(xPos+30, yPos).getOpen()
							&& gui.getMoveBox(xPos+20, yPos+10).getOpen() && gui.getMoveBox(xPos+30, yPos+10).getOpen()
							&& gui.getMoveBox(xPos+20, yPos-10).getOpen() && gui.getMoveBox(xPos+30, yPos-10).getOpen()
							&& gui.getMoveBox(xPos+20, yPos-20).getOpen() && gui.getMoveBox(xPos+30, yPos-20).getOpen()) {
						xPos+=20;
					}
				} else if (yPos < yGoal) {
					if (gui.getMoveBox(xPos, yPos+20).getOpen() && gui.getMoveBox(xPos, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+10, yPos+20).getOpen() && gui.getMoveBox(xPos+10, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+20, yPos+20).getOpen() && gui.getMoveBox(xPos+20, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+30, yPos+20).getOpen() && gui.getMoveBox(xPos+30, yPos+30).getOpen()) {
						yPos+=20;
					}
				}
			}
			if (destination.equals("busStop4")) {
				if (yPos < yGoal) {
					if (gui.getMoveBox(xPos, yPos+20).getOpen() && gui.getMoveBox(xPos, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+10, yPos+20).getOpen() && gui.getMoveBox(xPos+10, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+20, yPos+20).getOpen() && gui.getMoveBox(xPos+20, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+30, yPos+20).getOpen() && gui.getMoveBox(xPos+30, yPos+30).getOpen()) {
						yPos+=20;
					}
				} else if (xPos > xGoal) {
					if (gui.getMoveBox(xPos-20, yPos).getOpen() && gui.getMoveBox(xPos-10, yPos).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+10).getOpen() && gui.getMoveBox(xPos-10, yPos+10).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+20).getOpen() && gui.getMoveBox(xPos-10, yPos+20).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+30).getOpen() && gui.getMoveBox(xPos-10, yPos+30).getOpen()) {
						xPos-=20;
					}
				}
			}
		}
		
		if (xPos == xGoal && yPos == yGoal) {
        	if (command == Command.GoToDestination) {
        		agent.msgAtStop();
        	}
        	command = Command.noCommand;
        }
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(xPos, yPos, width, height);
	}
	
	public void DoGoToStop(BusStop b) {
		destination = b.getName();
		if (destination.equals("busStop1")) {
			xGoal = city.getBuildingEntrance(b.getName()).x-30;
			yGoal = city.getBuildingEntrance(b.getName()).y;
		} else if (destination.equals("busStop2")) {
			xGoal = city.getBuildingEntrance(b.getName()).x-10;
			yGoal = city.getBuildingEntrance(b.getName()).y-30;
		} else if (destination.equals("busStop3")) {
			xGoal = city.getBuildingEntrance(b.getName()).x+20;
			yGoal = city.getBuildingEntrance(b.getName()).y-10;
		} else {
			xGoal = city.getBuildingEntrance(b.getName()).x;
			yGoal = city.getBuildingEntrance(b.getName()).y+20;
		}
 		command = Command.GoToDestination;
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
}
