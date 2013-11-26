package simcity.bank.gui;

import javax.swing.*;

import simcity.CityDirectory;
import simcity.gui.BuildingGui;
import simcity.gui.BuildingsGui;
import simcity.gui.CityGui;
import simcity.bank.BankDepositorRole;
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
	private Object currentPerson;
	/**
	 * Constructor
	 */
	public BankGui(String n, BuildingsGui bG, CityDirectory cD)
	{
		super(n, bG, cD);
		
		animationPanel = new BankAnimationPanel();
		inputPanel = new BankInputPanel(n);

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
	
	public void addBankDepositorGui(BankDepositorGui bg){
		animationPanel.addGui(bg);
	}
	
	public void addBankTellerGui(BankTellerGui tg){
		animationPanel.addGui(tg);
	}
	
	
}