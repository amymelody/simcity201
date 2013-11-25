package simcity.market.gui;

import javax.swing.*;

import simcity.gui.BuildingGui;

import java.awt.*;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class MarketGui extends JFrame
{
	private JFrame animationFrame = new JFrame("Market Animation");
	private MarketAnimationPanel animationPanel = new MarketAnimationPanel();
	private MarketInputPanel inputPanel;
	private BuildingGui buildingGui;
	
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;

	/**
	 * Constructor
	 */
	public MarketGui()
	{
		inputPanel = new MarketInputPanel(buildingGui);
		
		setBounds(BUFFERSIDE, BUFFERTOP, WINDOWX, WINDOWY);
		BorderLayout frameLayout = new BorderLayout();
		setLayout(frameLayout);

		//input panel
		double inputFractionOfWindow = 150 / 650;
		Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		add(inputPanel, frameLayout.WEST);

		//animation panel
		double animationFractionOfWindow = 500 / 650;
		Dimension animDim = new Dimension((int)(WINDOWX * animationFractionOfWindow), WINDOWY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		add(animationPanel, frameLayout.CENTER);
	}
	
	/**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
            MarketGui gui = new MarketGui();
            gui.setTitle("SimCity201: Market");
            gui.setVisible(false);
            gui.setResizable(false);
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
}