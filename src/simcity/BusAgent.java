package simcity;

import java.util.LinkedList;
import java.util.List;

import agent.Agent;

public class BusAgent extends Agent{

	//nodes that bus goes through, the BusGUI uses this attribute
	//to know where to move to
	private LinkedList<TrafficNode> nodes;
	
	//list of its bus stops
	private List<TrafficNode> busStops;
	
	//passengers are in bus
	private List<BusJob> inBusPassengers;
	
	public enum AgentState {

		DoingNothing, GoingToBusStop
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {

		none, started, gotBusStop
		
	};

	private AgentEvent event = AgentEvent.none;
	private int nextBusStopIndex = 0;
	
	private List<BusJob> waitingPassengers;
	
	@Override
	public boolean pickAndExecuteAnAction() {
		if (state == AgentState.DoingNothing && event == AgentEvent.started){
			state = AgentState.GoingToBusStop;
			return true;
		}
		if (state == AgentState.GoingToBusStop && event == AgentEvent.gotBusStop){
			state = AgentState.GoingToBusStop;
			//1, load, unload passenger if any
			loadPassenger(busStops.get(nextBusStopIndex));
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
	private void loadPassenger(TrafficNode busstop){
		while(true){
			for (BusJob pass: waitingPassengers){
				if (pass.destination.equals(busstop)){
					inBusPassengers.add(pass);
					continue;
				}
			}
			break;
		}
	}
	
	//at bus stop, some passengers are getting off
	private void unloadPassenger(TrafficNode busstop){
		while(true){
			for (BusJob pass: inBusPassengers){
				if (pass.destination.equals(busstop)){
					inBusPassengers.remove(pass);
					continue;
				}
			}
			break;
		}
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

	
	//person is getting on bus
	public void msgComingAboard(PersonAgent personAgent, String destination) {
		//passenger has to wait until bus reached the bus stop that passenger can get on
		//it is put in waiting list
		waitingPassengers.add(new BusJob(personAgent, destination));
	}
	
	class BusJob{
		PersonAgent person;
		String destination;
		public BusJob(PersonAgent person, String destination) {
			this.person = person;
			this.destination = destination;
		}
	}
}
