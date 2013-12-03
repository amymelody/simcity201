package simcity;

import java.awt.Point;
import java.util.LinkedList;
//Traffic Node represents a Node 
//it is a point that has its neihbous (the places it can move next)
public class TrafficNode extends Point{
	
	private LinkedList<TrafficNode> neighbors;
	public int x2;
	public int y2;

	public TrafficNode(int x, int y) {
		super(x,y);
		x2 = x+20;
		y2 = y+20;
		neighbors = new LinkedList<TrafficNode>();
	}

	//get neighbors
	public LinkedList<TrafficNode> getNeighbors() {
		return neighbors;
	}
	
	public TrafficNode getSouthNeighbor() {
		for (TrafficNode n : neighbors) {
			if (n.y > y) {
				return n;
			}
		}
		return null;
	}
	
	public TrafficNode getNorthNeighbor() {
		for (TrafficNode n : neighbors) {
			if (n.y < y) {
				return n;
			}
		}
		return null;
	}
	
	public TrafficNode getWestNeighbor() {
		for (TrafficNode n : neighbors) {
			if (n.x < x) {
				return n;
			}
		}
		return null;
	}
	
	public TrafficNode getEastNeighbor() {
		for (TrafficNode n : neighbors) {
			if (n.x > x) {
				return n;
			}
		}
		return null;
	}

	//set neighbors
	public void setNeighbors(LinkedList<TrafficNode> neighbors) {
		this.neighbors = neighbors;
	}
	
	public void addNeighbor(TrafficNode n) {
		neighbors.add(n);
	}
}
