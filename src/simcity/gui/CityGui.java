package simcity.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	private static CityDirectory cityDirectory;
	private static BuildingGui buildingGui;
	
	private int windowX = 650;
	private int windowY = 500;
	private int bufferFromTopOfScreen = 50;
	private int bufferFromSideOfScreen = 15;

	/**
	 * Constructor
	 */
	public CityGui(CityDirectory cd, BuildingGui bg)
	{
		inputPanel = new CityInputPanel(this, cd, bg);
		
		setBounds(bufferFromSideOfScreen, bufferFromTopOfScreen, windowX, windowY);
		BorderLayout frameLayout = new BorderLayout();
		setLayout(frameLayout);

		//input panel
		double inputFractionOfWindow = 150 / 650;
		Dimension inputDim = new Dimension((int)(windowX * inputFractionOfWindow), windowY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		add(inputPanel, frameLayout.WEST);

		//animation panel
		double animationFractionOfWindow = 500 / 650;
		Dimension animDim = new Dimension((int)(windowX * animationFractionOfWindow), windowY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		add(animationPanel, frameLayout.CENTER);
	}
	
	public void addGui(PersonGui g) {
		animationPanel.addGui(g);
	}
	
	public void addGui(BuildingGui g) {
		
	}
	
	/**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
            CityGui gui = new CityGui(cityDirectory, buildingGui);
            gui.setTitle("SimCity201: City of the Blind");
            gui.setVisible(true);
            gui.setResizable(false);
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
}