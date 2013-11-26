package simcity.housing.gui;

import simcity.CityDirectory;
import simcity.gui.CityGui;
import simcity.gui.BuildingGui;
import javax.swing.*;
import java.awt.*;

public class HousingGui extends BuildingGui
{
	private HousingAnimationPanel animationPanel;
	private HousingInputPanel inputPanel;
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;
	
	private MoveBox[][] boxes = new MoveBox[25][25];

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
				boxes[i][j] = new MoveBox(j*20, i*20);
				if(i == 0 || j == 0 || i == 24 || j == 24)
				{
					boxes[i][j].setOpen(false);
				}
			}
		}
	}
	
	public void setVisible(boolean visible)
	{
		if(visible)
		{
			animationPanel.setVisible(true);
			inputPanel.setVisible(true);
		}
		else
		{
			animationPanel.setVisible(false);
			inputPanel.setVisible(false);
		}
	}
}
