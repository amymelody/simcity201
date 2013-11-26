package simcity.bank.gui;

import javax.swing.*;

import simcity.CityDirectory;
import simcity.gui.BuildingGui;
import simcity.gui.CityGui;
import simcity.bank.gui.BankAnimationPanel;
import simcity.bank.gui.BankInputPanel;

import java.awt.*;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class BankGui extends BuildingGui
{
	private BankAnimationPanel animationPanel;
	private BankInputPanel inputPanel;
	
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;

	/**
	 * Constructor
	 */
	public BankGui(String n, CityGui cG, CityDirectory cD)
	{
		super(n, cG, cD);
		
		animationPanel = new BankAnimationPanel();
		inputPanel = new BankInputPanel(n);

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