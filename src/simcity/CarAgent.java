package simcity;

import java.awt.Point;

import simcity.agent.Agent;

//Car Agent
//TODO, it will ask transportation to know how to move to destination
// (must pass through some points)
public class CarAgent extends Agent{

	private Point destination;
	private String destinationString;
	
	public enum AgentState {

		DoingNothing, GoingToDestination
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {

		none, started, gotDestination
		
	};
	
	private PersonAgent person;

	private AgentEvent event = AgentEvent.none;
	
	@Override
	public boolean pickAndExecuteAnAction() {
		if (state ==  AgentState.DoingNothing && event == AgentEvent.started){
			state = AgentState.GoingToDestination;
			//TODO ask CarGUI to move
			return true;
		}
		if (state ==  AgentState.GoingToDestination && event == AgentEvent.gotDestination){
			state = AgentState.DoingNothing;
			atDestination();
			//person.msgGotDestination();
			return true;
		}
		return false;
	}

	//start to run to point
	public void Start(Point point) {
		this.destination = point;
		event = AgentEvent.started;
		stateChanged();
	}
	
	//called by Car GUI to know it reached destination
	public void msgGotDestination() {
		event = AgentEvent.gotDestination;
		stateChanged();
	}

	public void msgGoToDestination(String d) {
		destinationString = d;		
	}
	
	private void atDestination(){
		person.msgAtDestination(destinationString);
	}
}
