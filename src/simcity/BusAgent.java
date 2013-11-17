package simcity;

import java.util.LinkedList;
import java.util.List;

import simcity.agent.Agent;

public class BusAgent extends Agent{

	//nodes that bus goes through, the BusGUI uses this attribute
	//to know where to move to
	private LinkedList<TrafficNode> nodes;
	
	//list of its bus stops
	private List<TrafficNode> busStops;
	
	//passengers are in bus
	private List<PersonAgent> inBusPassengers;
	
	public enum AgentState {

		DoingNothing, GoingToBusStop
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {

		none, started, gotBusStop
		
	};
	
	private BusStopAgent busStop;

	private AgentEvent event = AgentEvent.none;
	private int nextBusStopIndex = 0;
	
	@Override
	public boolean pickAndExecuteAnAction() {
		if (state == AgentState.DoingNothing && event == AgentEvent.started){
			state = AgentState.GoingToBusStop;
			return true;
		}
		if (state == AgentState.GoingToBusStop && event == AgentEvent.gotBusStop){
			state = AgentState.GoingToBusStop;
			busStop.msgBusCome(this);
			//1, load, unload passenger if any
			unloadPassenger(busStops.get(nextBusStopIndex));
			
			//2. calculate the next bus stop to move to			
			if (nextBusStopIndex ==busStops.size() - 1){
				nextBusStopIndex = 0;
			}
			
			return true;
		}
		return false;
	}
	
	//at bus stop, some passengers are getting on
	public void loadPassenger(PersonAgent passenger){
		inBusPassengers.add(passenger);
		passenger.msgBusIsHere(this);
	}
	
	//at bus stop, some passengers are getting off
	private void unloadPassenger(TrafficNode busstop){
		while(true){
			for (PersonAgent pass: inBusPassengers){
				if (pass.getDestination().equals(busstop)){
					inBusPassengers.remove(pass);
					pass.getOffBus(this);
					continue;
				}
			}
			break;
		}
	}
	
	public void commingAboard(PersonAgent pass){
		inBusPassengers.add(pass);
	}
	
	//called from Transportation to start the bus
	public void msgStart(){
		event = AgentEvent.started;
		stateChanged();
	}
	
	//called from BusGUI when it reached bus stop
	public void msgGotToBusStop(){
		event = AgentEvent.gotBusStop;
		stateChanged();
	}
}
