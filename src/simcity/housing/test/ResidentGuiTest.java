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
		assertTrue("MockHousingGui should have logged \"Received setBox\" but didn't. Instead, it reads: "
				+ house.log.toString(), house.log.containsString("Received setBox. List size = 0"));
		assertEquals("Gui variable xDestination should equal 480. It doesn't",
				480, gui.xDestination);
		assertEquals("Gui variable yDestination should equal 220. It doesn't",
				220, gui.yDestination);
		assertEquals("Gui variable xPos should equal 499. It doesn't",
				499, gui.xPos);
		assertEquals("Gui variable yPos should equal 220. It doesn't",
				220, gui.yPos);
		//Checking logs
		assertEquals("Gui should have 3 events logged. Instead, it has "
				+ gui.log.size(), 3, gui.log.size());
		assertEquals("MockHousingGui should have 2 events logged. Instead, it reads: "
				+ house.log.toString(), 2, house.log.size());

	//Step 3
		gui.xPos = 480;
		gui.updatePosition();

		assertTrue("Gui should have logged \"Received updatePosition\" but didn't. Instead, it reads: "
				+ gui.log.toString(), gui.log.containsString("Received updatePosition"));
		assertTrue("Gui should have logged \"Received findMoveBox\" but didn't. Instead, it reads: "
				+ gui.log.toString(), gui.log.containsString("Received findMoveBox"));
		assertTrue("MockHousingGui should have logged \"Received getBox\" but didn't. Instead, it reads: "
				+ house.log.toString(), house.log.containsString("Received getBox. Y = 11, X = 23"));
		assertTrue("MockHousingGui should have logged \"Received getBox\" but didn't. Instead, it reads: "
				+ house.log.toString(), house.log.containsString("Received getBox. Y = 12, X = 24"));
		assertTrue("MockHousingGui should have logged \"Received setBox\" but didn't. Instead, it reads: "
				+ house.log.toString(), house.log.containsString("Received setBox. List size = 2"));
		assertEquals("Gui variable xDestination should equal 460. It doesn't",
				460, gui.xDestination);
		assertEquals("Gui variable yDestination should equal 220. It doesn't",
				220, gui.yDestination);
		assertEquals("Gui variable xPos should equal 479. It doesn't",
				479, gui.xPos);
		assertEquals("Gui variable yPos should equal 220. It doesn't",
				220, gui.yPos);
		//Checking logs
		assertEquals("Gui should have 5 events logged. Instead, it has "
				+ gui.log.size(), 5, gui.log.size());
		assertEquals("MockHousingGui should have 5 events logged. Instead, it has "
				+ house.log.size(), 5, house.log.size());

	//Step 3
		gui.xPos = 460;
		gui.updatePosition();

		assertTrue("Gui should have logged \"Received updatePosition\" but didn't. Instead, it reads: "
				+ gui.log.toString(), gui.log.containsString("Received updatePosition"));
		assertTrue("Gui should have logged \"Received findMoveBox\" but didn't. Instead, it reads: "
				+ gui.log.toString(), gui.log.containsString("Received findMoveBox"));
		assertTrue("MockHousingGui should have logged \"Received getBox\" but didn't. Instead, it reads: "
				+ house.log.toString(), house.log.containsString("Received getBox. Y = 11, X = 23"));
		assertTrue("MockHousingGui should have logged \"Received getBox\" but didn't. Instead, it reads: "
				+ house.log.toString(), house.log.containsString("Received getBox. Y = 12, X = 24"));
		assertTrue("MockHousingGui should have logged \"Received setBox\" but didn't. Instead, it reads: "
				+ house.log.toString(), house.log.containsString("Received setBox. List size = 2"));
		assertEquals("Gui variable boxX should equal 22. It doesn't",
				22, gui.boxX);
		assertEquals("Gui variable boxY should equal 11. It doesn't",
				11, gui.boxY);
		assertEquals("Gui variable xDestination should equal 440. It doesn't",
				440, gui.xDestination);
		assertEquals("Gui variable yDestination should equal 220. It doesn't",
				220, gui.yDestination);
		assertEquals("Gui variable xPos should equal 459. It doesn't",
				459, gui.xPos);
		assertEquals("Gui variable yPos should equal 220. It doesn't",
				220, gui.yPos);
		//Checking logs
		assertEquals("Gui should have 7 events logged. Instead, it has "
				+ gui.log.size(), 7, gui.log.size());
		assertEquals("MockHousingGui should have 8 events logged. Instead, it has "
				+ house.log.size(), 8, house.log.size());

	//Step 4
		gui.xPos = 440;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 21. It doesn't",
				21, gui.boxX);
		assertEquals("Gui variable boxY should equal 11. It doesn't",
				11, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 9 events logged. Instead, it has "
				+ gui.log.size(), 9, gui.log.size());
		assertEquals("MockHousingGui should have 11 events logged. Instead, it has "
				+ house.log.size(), 11, house.log.size());
	
	//Step 5
		gui.xPos = 420;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 20. It doesn't",
				20, gui.boxX);
		assertEquals("Gui variable boxY should equal 11. It doesn't",
				11, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 11 events logged. Instead, it has "
				+ gui.log.size(), 11, gui.log.size());
		assertEquals("MockHousingGui should have 14 events logged. Instead, it has "
				+ house.log.size(), 14, house.log.size());
		
	//Step 6
		gui.xPos = 400;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 20. It doesn't",
				20, gui.boxX);
		assertEquals("Gui variable boxY should equal 12. It doesn't",
				12, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 13 events logged. Instead, it has "
				+ gui.log.size(), 13, gui.log.size());
		assertEquals("MockHousingGui should have 17 events logged. Instead, it has "
				+ house.log.size(), 17, house.log.size());

	//Step 7
		gui.yPos = 240;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 20. It doesn't",
				20, gui.boxX);
		assertEquals("Gui variable boxY should equal 13. It doesn't",
				13, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 15 events logged. Instead, it has "
				+ gui.log.size(), 15, gui.log.size());
		assertEquals("MockHousingGui should have 21 events logged. Instead, it has "
				+ house.log.size(), 21, house.log.size());
		
	//Step 8
		gui.yPos = 260;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 20. It doesn't",
				20, gui.boxX);
		assertEquals("Gui variable boxY should equal 14. It doesn't",
				14, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 17 events logged. Instead, it has "
				+ gui.log.size(), 17, gui.log.size());
		assertEquals("MockHousingGui should have 27 events logged. Instead, it has "
				+ house.log.size(), 27, house.log.size());
		
	//Step 9
		gui.yPos = 280;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 19. It doesn't",
				19, gui.boxX);
		assertEquals("Gui variable boxY should equal 14. It doesn't",
				14, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 19 events logged. Instead, it has "
				+ gui.log.size(), 19, gui.log.size());
		assertEquals("MockHousingGui should have 30 events logged. Instead, it has "
				+ house.log.size(), 30, house.log.size());
		
	//Step 9
		gui.yPos = 280;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 19. It doesn't",
				19, gui.boxX);
		assertEquals("Gui variable boxY should equal 14. It doesn't",
				14, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 20 events logged. Instead, it has "
				+ gui.log.size(), 20, gui.log.size());
		assertEquals("MockHousingGui should have 30 events logged. Instead, it has "
				+ house.log.size(), 30, house.log.size());
		
	//Step 10
		gui.xPos = 380;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 18. It doesn't",
				18, gui.boxX);
		assertEquals("Gui variable boxY should equal 14. It doesn't",
				14, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 22 events logged. Instead, it has "
				+ gui.log.size(), 22, gui.log.size());
		assertEquals("MockHousingGui should have 33 events logged. Instead, it has "
				+ house.log.size(), 33, house.log.size());
		
	//Step 11
		gui.xPos = 360;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 17. It doesn't",
				17, gui.boxX);
		assertEquals("Gui variable boxY should equal 14. It doesn't",
				14, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 24 events logged. Instead, it has "
				+ gui.log.size(), 24, gui.log.size());
		assertEquals("MockHousingGui should have 37 events logged. Instead, it has "
				+ house.log.size(), 37, house.log.size());
		
	//Step 12
		gui.xPos = 340;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 17. It doesn't",
				17, gui.boxX);
		assertEquals("Gui variable boxY should equal 13. It doesn't",
				13, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 26 events logged. Instead, it has "
				+ gui.log.size(), 26, gui.log.size());
		assertEquals("MockHousingGui should have 40 events logged. Instead, it has "
				+ house.log.size(), 40, house.log.size());
		
	//Step 13
		gui.yPos = 260;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 17. It doesn't",
				17, gui.boxX);
		assertEquals("Gui variable boxY should equal 12. It doesn't",
				12, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 28 events logged. Instead, it has "
				+ gui.log.size(), 28, gui.log.size());
		assertEquals("MockHousingGui should have 43 events logged. Instead, it has "
				+ house.log.size(), 43, house.log.size());
		
	//Step 14
		gui.yPos = 240;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 18. It doesn't",
				18, gui.boxX);
		assertEquals("Gui variable boxY should equal 12. It doesn't",
				12, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 30 events logged. Instead, it has "
				+ gui.log.size(), 30, gui.log.size());
		assertEquals("MockHousingGui should have 47 events logged. Instead, it has "
				+ house.log.size(), 47, house.log.size());
		
	//Step 15
		gui.xPos = 360;
		gui.updatePosition();
		assertEquals("Gui variable boxX should equal 18. It doesn't",
				18, gui.boxX);
		assertEquals("Gui variable boxY should equal 12. It doesn't",
				12, gui.boxY);
		//Checking logs
		assertEquals("Gui should have 31 events logged. Instead, it has "
				+ gui.log.size(), 31, gui.log.size());
		assertEquals("MockHousingGui should have 47 events logged. Instead, it has "
				+ house.log.size(), 47, house.log.size());
	}
}