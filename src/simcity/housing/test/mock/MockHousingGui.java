package simcity.housing.test.mock;

import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.housing.gui.MoveBox;
import simcity.housing.gui.ResidentGui;
import simcity.interfaces.HousingGuiInterface;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockHousingGui extends Mock implements HousingGuiInterface
{

	public MockHousingGui(String name)
	{
		super(name);

		for(int i = 0; i < 25; i++)
		{
			for(int j = 0; j < 25; j++)
			{
				boxes[i][j] = new MoveBox(j, i, j*20, i*20);
				if(i == 0 || j == 0 || j == 13 || i == 24 || j == 24) //walls
				{
					if(!((i == 1 || i == 2 || i == 15 || i == 16 || i == 17 || i == 18 || i == 19) && j == 13) && !((i == 11 || i == 12 || i == 13) && j == 24)) //doors
					{
						boxes[i][j].setOpen(false);
					}
				}
			}
		}
		boxes[12][1].setOpen(false); //Kitchen/Dining Room divider
		boxes[12][2].setOpen(false);
		boxes[12][3].setOpen(false);
		boxes[12][10].setOpen(false);
		boxes[12][11].setOpen(false);
		boxes[12][12].setOpen(false);
		boxes[6][1].setOpen(false); //stove
		boxes[6][2].setOpen(false);
		boxes[7][1].setOpen(false);
		boxes[7][2].setOpen(false);
		boxes[8][1].setOpen(false);
		boxes[8][2].setOpen(false);
		boxes[6][11].setOpen(false); //fridge
		boxes[6][12].setOpen(false);
		boxes[7][11].setOpen(false);
		boxes[7][12].setOpen(false);
		boxes[8][11].setOpen(false);
		boxes[8][12].setOpen(false);
		boxes[16][5].setOpen(false); //table
		boxes[16][6].setOpen(false);
		boxes[16][7].setOpen(false);
		boxes[16][8].setOpen(false);
		boxes[17][5].setOpen(false);
		boxes[17][6].setOpen(false);
		boxes[17][7].setOpen(false);
		boxes[17][8].setOpen(false);
		boxes[18][5].setOpen(false);
		boxes[18][6].setOpen(false);
		boxes[18][7].setOpen(false);
		boxes[18][8].setOpen(false);
		boxes[11][14].setOpen(false); //tv
		boxes[12][14].setOpen(false);
		boxes[13][14].setOpen(false);
		boxes[11][18].setOpen(false); //sofa
		boxes[11][19].setOpen(false);
		boxes[12][19].setOpen(false);
		boxes[13][18].setOpen(false);
		boxes[13][19].setOpen(false);
	}

	@Override
	public MoveBox getBox(int y, int x)
	{
		log.add(new LoggedEvent("Received getBox. Y = " + y + ", X = " + x));
		return boxes[y][x];
	}

	@Override
	public void setBox(List<MoveBox> borrowedBoxes)
	{
		log.add(new LoggedEvent("Received setBox. List size = " + borrowedBoxes.size()));
	}

}
