package simcity.gui;

import javax.swing.*;
import java.awt.*;
import simcity.CityDirectory;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class CityGui extends JFrame
{
	private JFrame animationFrame = new JFrame("City Animation");
	private CityAnimationPanel animationPanel = new CityAnimationPanel();
	private CityInputPanel inputPanel;

	private int windowX = 650;
	private int windowY = 500;
	private int bufferFromTopOfScreen = 50;
	private int bufferFromSideOfScreen = 15;

	/**
	 * Constructor
	 */
	public CityGui(CityDirectory cd)
	{
		inputPanel = new CityInputPanel(this, cd);
		
		setBounds(bufferFromSideOfScreen, bufferFromTopOfScreen, windowX, windowY);
		BorderLayout frameLayout = new BorderLayout();
		setLayout(frameLayout);

		//input panel
		double inputFractionOfWindow = .3;
		Dimension inputDim = new Dimension((int)(windowX * inputFractionOfWindow), windowY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		add(inputPanel, frameLayout.WEST);

		//animation panel
		double animationFractionOfWindow = .6;
		Dimension animDim = new Dimension((int)(windowX * animationFractionOfWindow), windowY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		add(animationPanel, frameLayout.CENTER);
	}
	
	public void addGui(PersonGui g) {
		animationPanel.addGui(g);
	}
}