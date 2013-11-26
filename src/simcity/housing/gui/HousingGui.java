package simcity.housing.gui;

import simcity.CityDirectory;
import simcity.gui.CityGui;
import simcity.gui.BuildingGui;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Semaphore;

public class HousingGui extends BuildingGui
{
	private HousingAnimationPanel animationPanel;
	private HousingInputPanel inputPanel;
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;
	
	private MoveBox[][] boxes = new MoveBox[25][25];
	private Semaphore[][] boxBlock = new Semaphore[25][25];

	/**
	 * Constructor
	 */
	public HousingGui(String n, CityGui cG, CityDirectory cD)
	{
		super(n, cG, cD);

		animationPanel = new HousingAnimationPanel(cG);
		inputPanel = new HousingInputPanel(n);

		//input panel
		double inputFractionOfWindow = 150.0 / 650.0;
		Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setVisible(false);
		cG.add(inputPanel);

		//animation panel
		double animationFractionOfWindow = 500.0 / 650.0;
		Dimension animDim = new Dimension((int)(WINDOWX * animationFractionOfWindow), WINDOWY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		animationPanel.setVisible(false);
		cG.add(animationPanel);
		
		for(int i = 0; i < 25; i++)
		{
			for(int j = 0; j < 25; j++)
			{
				boxes[i][j] = new MoveBox(j, i, j*20, i*20);
				if(i == 0 || j == 0 || j == 13 || i == 24 || j == 24) //walls
				{
					if(!((i == 1 || i == 2 || i == 22 || i == 23) && j == 13) || ((i == 11 || i == 12 || i == 13) && j == 24)) //doors
					{
						boxes[i][j].setOpen(false);
					}
				}
			}
		}
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
		
		for(int i = 0; i < 25; i++)
		{
			for(int j = 0; j < 25; j++)
			{
				boxBlock[i][j] = new Semaphore(0, true);
			}
		}
	}
	
	public MoveBox getBox(int y, int x)
	{
		try
		{
			boxBlock[y][x].acquire(); //this doesn't also "try" to return, right?
			return boxes[y][x];
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public void setBox(List<MoveBox> borrowedBoxes)
	{
		for(MoveBox b : borrowedBoxes)
		{
			for(int i = 0; i < 25; i++)
			{
				for(int j = 0; j < 25; j++)
				{
					if(boxes[i][j] == b)
					{
						boxes[i][j].setOpen(b.getOpen());
						boxBlock[i][j].release();
					}
				}
			}
		}
	}

	@Override
	public void changeView(boolean visible)
	{
		animationPanel.setVisible(visible);
		inputPanel.setVisible(visible);
	}
}
