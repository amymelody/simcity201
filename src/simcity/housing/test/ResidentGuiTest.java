package simcity.housing.test;

import java.awt.Point;

import simcity.PersonAgent;
import simcity.housing.LandlordRole;
import simcity.housing.LandlordRole.RenterState;
import simcity.housing.gui.ResidentGui;
import simcity.housing.gui.ResidentGui.Command;
import simcity.housing.test.mock.MockHousingGui;
import simcity.housing.test.mock.MockPerson;
import simcity.housing.test.mock.MockResident;

import junit.framework.*;

public class ResidentGuiTest extends TestCase
{
	ResidentGui gui;
	MockResident resident;
	MockHousingGui house;

	public void setUp() throws Exception
	{
		super.setUp();
		resident = new MockResident("MockResident");
		gui = new ResidentGui(resident);
		house = new MockHousingGui("MockHousingGui");
		gui.setGui(house);
	}

	public void testRentCollection()
	{
	//Preconditions
		assertEquals("Gui's pastBoxes list should start empty. Instead, it has "
				+ gui.pastBoxes.size(), 0, gui.pastBoxes.size());
		assertTrue("Gui should start with command = Command.noCommand. It didn't",
				gui.command == Command.noCommand);
		assertTrue("Gui should start with pastCommand = Command.noCommand. It didn't",
				gui.pastCommand == Command.noCommand);
		//Checking logs
		assertEquals("Gui should have an empty event log. Instead, it reads: "
				+ gui.log.toString(), 0, gui.log.size());
		assertEquals("MockHousingGui should have an empty event log. Instead, it reads: "
				+ house.log.toString(), 0, house.log.size());

	//Step 1
		gui.doGoToLocation(new Point(360, 240), ""); //Sofa

		assertTrue("Gui should have logged \"Received doGoToLocation\" but didn't. Instead, it reads: "
				+ gui.log.toString(), gui.log.containsString("Received doGoToLocation. String purpose = "));
		assertEquals("Gui's variable xGoal should equal 360. It doesn't",
				360, gui.xGoal);
		assertEquals("Gui's variable yGoal should equal 240. It doesn't",
				240, gui.yGoal);
		assertTrue("Gui's variable command should be command = Command.noCommand. It isn't",
				gui.command == Command.noCommand);
		assertTrue("Gui variable pastCommand should be pastCommand = Command.noCommand. It isn't",
				gui.pastCommand == Command.noCommand);
		//Checking logs
		assertEquals("Gui should have 1 event logged. Instead, it has "
				+ gui.log.size(), 1, gui.log.size());
		assertEquals("MockHousingGui should have an empty event log. Instead, it reads: "
				+ house.log.toString(), 0, house.log.size());

	//Step 2
		gui.updatePosition();

		assertTrue("Gui should have logged \"Received updatePosition\" but didn't. Instead, it reads: "
				+ gui.log.toString(), gui.log.containsString("Received updatePosition"));
		assertTrue("Gui should have logged \"Received findMoveBox\" but didn't. Instead, it reads: "
				+ gui.log.toString(), gui.log.containsString("Received findMoveBox"));
		assertTrue("MockHousingGui should have logged \"Received getBox\" but didn't. Instead, it reads: "
				+ house.log.toString(), house.log.containsString("Received getBox. Y = 11, X = 24"));
		
		
		
		
		
	}
}