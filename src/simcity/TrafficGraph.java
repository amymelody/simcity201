package simcity;

import java.util.LinkedList;

public class TrafficGraph {
	
	private LinkedList<TrafficNode> nodes;
	
	//create all node nodes and their neighbors
	public void createTrafficGraph(){
		//TODO
		//read from file
		nodes = new LinkedList<TrafficNode>();
	}

	//get nodes in traffic
	public LinkedList<TrafficNode> getNodes() {
		return nodes;
	}
	
}
