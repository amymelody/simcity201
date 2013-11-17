package simcity;

import java.util.List;

import simcity.agent.Agent;

public class BusStopAgent extends Agent{

	private List<PersonAgent> waitingPassengers;
	
	
	public enum AgentState {

		 WaitingForBus
	};

	private AgentState state = AgentState.WaitingForBus;// The start state

	public enum AgentEvent {

		none, busCome
		
	};

	private AgentEvent event = AgentEvent.none;
	
	private BusAgent bus;
	
	@Override
	public boolean pickAndExecuteAnAction() {
		if ( state == AgentState.WaitingForBus && event == AgentEvent.busCome){
			for (PersonAgent agent: waitingPassengers){
				bus.loadPassenger(agent);
			}
			waitingPassengers.clear();
			return true;
		}
		return false;
	}
	
	public void msgBusCome(BusAgent bus){
		this.bus = bus;
		event = AgentEvent.busCome;
		stateChanged();
	}

	public void msgWaitingForBus(PersonAgent personAgent) {
		waitingPassengers.add(personAgent);
	}

}
