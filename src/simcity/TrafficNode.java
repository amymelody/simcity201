package simcity;

import java.awt.Point;
import java.util.LinkedList;
//Traffic Node represents a Node 
//it is a point that has its neihbous (the places it can move next)
public class TrafficNode extends Point{
	
	//neighbors nodes
	private LinkedList<TrafficNode> neighbors;

	//get neighbors
	public LinkedList<TrafficNode> getNeighbors() {
		return neighbors;
	}

	//set neighbors
	public void setNeighbors(LinkedList<TrafficNode> neighbors) {
		this.neighbors = neighbors;
	}
}
