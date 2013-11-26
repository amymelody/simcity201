package simcity.housing.test;

import simcity.PersonAgent;
import simcity.housing.LandlordRole;
import simcity.housing.LandlordRole.RenterState;
import simcity.housing.LandlordRole.Command;
import simcity.housing.test.mock.MockPerson;
import simcity.housing.test.mock.MockResident;
import junit.framework.*;

public class LandlordRoleTest extends TestCase
{
	LandlordRole landlord;
	MockPerson person;
	MockResident resident1;
	MockResident resident2;

	public void setUp() throws Exception
	{
		super.setUp();
		landlord = new LandlordRole();
		landlord.unitTesing = true;
		person = new MockPerson("MockPerson");
		landlord.setPerson(person);
		resident1 = new MockResident("MockResident1");
		landlord.addRenter(resident1);
		resident2 = new MockResident("MockResident2");
		landlord.addRenter(resident2);
	}

	public void testRentCollection()
	{
	//Preconditions
		assertEquals("Landlord should start with no commands. It didn't",
				0, landlord.commands.size());
		assertEquals("Landlord's renter list should hold 2 renters. Instead, it has "
				+ landlord.renters.size(), 2, landlord.renters.size());
		assertTrue("Landlord's first renter's state should be state = RenterState.away. It isn't",
				landlord.renters.get(0).state == RenterState.away);
		assertTrue("Landlord's second renter's state should be state = RenterState.away. It isn't",
				landlord.renters.get(1).state == RenterState.away);
		//Checking logs
		assertEquals("Landlord should have an empty event log before the Landlord's scheduler is called. Instead, the Landlord's event log reads: "
				+ landlord.log.toString(), 0, landlord.log.size());
		assertEquals("MockPerson should have an empty event log before the Landlord's scheduler is called. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockResident1 should have an empty event log before the Landlord's scheduler is called. Instead, MockResident1's event log reads: "
				+ resident1.log.toString(), 0, resident1.log.size());
		assertEquals("MockResident2 should have an empty event log before the Landlord's scheduler is called. Instead, MockResident2's event log reads: "
				+ resident2.log.toString(), 0, resident2.log.size());

	//Step 1
		landlord.msgStartShift(); //called from Person

		assertTrue("Landlord should have logged \"Received msgStartShift\" but didn't. ",
				landlord.log.containsString("Received msgStartShift from Person. Command.callRenters"));
		assertEquals("Landlord should have one command. Instead, it has "
				+ landlord.commands.size(), 1, landlord.commands.size());
		assertTrue("Landlord's command should be Command.callRenters. It isn't",
				landlord.commands.get(0) == Command.callRenters);
		assertTrue("LandlordResident's scheduler should have returned true, but didn't.",
				landlord.pickAndExecuteAnAction());
				//running sendRentDue()
		assertEquals("Landlord should have no commands. Instead, it has "
				+ landlord.commands.size(), 0, landlord.commands.size());
		assertTrue("MockResident1 should have logged \"Received msgRentDue\" but didn't",
				resident1.log.containsString("Received msgRentDue from Landlord. Setting person.rentDue and maintenanceSchedule--"));
		assertTrue("Landlord's first renter's state should be state = RenterState.called. It isn't",
				landlord.renters.get(0).state == RenterState.called);
		assertTrue("MockResident2 should have logged \"Received msgRentDue\" but didn't",
				resident2.log.containsString("Received msgRentDue from Landlord. Setting person.rentDue and maintenanceSchedule--"));
		assertTrue("Landlord's second renter's state should be state = RenterState.called. It isn't",
				landlord.renters.get(1).state == RenterState.called);
		//Checking logs
		assertEquals("Landlord should have 1 event logged after the Landlord's scheduler is called for the first time. Instead, it has "
				+ landlord.log.size(), 1, landlord.log.size());
		assertEquals("MockPerson should have an empty event log after the Landlord's scheduler is called for the first time. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockResident1 should have 1 event logged after the Landlord's scheduler is called for the first time. Instead, it has "
				+ resident1.log.size(), 1, resident1.log.size());
		assertEquals("MockResident2 should have 1 event logged after the Landlord's scheduler is called for the first time. Instead, it has "
				+ resident2.log.size(), 1, resident2.log.size());

	//Step 2
		landlord.msgDingDong(resident1);
		
		assertTrue("Landlord should have logged \"Received msgDingDong\" but didn't. His log reads instead: "
				+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received msgDingDong from Resident. State.arrived, Command.collectRent"));
		assertEquals("Landlord should have one command. Instead, it has "
				+ landlord.commands.size(), 1, landlord.commands.size());
		assertTrue("Landlord's command should be Command.collectRent. It isn't",
				landlord.commands.get(0) == Command.collectRent);
		assertTrue("Landlord's first renter's state should be state = RenterState.arrived. It isn't",
				landlord.renters.get(0).state == RenterState.arrived);
		assertTrue("Landlord's second renter's state should be state = RenterState.called. It isn't",
				landlord.renters.get(1).state == RenterState.called);
		assertTrue("LandlordResident's scheduler should have returned true, but didn't.",
				landlord.pickAndExecuteAnAction());
				//running sendAmountOwed()
		assertEquals("Landlord should have no commands. Instead, it has "
				+ landlord.commands.size(), 0, landlord.commands.size());
		assertTrue("MockResident1 should have logged \"Received msgAmountOwed\" but didn't",
				resident1.log.containsString("Received msgAmountOwed from Landlord. Rent = $50, Command.payLandlord"));
		assertTrue("Landlord's first renter's state should be state = RenterState.askedToPay. It isn't",
				landlord.renters.get(0).state == RenterState.askedToPay);
		assertTrue("Landlord's second renter's state should be state = RenterState.called. It isn't",
				landlord.renters.get(1).state == RenterState.called);
		//Checking logs
		assertEquals("Landlord should have 2 events logged after the Landlord's scheduler is called for the second time. Instead, it has "
				+ landlord.log.size(), 2, landlord.log.size());
		assertEquals("MockPerson should have an empty event log after the Landlord's scheduler is called for the second time. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockResident1 should have 2 events logged after the Landlord's scheduler is called for the second time. Instead, it has "
				+ resident1.log.size(), 2, resident1.log.size());
		assertEquals("MockResident2 should have 1 event logged after the Landlord's scheduler is called for the second time. Instead, it has "
				+ resident2.log.size(), 1, resident2.log.size());

	//Step 3
		landlord.msgDingDong(resident2);
		
		assertTrue("Landlord should have logged \"Received msgDingDong\" but didn't. His log reads instead: "
				+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received msgDingDong from Resident. State.arrived, Command.collectRent"));
		assertEquals("Landlord should have one command. Instead, it has "
				+ landlord.commands.size(), 1, landlord.commands.size());
		assertTrue("Landlord's command should be Command.collectRent. It isn't",
				landlord.commands.get(0) == Command.collectRent);
		assertTrue("Landlord's first renter's state should be state = RenterState.askedToPay. It isn't",
				landlord.renters.get(0).state == RenterState.askedToPay);
		assertTrue("Landlord's second renter's state should be state = RenterState.arrived. It isn't",
				landlord.renters.get(1).state == RenterState.arrived);
		assertTrue("LandlordResident's scheduler should have returned true, but didn't.",
				landlord.pickAndExecuteAnAction());
				//running sendAmountOwed()
		assertEquals("Landlord should have no commands. Instead, it has "
				+ landlord.commands.size(), 0, landlord.commands.size());
		assertTrue("MockResident2 should have logged \"Received msgAmountOwed\" but didn't",
				resident2.log.containsString("Received msgAmountOwed from Landlord. Rent = $50, Command.payLandlord"));
		assertTrue("Landlord's first renter's state should be state = RenterState.askedToPay. It isn't",
				landlord.renters.get(0).state == RenterState.askedToPay);
		assertTrue("Landlord's second renter's state should be state = RenterState.askedToPay. It isn't",
				landlord.renters.get(1).state == RenterState.askedToPay);
		//Checking logs
		assertEquals("Landlord should have 3 events logged after the Landlord's scheduler is called for the third time. Instead, it has "
				+ landlord.log.size(), 3, landlord.log.size());
		assertEquals("MockPerson should have an empty event log after the Landlord's scheduler is called for the third time. Instead, the MockPerson's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		assertEquals("MockResident1 should have 2 events logged after the Landlord's scheduler is called for the third time. Instead, it has "
				+ resident1.log.size(), 2, resident1.log.size());
		assertEquals("MockResident2 should have 2 events logged after the Landlord's scheduler is called for the third time. Instead, it has "
				+ resident2.log.size(), 2, resident2.log.size());

		//Step 4
			landlord.msgPayRent(resident2, resident2.rent);
			
			assertTrue("Landlord should have logged \"Received msgPayRent\" but didn't. His log reads instead: "
					+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received msgPayRent from Resident. State.paid. Payment = $50"));
			assertTrue("Landlord's variable moneyEarned should equal $50. It doesn't.",
					landlord.moneyEarned == 50);
			assertEquals("Landlord should have no commands. Instead, it has "
					+ landlord.commands.size(), 0, landlord.commands.size());
			assertTrue("Landlord's first renter's state should be state = RenterState.askedToPay. It isn't",
					landlord.renters.get(0).state == RenterState.askedToPay);
			assertTrue("Landlord's second renter's state should be state = RenterState.paid. It isn't",
					landlord.renters.get(1).state == RenterState.paid);
			assertFalse("LandlordResident's scheduler should have returned false, but didn't.",
					landlord.pickAndExecuteAnAction());
			//Checking logs
			assertEquals("Landlord should have 4 events logged after the Landlord's scheduler is called for the fourth time. Instead, it has "
					+ landlord.log.size(), 4, landlord.log.size());
			assertEquals("MockPerson should have an empty event log after the Landlord's scheduler is called for the fourth time. Instead, the MockPerson's event log reads: "
					+ person.log.toString(), 0, person.log.size());
			assertEquals("MockResident1 should have 2 events logged after the Landlord's scheduler is called for the fourth time. Instead, it has "
					+ resident1.log.size(), 2, resident1.log.size());
			assertEquals("MockResident2 should have 2 events logged after the Landlord's scheduler is called for the fourth time. Instead, it has "
					+ resident2.log.size(), 2, resident2.log.size());

		//Step 5
			landlord.msgPayRent(resident1, resident1.rent);
			
			assertTrue("Landlord should have logged \"Received msgPayRent\" but didn't. His log reads instead: "
					+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received msgPayRent from Resident. State.paid. Payment = $50"));
			assertTrue("Landlord's variable moneyEarned should equal $100. It doesn't.",
					landlord.moneyEarned == 100);
			assertEquals("Landlord should have no commands. Instead, it has "
					+ landlord.commands.size(), 0, landlord.commands.size());
			assertTrue("Landlord's first renter's state should be state = RenterState.paid. It isn't",
					landlord.renters.get(0).state == RenterState.paid);
			assertTrue("Landlord's second renter's state should be state = RenterState.paid. It isn't",
					landlord.renters.get(1).state == RenterState.paid);
			assertTrue("LandlordResident's scheduler should have returned true, but didn't.",
					landlord.pickAndExecuteAnAction());
					//running sendEndShift()
			assertTrue("Landlord's first renter's state should be state = RenterState.away. It isn't",
					landlord.renters.get(0).state == RenterState.away);
			assertTrue("Landlord's second renter's state should be state = RenterState.away. It isn't",
					landlord.renters.get(1).state == RenterState.away);
			assertTrue("MockPerson should have logged \"Received msgIncome\" but didn't",
					person.log.containsString("Received msgIncome from Landlord. Money = $100"));
			assertTrue("MockPerson should have logged \"Received msgEndShift\" but didn't. His log reads instead: "
					+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgEndShift from Landlord"));
			assertTrue("Landlord's variable moneyEarned should be 0. It doesn't.",
					landlord.moneyEarned == 0);
			assertTrue("MockPerson should have logged \"Received msgLeftDestination\" but didn't. His log reads instead: "
					+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
			//Checking logs
			assertEquals("Landlord should have 5 events logged after the Landlord's scheduler is called for the fifth time. Instead, it has "
					+ landlord.log.size(), 5, landlord.log.size());
			assertEquals("MockPerson should have 3 events logged after the Landlord's scheduler is called for the fifth time. Instead, the MockPerson's event log reads: "
					+ person.log.toString(), 3, person.log.size());
			assertEquals("MockResident1 should have 2 events logged after the Landlord's scheduler is called for the fifth time. Instead, it has "
					+ resident1.log.size(), 2, resident1.log.size());
			assertEquals("MockResident2 should have 2 events logged after the Landlord's scheduler is called for the fifth time. Instead, it has "
					+ resident2.log.size(), 2, resident2.log.size());

		//Final Check
			assertFalse("LandlordResident's scheduler should have returned false, but didn't.",
					landlord.pickAndExecuteAnAction());
		
	}
}