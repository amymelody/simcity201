package simcity.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import simcity.PersonAgent;
import simcity.CityDirectory;
import simcity.TrafficNode;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

public class PersonGui implements Gui {
	private PersonAgent agent = null;
	private CityGui gui;
	private CityDirectory city;

	private static final int width = 10;
	private static final int height = 10;
	private static final int respawnTime = 2000;
	private int xPos = 0, yPos = 210;//default Person position
	private int xGoal = xPos, yGoal = yPos;//default Person destination
	private TrafficNode currentNode;
	private String destination;
	private String goalStop;
	private Direction direction = Direction.Right;
	private Font font = new Font("font", Font.PLAIN, 8);
	private Timer timer = new Timer();
	Image personImage;
	List<ImageIcon> upAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> rightAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> downAnimation = new ArrayList<ImageIcon>();
	List<ImageIcon> leftAnimation = new ArrayList<ImageIcon>();
	
	private enum Command {noCommand, Respawn, GoToDestination, BoardBus};
	private enum Direction {Left, Right, Up, Down};
	private Command command=Command.noCommand;

	public PersonGui(PersonAgent p, CityGui g, CityDirectory c) {
		agent = p;
		gui = g;
		city = c;
		currentNode = gui.getTrafficNodes().get(12);
		
		ImageIcon normal_maleU1 = new ImageIcon(this.getClass().getResource("images/person/normal_male_u1.png"));
		ImageIcon normal_maleU2 = new ImageIcon(this.getClass().getResource("images/person/normal_male_u2.png"));
		ImageIcon normal_maleR1 = new ImageIcon(this.getClass().getResource("images/person/normal_male_r1.png"));
		ImageIcon normal_maleR2 = new ImageIcon(this.getClass().getResource("images/person/normal_male_r2.png"));
		ImageIcon normal_maleD1 = new ImageIcon(this.getClass().getResource("images/person/normal_male_d1.png"));
		ImageIcon normal_maleD2 = new ImageIcon(this.getClass().getResource("images/person/normal_male_d2.png"));
		ImageIcon normal_maleL1 = new ImageIcon(this.getClass().getResource("images/person/normal_male_l1.png"));
		ImageIcon normal_maleL2 = new ImageIcon(this.getClass().getResource("images/person/normal_male_l2.png"));
		upAnimation.add(normal_maleU1);
		upAnimation.add(normal_maleU2);
		rightAnimation.add(normal_maleR1);
		rightAnimation.add(normal_maleR2);
		downAnimation.add(normal_maleD1);
		downAnimation.add(normal_maleD2);
		leftAnimation.add(normal_maleL1);
		leftAnimation.add(normal_maleL2);
		
		personImage = downAnimation.get(0).getImage();
		Collections.rotate(downAnimation, 1);
	}

	public void updatePosition() {
		if (command != Command.Respawn) { 
			if (city != null && destination != null && currentNode != null && (xPos == currentNode.x || xPos == currentNode.x2) && (yPos == currentNode.y || yPos == currentNode.y2)) {
				if (city.getBuildingOrientation(destination).equals("horizontal")) {
					if (yGoal < currentNode.y && yGoal < currentNode.y2) {
						currentNode = currentNode.getNorthNeighbor();
						direction = Direction.Up;
					} else if (yGoal > currentNode.y && yGoal > currentNode.y2) {
						currentNode = currentNode.getSouthNeighbor();
						direction = Direction.Down;
					} else if (xGoal < currentNode.x && xGoal < currentNode.x2) {
						currentNode = currentNode.getWestNeighbor();
						direction = Direction.Left;
					} else {
						currentNode = currentNode.getEastNeighbor();
						direction = Direction.Right;
					}
				}
				if (city.getBuildingOrientation(destination).equals("vertical")) {
					if (xGoal < currentNode.x && xGoal < currentNode.x2) {
						currentNode = currentNode.getWestNeighbor();
						direction = Direction.Left;
					} else if (xGoal > currentNode.x && xGoal > currentNode.x2) {
						currentNode = currentNode.getEastNeighbor();
						direction = Direction.Right;
					} else if (yGoal < currentNode.y && yGoal < currentNode.y2) {
						currentNode = currentNode.getNorthNeighbor();
						direction = Direction.Up;
					} else {
						currentNode = currentNode.getSouthNeighbor();
						direction = Direction.Down;
					}
				}
	//			AlertLog.getInstance().logDebug(AlertTag.PERSON, agent.getName(), direction.toString());
	//			AlertLog.getInstance().logDebug(AlertTag.PERSON, agent.getName(), currentNode.x + ", " + currentNode.y);
			}
			
			if (city != null && destination != null && currentNode == null) {
				currentNode = gui.getClosestNode(xPos,yPos);
				if (!((xPos == currentNode.x || xPos == currentNode.x2) && (yPos == currentNode.y || yPos == currentNode.y2))) {
					if (xPos == currentNode.x || xPos == currentNode.x2) {
						if (yGoal < yPos && yPos < currentNode.y) {
							currentNode = currentNode.getNorthNeighbor();
							direction = Direction.Up;
						} else if (yGoal > yPos && yPos > currentNode.y) {
							currentNode = currentNode.getSouthNeighbor();
							direction = Direction.Down;
						}
						if (yPos < currentNode.y) {
							direction = Direction.Down;
						} else {
							direction = Direction.Up;
						}
					} else if (yPos == currentNode.y || yPos == currentNode.y2) {
						if (xGoal < xPos && xPos < currentNode.x) {
							currentNode = currentNode.getWestNeighbor();
							direction = Direction.Left;
						} else if (xGoal > xPos && xPos > currentNode.x) {
							currentNode = currentNode.getEastNeighbor();
							direction = Direction.Right;
						}
						if (xPos < currentNode.x) {
							direction = Direction.Right;
						} else {
							direction = Direction.Left;
						}
					}
				}
	//			AlertLog.getInstance().logDebug(AlertTag.PERSON, agent.getName(), direction.toString());
	//			AlertLog.getInstance().logDebug(AlertTag.PERSON, agent.getName(), currentNode.x + ", " + currentNode.y);
			}
			
			if (destination == null) {
				if (xPos < xGoal) {
					gui.setBox(xPos, yPos, true);
					xPos+=10;
					personImage = rightAnimation.get(0).getImage();
					Collections.rotate(rightAnimation, 1);
					gui.setBox(xPos, yPos, false);
				} else if (xPos > xGoal) {
					gui.setBox(xPos, yPos, true);
					xPos-=10;
					personImage = leftAnimation.get(0).getImage();
					Collections.rotate(leftAnimation, 1);
					gui.setBox(xPos, yPos, false);
				}
				if (yPos < yGoal) {
					gui.setBox(xPos, yPos, true);
					yPos+=10;
					personImage = downAnimation.get(0).getImage();
					Collections.rotate(downAnimation, 1);
					gui.setBox(xPos, yPos, false);
				} else if (yPos > yGoal) {
					gui.setBox(xPos, yPos, true);
					yPos-=10;
					personImage = upAnimation.get(0).getImage();
					Collections.rotate(upAnimation, 1);
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
				//AlertLog.getInstance().logDebug(AlertTag.PERSON, agent.getName(), direction.toString());
				boolean ok = true;
				if (direction == Direction.Right && yPos == currentNode.y) {
					if (gui.getMoveBox(xPos, yPos+10).getOpen()) {
						gui.setBox(xPos, yPos, true);
						yPos+=10;
						personImage = downAnimation.get(0).getImage();
						Collections.rotate(downAnimation, 1);
						gui.setBox(xPos, yPos, true);
						ok = true;
					} else {
						ok = false;
					}
				} else if (direction == Direction.Left && yPos == currentNode.y2) {
					if (gui.getMoveBox(xPos, yPos-10).getOpen()) {
						gui.setBox(xPos, yPos, true);
						yPos-=10;
						personImage = upAnimation.get(0).getImage();
						Collections.rotate(upAnimation, 1);
						gui.setBox(xPos, yPos, true);
						ok = true;
					} else {
						ok = false;
					}
				} else if (direction == Direction.Down && xPos == currentNode.x2) {
					if (gui.getMoveBox(xPos-10, yPos).getOpen()) {
						gui.setBox(xPos, yPos, true);
						xPos-=10;
						personImage = leftAnimation.get(0).getImage();
						Collections.rotate(leftAnimation, 1);
						gui.setBox(xPos, yPos, true);
						ok = true;
					} else {
						ok = false;
					}
				} else if (direction == Direction.Up && xPos == currentNode.x) {
					if (gui.getMoveBox(xPos+10, yPos).getOpen()) {
						gui.setBox(xPos, yPos, true);
						xPos+=10;
						personImage = rightAnimation.get(0).getImage();
						Collections.rotate(rightAnimation, 1);
						gui.setBox(xPos, yPos, true);
						ok = true;
					} else {
						ok = false;
					}
				}
				if (ok) {
					if (direction == Direction.Right && xPos < currentNode.x) {
						if (gui.getMoveBox(xPos+10, yPos).getOpen()) {
							gui.setBox(xPos, yPos, true);
							xPos+=10;
							personImage = rightAnimation.get(0).getImage();
							Collections.rotate(rightAnimation, 1);
							gui.setBox(xPos, yPos, false);
						}
					}
					else if (direction == Direction.Left && xPos > currentNode.x) {
						if (gui.getMoveBox(xPos-10, yPos).getOpen()) {
							gui.setBox(xPos, yPos, true);
							xPos-=10;
							personImage = leftAnimation.get(0).getImage();
							Collections.rotate(leftAnimation, 1);
							gui.setBox(xPos, yPos, false);
						}
					}
			
					if (direction == Direction.Down && yPos < currentNode.y) {
						if (gui.getMoveBox(xPos, yPos+10).getOpen()) {
							gui.setBox(xPos, yPos, true);
							yPos+=10;
							personImage = downAnimation.get(0).getImage();
							Collections.rotate(downAnimation, 1);
							gui.setBox(xPos, yPos, false);
						}
					}
					else if (direction == Direction.Up && yPos > currentNode.y) {
						if (gui.getMoveBox(xPos, yPos-10).getOpen()) {
							gui.setBox(xPos, yPos, true);
							yPos-=10;
							personImage = upAnimation.get(0).getImage();
							Collections.rotate(upAnimation, 1);
							gui.setBox(xPos, yPos, false);
						}
					}
				}
			}
			
			if (direction == Direction.Right && xPos == xGoal && yPos == yGoal+10) {
				gui.setBox(xPos,  yPos,  true);
				yPos -= 10;
				personImage = upAnimation.get(0).getImage();
				Collections.rotate(upAnimation, 1);
			} else if (direction == Direction.Left && xPos == xGoal && yPos == yGoal-10) {
				gui.setBox(xPos,  yPos,  true);
				yPos += 10;
				personImage = downAnimation.get(0).getImage();
				Collections.rotate(downAnimation, 1);
			} else if (direction == Direction.Up && yPos == yGoal && xPos == xGoal+10) {
				gui.setBox(xPos,  yPos,  true);
				xPos -= 10;
				personImage = leftAnimation.get(0).getImage();
				Collections.rotate(leftAnimation, 1);
			} else if (direction == Direction.Down && yPos == yGoal && xPos == xGoal-10) {
				gui.setBox(xPos,  yPos,  true);
				xPos += 10;
				personImage = rightAnimation.get(0).getImage();
				Collections.rotate(rightAnimation, 1);
			} 
			
			if (gui.getMoveBox(xPos, yPos).getHasVehicle() && command != Command.BoardBus) {
				AlertLog.getInstance().logMessage(AlertTag.PERSON, agent.getName(), "I've been hit!");
				command = Command.Respawn;
				gui.setBox(xPos, yPos, true);
				timer.schedule(new TimerTask() {
					public void run() {
						AlertLog.getInstance().logMessage(AlertTag.PERSON, agent.getName(), "Respawned!");
						command = Command.GoToDestination;
						xPos = 0;
						yPos = 0;
						currentNode = gui.getTrafficNodes().get(0);
					}
				},
				respawnTime);//how long to wait before running task
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
		if (command != Command.noCommand && command != Command.Respawn) {
			String text = "";
			switch(agent.getJob()) {
			case "landlordRole":
				text = "la";
				break;
			case "unemployed":
				text = "u";
				break;
			case "restHostRole":
				text = "rH";
				break;
			case "restWaiter1Role":
				text = "rW";
				break;
			case "restWaiter2Role":
				text = "rW";
				break;
			case "restCashierRole":
				text = "rCa";
				break;
			case "restCookRole":
				text = "rCo";
				break;
			case "marketEmployeeRole":
				text = "mE";
				break;
			case "marketDelivererRole":
				text = "mD";
				break;
			case "marketCashierRole":
				text = "mC";
				break;
			case "bankManagerRole":
				text = "bM";
				break;
			case "bankTellerRole":
				text = "bT";
				break;
			}
			g.setColor(Color.YELLOW);
			//g.fillRect(xPos, yPos, width, height);
			g.drawImage(personImage, xPos, yPos, null);
			g.setColor(Color.WHITE);
			g.setFont(font);
			g.drawString(text, xPos-2, yPos+10);
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