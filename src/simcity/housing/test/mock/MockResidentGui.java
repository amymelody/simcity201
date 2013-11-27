package simcity.housing.test.mock;

import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockResidentGui extends Mock implements Gui
{

	public MockResidentGui(String name)
	{
		super(name);
	}

	@Override
	public void updatePosition()
	{
		log.add(new LoggedEvent(""));
	}

	@Override
	public void draw(Graphics2D g)
	{
		log.add(new LoggedEvent(""));
	}

	@Override
	public boolean isPresent()
	{
		log.add(new LoggedEvent(""));
		return false;
	}

}
