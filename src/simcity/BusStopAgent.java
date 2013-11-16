package simcity;

import java.util.List;

import agent.Agent;

public class BusStopAgent extends Agent{

	private List<PersonAgent> inBusPassengers;
	
	@Override
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	public void msgWaitingForBus(PersonAgent personAgent) {
		inBusPassengers.add(personAgent);
	}

}
