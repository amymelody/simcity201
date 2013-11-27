package simcity.housing.test;

import simcity.housing.ResidentRole;
import simcity.housing.ResidentRole.ResidentState;
import simcity.housing.ResidentRole.Command;
import simcity.housing.test.mock.MockLandlord;
import simcity.housing.test.mock.MockPerson;
import junit.framework.*;

public class ResidentRoleTest extends TestCase
{
	ResidentRole resident;
	MockPerson person;
	MockLandlord landlord;

	public void setUp() throws Exception
	{
		super.setUp();
		resident = new ResidentRole();
		resident.unitTesting = true;
		person = new MockPerson("MockPerson");
		resident.setPerson(person);
		landlord = new MockLandlord("MockLandlord");
		resident.setLandlord(landlord);
	}

	public void testAtHomeSequence()
	{
	//Preconditions
		assertTrue("Resident should start as state = ResidentState.away. It didn't",
				resident.state == ResidentState.away);
		assertEquals("Resident should start with no commands. It didn't",
				0, resident.commands.size());
		//Checking logs
		assertEquals("Resident should have an empty event log before the Resident's scheduler is called. Instead, the Resident's event log reads: "
				+ resident.log.toString(), 0, resident.log.size());
		assertEquals("MockPerson should have an empty event log before the Resident's scheduler is called. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockLandlord should have an empty event log before the Resident's scheduler is called. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());

	//Step 1
		resident.msgImHome(); //from Person

		assertTrue("Resident should have logged \"Received msgImHome\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgImHome from Person. State.atHome"));
		assertTrue("Resident's state should be state = ResidentState.atHome. It isn't",
				resident.state == ResidentState.atHome);
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertFalse("Resident's scheduler should have returned false, but didn't.",
				resident.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Resident should have 1 event logged after the Resident's scheduler is called for the first time. Instead, it has "
				+ resident.log.size(), 1, resident.log.size());
		assertEquals("MockPerson should have an empty event log after the Resident's scheduler is called for the first time. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockLandlord should have an empty event log after the Resident's scheduler is called for the first time. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());

	//Step 2
		for(int i = 0; i < resident.foodInFridge.size(); i++)
		{
			resident.foodInFridge.get(i).setAmount(1);
		}
		resident.msgEat(); //from Person

		assertTrue("Resident should have logged \"Received msgEat\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgEat from Person. Command.eat"));
		assertTrue("Resident's state should be state = ResidentState.atHome. It isn't",
				resident.state == ResidentState.atHome);
		assertEquals("Resident should have one command. Instead, it has "
				+ resident.commands.size(), 1, resident.commands.size());
		assertTrue("Resident's command should be Command.eat. It isn't",
				resident.commands.get(0) == Command.eat);
		assertEquals("Resident's maintenanceSchedule should equal 2. It doesn't",
				2, resident.maintenanceSchedule);
		assertTrue("Resident's scheduler should have returned true, but didn't.",
				resident.pickAndExecuteAnAction());
				//running eat()
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertTrue("MockPerson should have logged \"Received msgDoneEating\" but didn't",
				person.log.containsString("Received msgDoneEating from Resident"));
		assertTrue("MockPerson should have logged \"Received msgFoodLow\" but didn't. His log reads instead: "
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgFoodLow from Resident. 4 groceries needed"));
		//Checking logs
		assertEquals("Resident should have 2 events logged after the Resident's scheduler is called for the second time. Instead, it has "
				+ resident.log.size(), 2, resident.log.size());
		assertEquals("MockPerson should have 2 events log after the Resident's scheduler is called for the second time. Instead, it has "
				+ person.log.size(), 2, person.log.size());
		assertEquals("MockLandlord should have an empty event log after the Resident's scheduler is called for the second time. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());

	//Step 3
		resident.msgGroceries(person.groceries); //from Person

		assertTrue("Resident should have logged \"Received msgGroceries\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgGroceries from Person. Number of grocery items = 4, Command.putAwayGroceries"));
		assertTrue("Resident's state should be state = ResidentState.atHome. It isn't",
				resident.state == ResidentState.atHome);
		assertEquals("Resident should have one command. Instead, it has "
				+ resident.commands.size(), 1, resident.commands.size());
		assertTrue("Resident's command should be Command.putAwayGroceries. It isn't",
				resident.commands.get(0) == Command.putAwayGroceries);
		assertTrue("Resident's groceries should be the same as MockPerson's groceries. they aren't",
				resident.groceries == person.groceries);
		assertTrue("Resident's scheduler should have returned true, but didn't.",
				resident.pickAndExecuteAnAction());
				//running putGroceriesInFridge()
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertEquals("Resident's foodInFridge should have been restocked. It hasn't",
				5, resident.foodInFridge.get(0).getAmount());
		//Checking logs
		assertEquals("Resident should have 3 events logged after the Resident's scheduler is called for the second time. Instead, it has "
				+ resident.log.size(), 3, resident.log.size());
		assertEquals("MockPerson should have 2 events log after the Resident's scheduler is called for the second time. Instead, it has "
				+ person.log.size(), 2, person.log.size());
		assertEquals("MockLandlord should have an empty event log after the Resident's scheduler is called for the second time. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());
		
	//Step 4
		resident.maintenanceSchedule = 0;
		assertTrue("Resident's scheduler should have returned true, but didn't.",
				resident.pickAndExecuteAnAction());
				//running clean()
		assertEquals("Resident's variable maintenanceSchedule should be reset to 3. It hasn't been ",
				0, resident.commands.size());
		//Checking logs
		assertEquals("Resident should have 3 events logged after the Resident's scheduler is called for the third time. Instead, it has "
				+ resident.log.size(), 3, resident.log.size());
		assertEquals("MockPerson should have 2 events log after the Resident's scheduler is called for the third time. Instead, it has "
				+ person.log.size(), 2, person.log.size());
		assertEquals("MockLandlord should have an empty event log after the Resident's scheduler is called for the third time. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());

		assertFalse("Resident's scheduler should have returned false, but didn't.",
				resident.pickAndExecuteAnAction());
	}
	
	public void testRentSequence()
	{
	//Preconditions
		assertTrue("Resident should start as state = ResidentState.away. It didn't",
				resident.state == ResidentState.away);
		assertEquals("Resident should start with no commands. It didn't",
				0, resident.commands.size());
		//Checking logs
		assertEquals("Resident should have an empty event log before the Resident's scheduler is called. Instead, the Resident's event log reads: "
				+ resident.log.toString(), 0, resident.log.size());
		assertEquals("MockPerson should have an empty event log before the Resident's scheduler is called. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockLandlord should have an empty event log before the Resident's scheduler is called. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());

	//Step 1
		resident.msgImHome(); //from Person

		assertTrue("Resident should have logged \"Received msgImHome\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgImHome from Person. State.atHome"));
		assertTrue("Resident's state should be state = ResidentState.atHome. It isn't",
				resident.state == ResidentState.atHome);
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertFalse("Resident's scheduler should have returned false, but didn't.",
				resident.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Resident should have 1 event logged after the Resident's scheduler is called for the first time. Instead, it has "
				+ resident.log.size(), 1, resident.log.size());
		assertEquals("MockPerson should have an empty event log after the Resident's scheduler is called for the first time. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockLandlord should have an empty event log after the Resident's scheduler is called for the first time. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());
		
	//Step 2
		assertFalse("MockPerson's rentDue should be false. It isn't", person.rentDue);
		resident.msgRentDue(); //from Landlord

		assertTrue("Resident should have logged \"Received msgRentDue\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgRentDue from Landlord. Setting person.rentDue"));
		assertTrue("Resident's state should be state = ResidentState.atHome. It isn't",
				resident.state == ResidentState.atHome);
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertTrue("MockPerson's rentDue should be true. It isn't",
				person.rentDue);
		assertFalse("Resident's scheduler should have returned false, but didn't.",
				resident.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Resident should have 2 events logged after the Resident's scheduler is called for the second time. Instead, it has "
				+ resident.log.size(), 2, resident.log.size());
		assertEquals("MockPerson should have an empty event log after the Resident's scheduler is called for the second time. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockLandlord should have an empty event log after the Resident's scheduler is called for the second time. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());

	//Step 3
		resident.msgLeave(); //from Person

		assertTrue("Resident should have logged \"Received msgLeave\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgLeave from Person. Command.leave"));
		assertTrue("Resident's state should be state = ResidentState.atHome. It isn't",
				resident.state == ResidentState.atHome);
		assertEquals("Resident should have one command. Instead, it has "
				+ resident.commands.size(), 1, resident.commands.size());
		assertTrue("Resident's command should be Command.leave. It isn't",
				resident.commands.get(0) == Command.leave);
		assertTrue("Resident's scheduler should have returned true, but didn't.",
				resident.pickAndExecuteAnAction());
				//running leaveHousing()
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertTrue("Resident's state should be state = ResidentState.away. It isn't",
				resident.state == ResidentState.away);
		assertTrue("MockPerson should have logged \"Received msgLeftDestination\" but didn't. His log reads instead: "
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		//Checking logs
		assertEquals("Resident should have 3 events logged after the Resident's scheduler is called for the third time. Instead, it has "
				+ resident.log.size(), 3, resident.log.size());
		assertEquals("MockPerson should have 1 event logged after the Resident's scheduler is called for the third time. Instead, it has "
				+ person.log.size(), 1, person.log.size());
		assertEquals("MockLandlord should have an empty event log after the Resident's scheduler is called for the third time. Instead, the MockLandlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());
		
	//Step 4
		resident.msgAtLandlord(); //from Person

		assertTrue("Resident should have logged \"Received msgAtLandlord\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgAtLandlord from Person. State.atLandlord, Command.talkToLandlord"));
		assertTrue("Resident's state should be state = ResidentState.atLandlord. It isn't",
				resident.state == ResidentState.atLandlord);
		assertEquals("Resident should have one command. Instead, it has "
				+ resident.commands.size(), 1, resident.commands.size());
		assertTrue("Resident's command should be Command.talkToLandlord. It isn't",
				resident.commands.get(0) == Command.talkToLandlord);
		assertTrue("Resident's scheduler should have returned true, but didn't.",
				resident.pickAndExecuteAnAction());
				//running sendDingDong()
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertTrue("MockLandlord should have logged \"Received msgDingDong\" but didn't. His log reads instead: "
				+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received msgDingDong from Resident. State.arrived, Command.collectRent"));
		//Checking logs
		assertEquals("Resident should have 4 events logged after the Resident's scheduler is called for the fourth time. Instead, it has "
				+ resident.log.size(), 4, resident.log.size());
		assertEquals("MockPerson should have 1 event logged after the Resident's scheduler is called for the fourth time. Instead, it has "
				+ person.log.size(), 1, person.log.size());
		assertEquals("MockLandlord should have 1 event logged after the Resident's scheduler is called for the fourth time. Instead, it has "
				+ landlord.log.toString(), 1, landlord.log.size());

	//Step 5
		resident.msgAmountOwed(50); //from Landlord
		
		assertTrue("Resident should have logged \"Received msgAmountOwed\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgAmountOwed from Landlord. Rent = $50, Command.payLandlord"));
		assertTrue("Resident's state should be state = ResidentState.atLandlord. It isn't",
				resident.state == ResidentState.atLandlord);
		assertEquals("Resident should have one command. Instead, it has "
				+ resident.commands.size(), 1, resident.commands.size());
		assertTrue("Resident's command should be Command.payLandlord. It isn't",
				resident.commands.get(0) == Command.payLandlord);
		assertEquals("Resident's rent variavble ought to equal $50. It doesn't",
				50, resident.rent);
		assertTrue("Resident's scheduler should have returned true, but didn't.",
				resident.pickAndExecuteAnAction());
				//running sendPayRent()
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertTrue("MockLandlord should have logged \"Received msgPayRent\" but didn't. His log reads instead: "
				+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received msgPayRent from Resident. State.paid. Payment = $50"));
		assertTrue("MockPerson should have logged \"Received msgExpense\" but didn't. His log reads instead: "
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgExpense from Resident. Rent = $50"));
		assertEquals("Resident's rent variavble ought to equal -1. It doesn't",
				-1, resident.rent);
		//Checking logs
		assertEquals("Resident should have 5 events logged after the Resident's scheduler is called for the fifth time. Instead, it has "
				+ resident.log.size(), 5, resident.log.size());
		assertEquals("MockPerson should have 2 events logged after the Resident's scheduler is called for the fifth time. Instead, it has "
				+ person.log.size(), 2, person.log.size());
		assertEquals("MockLandlord should have 2 events logged after the Resident's scheduler is called for the fifth time. Instead, it has "
				+ landlord.log.toString(), 2, landlord.log.size());

	//Step 6
		resident.msgLeave(); //from Person

		assertTrue("Resident should have logged \"Received msgLeave\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgLeave from Person. Command.leave"));
		assertTrue("Resident's state should be state = ResidentState.atLandlord. It isn't",
				resident.state == ResidentState.atLandlord);
		assertEquals("Resident should have one command. Instead, it has "
				+ resident.commands.size(), 1, resident.commands.size());
		assertTrue("Resident's command should be Command.leave. It isn't",
				resident.commands.get(0) == Command.leave);
		assertTrue("Resident's scheduler should have returned true, but didn't.",
				resident.pickAndExecuteAnAction());
				//running leaveHousing()
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertTrue("Resident's state should be state = ResidentState.away. It isn't",
				resident.state == ResidentState.away);
		assertTrue("MockPerson should have logged \"Received msgLeftDestination\" but didn't. His log reads instead: "
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		//Checking logs
		assertEquals("Resident should have 6 events logged after the Resident's scheduler is called for the sixth time. Instead, it has "
				+ resident.log.size(), 6, resident.log.size());
		assertEquals("MockPerson should have 3 events logged after the Resident's scheduler is called for the sixth time. Instead, it has "
				+ person.log.size(), 3, person.log.size());
		assertEquals("MockLandlord should have 2 events logged after the Resident's scheduler is called for the sixth time. Instead, it has "
				+ landlord.log.toString(), 2, landlord.log.size());

	//Step 7
		resident.msgImHome(); //from Person

		assertTrue("Resident should have logged \"Received msgImHome\" but didn't. His log reads instead: "
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgImHome from Person. State.atHome"));
		assertTrue("Resident's state should be state = ResidentState.atHome. It isn't",
				resident.state == ResidentState.atHome);
		assertEquals("Resident should have no commands. Instead, it has "
				+ resident.commands.size(), 0, resident.commands.size());
		assertFalse("Resident's scheduler should have returned false, but didn't.",
				resident.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Resident should have 7 events logged after the Resident's scheduler is called for the seventh time. Instead, it has "
				+ resident.log.size(), 7, resident.log.size());
		assertEquals("MockPerson should have 3 events logged after the Resident's scheduler is called for the seventh time. Instead, it has "
				+ person.log.size(), 3, person.log.size());
		assertEquals("MockLandlord should have 2 events logged after the Resident's scheduler is called for the seventh time. Instead, it has "
				+ landlord.log.toString(), 2, landlord.log.size());
		
		assertFalse("Resident's scheduler should have returned false, but didn't.",
				resident.pickAndExecuteAnAction());
	}
}