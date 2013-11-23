package simcity.test;

import simcity.BusAgent;
import simcity.CarAgent;
import simcity.BusStopAgent;
import simcity.test.mock.MockBus;
import simcity.test.mock.MockBusStop;
import simcity.test.mock.MockCar;

import java.util.*;

public class TransportationTest extends TestCase{
	
	CarAgent cars;
	BusAgent buses;
	BusStopAgent busStops;
	MockBus bus;
	MockCar car;
	MockBusStop busStop;
	
	public void setUp() throws Exception{
		super.setUp();
		cars = new CarAgent();
		car = new MockCar("car");
		buses = new BusAgent();
		bus = new MockBus("bus");
	}
	
}