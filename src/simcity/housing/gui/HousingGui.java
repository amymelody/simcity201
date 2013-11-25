package simcity.housing.gui;

import javax.swing.*;

import simcity.CityDirectory;
import simcity.gui.BuildingGui;
import simcity.gui.CityGui;
import simcity.housing.gui.HousingAnimationPanel;
import simcity.housing.gui.HousingInputPanel;

import java.awt.*;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class HousingGui extends BuildingGui
{
	private HousingAnimationPanel animationPanel;
	private HousingInputPanel inputPanel;
	
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;

	/**
	 * Constructor
	 */
	public HousingGui(String n, CityGui cG, CityDirectory cD)
	{
		super(n, cG, cD);
		
		animationPanel = new HousingAnimationPanel();
		inputPanel = new HousingInputPanel(n);

		//input panel
		double inputFractionOfWindow = 150 / 650;
		Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setVisible(false);
		cG.add(inputPanel);

		//animation panel
		double animationFractionOfWindow = 500 / 650;
		Dimension animDim = new Dimension((int)(WINDOWX * animationFractionOfWindow), WINDOWY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		animationPanel.setVisible(false);
		cG.add(animationPanel);
	}
	
	public void setVisible(boolean visible) {
		if(visible) {
			animationPanel.setVisible(true);
			inputPanel.setVisible(true);
		}
		else {
			animationPanel.setVisible(false);
			inputPanel.setVisible(false);
		}
	}
	
}