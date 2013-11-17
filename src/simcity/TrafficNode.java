package simcity;

import java.awt.Point;
import java.util.LinkedList;
//Traffic Node represents a Node 
//it is a point that has its neihbous (the places it can move next)
public class TrafficNode extends Point{
	
	//neighbors nodes
	private LinkedList<TrafficNode> neighbors;

	public TrafficNode(int x, int y) {
		super(x,y);
	}

	//get neighbors
	public LinkedList<TrafficNode> getNeighbors() {
		return neighbors;
	}

	//set neighbors
	public void setNeighbors(LinkedList<TrafficNode> neighbors) {
		this.neighbors = neighbors;
	}
}
