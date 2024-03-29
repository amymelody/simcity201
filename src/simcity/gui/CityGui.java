package simcity.gui;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import simcity.CityDirectory;
import simcity.bank.BankDepositorRole;
import simcity.bank.BankTellerRole;
import simcity.housing.LandlordRole;
import simcity.housing.ResidentRole;
import simcity.housing.gui.MoveBox;
import simcity.market.MarketCustomerRole;
import simcity.market.MarketEmployeeRole;
import simcity.market.MarketDelivererRole;
import simcity.RestWaiterRole;
import simcity.RestCustomerRole;
import simcity.TrafficNode;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class CityGui extends JFrame
{
	private JFrame animationFrame = new JFrame("City Animation");
	CityAnimationPanel animationPanel;
	private CityInputPanel inputPanel;
	private static CityDirectory cityDirectory;

	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 20;
	private final int BUFFERSIDE = 15;
	
	private MoveBox[][] boxes = new MoveBox[50][50];
	private List<TrafficNode> trafficNodes = new ArrayList<TrafficNode>();

	/**
	 * Constructor
	 */
	public CityGui(CityDirectory cd)
	{		
		animationPanel = new CityAnimationPanel(this);
		inputPanel = new CityInputPanel(this, cd, null);

		setBounds(BUFFERSIDE, BUFFERTOP, WINDOWX, WINDOWY);
		BorderLayout frameLayout = new BorderLayout();
		setLayout(frameLayout);

		// input panel
		double inputFractionOfWindow = 150.0 / 650.0;
		Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		add(inputPanel, frameLayout.WEST);

		// animation panel
		double animationFractionOfWindow = 500.0 / 650.0;
		Dimension animDim = new Dimension((int)(WINDOWX * animationFractionOfWindow), WINDOWY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		add(animationPanel, frameLayout.CENTER);
		
		//create move boxes
		for (int i = 0; i < 50; i++) {
			for(int j = 0; j < 50; j++) {
				boxes[i][j] = new MoveBox(j, i, j*10, i*10);
			}
		}
		
		//buildings
		for (int i=0; i<2; i++) {
			for (int j=0; j<2; j++) {
				for (int x=8; x<21; x++) {
					for (int y=8; y<21; y++) {
						boxes[i*21 + x][j*21 + y].setOpen(false);
					}
				}
			}
		}
		
		//add Traffic Nodes
		trafficNodes.add(new TrafficNode(0,0));
		trafficNodes.add(new TrafficNode(60,0));
		trafficNodes.add(new TrafficNode(210,0));
		trafficNodes.add(new TrafficNode(270,0));
		trafficNodes.add(new TrafficNode(420,0));
		trafficNodes.add(new TrafficNode(480,0));
		trafficNodes.add(new TrafficNode(0,60));
		trafficNodes.add(new TrafficNode(60,60));
		trafficNodes.add(new TrafficNode(210,60));
		trafficNodes.add(new TrafficNode(270,60));
		trafficNodes.add(new TrafficNode(420,60));
		trafficNodes.add(new TrafficNode(480,60));
		trafficNodes.add(new TrafficNode(0,210));
		trafficNodes.add(new TrafficNode(60,210));
		trafficNodes.add(new TrafficNode(210,210));
		trafficNodes.add(new TrafficNode(270,210));
		trafficNodes.add(new TrafficNode(420,210));
		trafficNodes.add(new TrafficNode(480,210));
		trafficNodes.add(new TrafficNode(0,270));
		trafficNodes.add(new TrafficNode(60,270));
		trafficNodes.add(new TrafficNode(210,270));
		trafficNodes.add(new TrafficNode(270,270));
		trafficNodes.add(new TrafficNode(420,270));
		trafficNodes.add(new TrafficNode(480,270));
		trafficNodes.add(new TrafficNode(0,420));
		trafficNodes.add(new TrafficNode(60,420));
		trafficNodes.add(new TrafficNode(210,420));
		trafficNodes.add(new TrafficNode(270,420));
		trafficNodes.add(new TrafficNode(420,420));
		trafficNodes.add(new TrafficNode(480,420));
		trafficNodes.add(new TrafficNode(0,480));
		trafficNodes.add(new TrafficNode(60,480));
		trafficNodes.add(new TrafficNode(210,480));
		trafficNodes.add(new TrafficNode(270,480));
		trafficNodes.add(new TrafficNode(420,480));
		trafficNodes.add(new TrafficNode(480,480));
		
		for (TrafficNode node : trafficNodes) {
			for (TrafficNode n : trafficNodes) {
				if (n.x == node.x && n.y != node.y) {
					if ((node.y + 150 >= n.y && n.y > node.y) || (node.y - 150 <= n.y && n.y < node.y)) {
						node.addNeighbor(n);
					}
				}
				if (n.y == node.y && n.x != node.x) {
					if ((node.x + 150 >= n.x && n.x > node.x) || (node.x - 150 <= n.x && n.x < node.x)) {
						node.addNeighbor(n);
					}
				}
			}
		}
	}

	public List<TrafficNode> getTrafficNodes() {
		return trafficNodes;
	}
	
	public TrafficNode getClosestNode(int x, int y) {
		int distance = 100000000;
		TrafficNode temp = trafficNodes.get(0);
		for (TrafficNode t : trafficNodes) {
			int sum = Math.abs(x-t.x) + Math.abs(y-t.y);
			if (sum < distance) {
				distance = sum;
				temp = t;
			}
		}
		return temp;
	}
	
	public MoveBox getMoveBox(int x, int y) {
		for (int i=0; i<50; i++) {
			for (int j=0; j<50; j++) {
				if (boxes[i][j].getX() == x && boxes[i][j].getY() == y) {
					return boxes[i][j];
				}
			}
		}
		return null;
	}
	
	public void setBox(int x, int y, boolean open) {
		for (int i=0; i<50; i++) {
			for (int j=0; j<50; j++) {
				if (boxes[i][j].getX() == x && boxes[i][j].getY() == y) {
					boxes[i][j].setOpen(open);
				}
			}
		}
	}
	
	public void addGui(PersonGui g) {
		animationPanel.addGui(g);
	}
	
	public void addGui(BusGui g) {
		animationPanel.addGui(g);
	}

	public void addRestCustomer(RestCustomerRole c, String location) {
		animationPanel.addRestCustomer(c, location);
	}

	public void addRestWaiter(RestWaiterRole w) {
		animationPanel.addRestWaiter(w);
	}
	
	public void addResident(ResidentRole r, String homeName, String ownerHomeName) {
		animationPanel.addResident(r, homeName, ownerHomeName);
	}
	
	public void addLandlord(LandlordRole l, String buildingName) {
		animationPanel.addLandlord(l, buildingName);
	}
	
	public void addMarketCustomer(MarketCustomerRole c, String location) {
		animationPanel.addMarketCustomer(c, location);
	}
	
	public void addMarketEmployee(MarketEmployeeRole e) {
		animationPanel.addMarketEmployee(e);
	}
	
	public void addMarketDeliverer(MarketDelivererRole d) {
		animationPanel.addMarketDeliverer(d);
	}
	
	public void addBankDepositor(BankDepositorRole d, String location) {
		animationPanel.addBankDepositor(d, location);
	}
	
	public void addBankTeller(BankTellerRole t) {
		animationPanel.addBankTeller(t);
	}
	
	public void businessIsClosed(String building, boolean closed) {
		inputPanel.businessIsClosed(building, closed);
	}
}