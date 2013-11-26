package simcity.market.gui;

import javax.swing.*;

import simcity.CityDirectory;
import simcity.gui.BuildingGui;
import simcity.gui.BuildingsGui;
import simcity.gui.CityGui;

import java.awt.*;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class MarketGui extends BuildingGui
{
	private MarketAnimationPanel animationPanel;
	private MarketInputPanel inputPanel;
	
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;

	/**
	 * Constructor
	 */
	public MarketGui(String n, BuildingsGui bG, CityDirectory cD)
	{
		super(n, bG, cD);
		
		animationPanel = new MarketAnimationPanel();
		inputPanel = new MarketInputPanel(this, n);

		//input panel
		double inputFractionOfWindow = 150 / 650;
		Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setVisible(false);
		bG.add(inputPanel);

		//animation panel
		double animationFractionOfWindow = 500 / 650;
		Dimension animDim = new Dimension((int)(WINDOWX * animationFractionOfWindow), WINDOWY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		animationPanel.setVisible(false);
		bG.add(animationPanel);
		
		
	}
	
	public void changeView(boolean visible) {
		animationPanel.setVisible(visible);
		inputPanel.setVisible(visible);
	}
	
}