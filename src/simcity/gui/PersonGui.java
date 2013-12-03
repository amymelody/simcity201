package simcity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import simcity.PersonAgent;
import simcity.CityDirectory;
import simcity.TrafficNode;

public class PersonGui implements Gui {
	private PersonAgent agent = null;
	private CityGui gui;
	private CityDirectory city;

	private static final int width = 10;
	private static final int height = 10;
	private int xPos = 0, yPos = 0;//default Person position (280,210)
	private int xGoal = xPos, yGoal = yPos;//default Person destination
	private TrafficNode currentNode;
	private String destination;
	private String goalStop;
	
	private enum Command {noCommand, GoToDestination, BoardBus};
	private Command command=Command.noCommand;

	public PersonGui(PersonAgent p, CityGui g, CityDirectory c) {
		agent = p;
		gui = g;
		city = c;
		//currentNode = gui.getTrafficNodes().get(15);
		currentNode = gui.getTrafficNodes().get(0);
	}

	public void updatePosition() {
		
		if (city != null && destination != null && currentNode != null && xPos == currentNode.x && yPos == currentNode.y) {
			if (city.getBuildingOrientation(destination).equals("horizontal")) {
				if (yGoal < yPos) {
					currentNode = currentNode.getNorthNeighbor();
				} else if (yGoal > yPos) {
					currentNode = currentNode.getSouthNeighbor();
				} else if (xGoal < xPos) {
					currentNode = currentNode.getWestNeighbor();
				} else {
					currentNode = currentNode.getEastNeighbor();
				}
			}
			if (city.getBuildingOrientation(destination).equals("vertical")) {
				if (xGoal < xPos) {
					currentNode = currentNode.getWestNeighbor();
				} else if (xGoal > xPos) {
					currentNode = currentNode.getEastNeighbor();
				} else if (yGoal < yPos) {
					currentNode = currentNode.getNorthNeighbor();
				} else {
					currentNode = currentNode.getSouthNeighbor();
				}
			}
		}
		
		if (city != null && destination != null && currentNode == null) {
			currentNode = gui.getClosestNode(xPos,yPos);
			if (!(xPos == currentNode.x && yPos == currentNode.y)) {
				if (xPos == currentNode.x) {
					if (yGoal < yPos) {
						currentNode = currentNode.getNorthNeighbor();
					} else if (yGoal > yPos) {
						currentNode = currentNode.getSouthNeighbor();
					}
				} else if (yPos == currentNode.y) {
					if (xGoal < xPos) {
						currentNode = currentNode.getWestNeighbor();
					} else if (xGoal > xPos) {
						currentNode = currentNode.getEastNeighbor();
					}
				}
			}
		}
		
		if (destination == null) {
			if (xPos < xGoal) {
				gui.setBox(xPos, yPos, true);
				xPos+=10;
				gui.setBox(xPos, yPos, false);
			} else if (xPos > xGoal) {
				gui.setBox(xPos, yPos, true);
				xPos-=10;
				gui.setBox(xPos, yPos, false);
			}
			if (yPos < yGoal) {
				gui.setBox(xPos, yPos, true);
				yPos+=10;
				gui.setBox(xPos, yPos, false);
			} else if (yPos > yGoal) {
				gui.setBox(xPos, yPos, true);
				yPos-=10;
				gui.setBox(xPos, yPos, false);
			}
		}
		
//		if (destination != null) {
//			if (xPos < currentNode.x && xPos != xGoal) {
//				xPos+=10;
//			} else if (xPos > currentNode.x && xPos != xGoal) {
//				xPos-=10;
//			}
//			if (yPos < currentNode.y && yPos != yGoal) {
//				yPos+=10;
//			} else if (yPos > currentNode.y && yPos != yGoal) {
//				yPos-=10;
//			}
//		}
		
		if (destination != null && currentNode != null) {
			if (xPos < currentNode.x && xPos != xGoal) {
				if (gui.getMoveBox(xPos+10, yPos).getOpen()) {
					gui.setBox(xPos, yPos, true);
					xPos+=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos, yPos+10) != null && gui.getMoveBox(xPos, yPos+10).getOpen()) {
					gui.setBox(xPos, yPos, true);
					yPos+=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos-10, yPos) != null && gui.getMoveBox(xPos-10, yPos).getOpen()) {
					gui.setBox(xPos, yPos, true);
					xPos-=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos, yPos-10) != null && gui.getMoveBox(xPos, yPos-10).getOpen()) {
					gui.setBox(xPos, yPos, true);
					yPos-=10;
					gui.setBox(xPos, yPos, false);
				}
			}
			else if (xPos > currentNode.x && xPos != xGoal) {
				if (gui.getMoveBox(xPos-10, yPos).getOpen()) {
					gui.setBox(xPos, yPos, true);
					xPos-=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos, yPos-10) != null && gui.getMoveBox(xPos, yPos-10).getOpen()) {
					gui.setBox(xPos, yPos, true);
					yPos-=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos+10, yPos) != null && gui.getMoveBox(xPos+10, yPos).getOpen()) {
					gui.setBox(xPos, yPos, true);
					xPos+=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos, yPos+10) != null && gui.getMoveBox(xPos, yPos+10).getOpen()) {
					gui.setBox(xPos, yPos, true);
					yPos+=10;
					gui.setBox(xPos, yPos, false);
				}
			}
	
			if (yPos < currentNode.y && yPos != yGoal) {
				if (gui.getMoveBox(xPos, yPos+10).getOpen()) {
					gui.setBox(xPos, yPos, true);
					yPos+=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos-10, yPos) != null && gui.getMoveBox(xPos-10, yPos).getOpen()) {
					gui.setBox(xPos, yPos, true);
					xPos-=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos, yPos-10) != null && gui.getMoveBox(xPos, yPos-10).getOpen()) {
					gui.setBox(xPos, yPos, true);
					yPos-=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos+10, yPos) != null && gui.getMoveBox(xPos+10, yPos).getOpen()) {
					gui.setBox(xPos, yPos, true);
					xPos+=10;
					gui.setBox(xPos, yPos, false);
				}
			}
			else if (yPos > currentNode.y && yPos != yGoal) {
				if (gui.getMoveBox(xPos, yPos-10).getOpen()) {
					gui.setBox(xPos, yPos, true);
					yPos-=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos+10, yPos) != null && gui.getMoveBox(xPos+10, yPos).getOpen()) {
					gui.setBox(xPos, yPos, true);
					xPos+=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos, yPos+10) != null && gui.getMoveBox(xPos, yPos+10).getOpen()) {
					gui.setBox(xPos, yPos, true);
					yPos+=10;
					gui.setBox(xPos, yPos, false);
				} else if (gui.getMoveBox(xPos-10, yPos) != null && gui.getMoveBox(xPos-10, yPos).getOpen()) {
					gui.setBox(xPos, yPos, true);
					xPos-=10;
					gui.setBox(xPos, yPos, false);
				}
			}
		}
		
		
		if (xPos == xGoal && yPos == yGoal) {
        	if (command == Command.GoToDestination) {
        		gui.setBox(xPos, yPos, true);
        		destination = null;
        		currentNode = null;
        		agent.msgAtDestination();
        	}
        	if (command == Command.BoardBus) {
        		gui.setBox(xPos, yPos, true);
        		destination = null;
        		currentNode = null;
        		xPos = city.getBuildingEntrance(goalStop).x;
        		yPos = city.getBuildingEntrance(goalStop).y;
        		xGoal = xPos;
        		yGoal = yPos;
        		agent.msgOnBus();
        	}
        	command = Command.noCommand;
        }
	}

	public void draw(Graphics2D g) {
		if (command != Command.noCommand) { 
			g.setColor(Color.BLUE);
			g.fillRect(xPos, yPos, width, height);
		}
	}
	
	public void DoGoToDestination(String d) {
		destination = d;
		xGoal = city.getBuildingEntrance(d).x;
		yGoal = city.getBuildingEntrance(d).y;
		command = Command.GoToDestination;
	}
	
	public void DoBoardBus(String currentStop, String goalStop) {
		if (currentStop.equals("busStop1")) {
			xGoal = xPos-20;
			yGoal = yPos;
		} else if (currentStop.equals("busStop2")) {
			xGoal = xPos;
			yGoal = yPos-20;
		} else if (currentStop.equals("busStop2")) {
			xGoal = xPos+20;
			yGoal = yPos;
		} else {
			xGoal = xPos;
			yGoal = yPos+20;
		}
		this.goalStop = goalStop;
		command = Command.BoardBus;
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