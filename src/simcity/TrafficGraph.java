package simcity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class TrafficGraph {
	
	private LinkedList<TrafficNode> nodes;
	
	//create all node nodes and their neighbors
	public void createTrafficGraph(){
		//read from file
		nodes = new LinkedList<TrafficNode>();
		try {
			Scanner input = new Scanner(new File("graph.txt"));
			int nodesCount = Integer.parseInt(input.nextLine());
			for (int i = 0 ; i < nodesCount; i++){
				String line = input.nextLine();
				String[] tokens = line.split(" ");
				int index = Integer.parseInt(tokens[0]);
				int x = Integer.parseInt(tokens[1]);
				int y = Integer.parseInt(tokens[2]);
				nodes.add(new TrafficNode(x, y));
			}
			int linksCount = Integer.parseInt(input.nextLine());
			for (int i = 0 ; i < linksCount; i++){
				String line = input.nextLine();
				String[] tokens = line.split(" ");
				int index = Integer.parseInt(tokens[0]);
				for (int j = 1; j < tokens.length; j++){
					nodes.get(index).getNeighbors().add(nodes.get(Integer.parseInt(tokens[j])));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//get nodes in traffic
	public LinkedList<TrafficNode> getNodes() {
		return nodes;
	}
	
}
