package simcity.gui;

import javax.swing.*;

import simcity.alfredrestaurant.gui.AlfredRestaurantInputPanel;
import simcity.anjalirestaurant.gui.AnjaliRestaurantInputPanel;
import simcity.bank.gui.BankAnimationPanel;
import simcity.bank.gui.BankInputPanel;
import simcity.cherysrestaurant.gui.CherysRestaurantInputPanel;
import simcity.housing.gui.HousingAnimationPanel;
import simcity.housing.gui.HousingInputPanel;
import simcity.jesusrestaurant.gui.JesusRestaurantInputPanel;
import simcity.market.gui.MarketAnimationPanel;
import simcity.market.gui.MarketInputPanel;

import java.awt.*;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class BuildingGui extends JFrame
{
	private JFrame animationFrame = new JFrame("City Animation");
	private BankAnimationPanel bankAnimationPanel = new BankAnimationPanel();
	private BankInputPanel bankInputPanel = new BankInputPanel(this);
	private HousingAnimationPanel housingAnimationPanel = new HousingAnimationPanel();
	private HousingInputPanel housingInputPanel = new HousingInputPanel(this);
	private MarketAnimationPanel marketAnimationPanel = new MarketAnimationPanel();
	private MarketInputPanel marketInputPanel = new MarketInputPanel(this);
	private AlfredRestaurantAnimationPanel alfredRestaurantAnimationPanel = new AlfredRestaurantAnimationPanel();
	private AlfredRestaurantInputPanel alfredRestaurantInputPanel = new AlfredRestaurantInputPanel(this);
	private AnjaliRestaurantAnimationPanel anjaliRestaurantAnimationPanel = new AnjaliRestaurantAnimationPanel();
	private AnjaliRestaurantInputPanel anjaliRestaurantInputPanel = new AnjaliRestaurantInputPanel(this);
	private CherysRestaurantAnimationPanel cherysRestaurantAnimationPanel = new CherysRestaurantAnimationPanel();
	private CherysRestaurantInputPanel cherysRestaurantInputPanel = new CherysRestaurantInputPanel(this);
	private JesusRestaurantAnimationPanel jesusRestaurantAnimationPanel = new JesusRestaurantAnimationPanel();
	private JesusRestaurantInputPanel jesusRestaurantInputPanel = new JesusRestaurantInputPanel(this);
	private JoshRestaurantAnimationPanel joshRestaurantAnimationPanel = new JoshRestaurantAnimationPanel();
	private AlfredRestaurantInputPanel joshRestaurantInputPanel = new AlfredRestaurantInputPanel(this);

	private int windowX = 650;
	private int windowY = 500;
	private int bufferFromTopOfScreen = 50;
	private int bufferFromSideOfScreen = 600;

	/**
	 * Constructor
	 */
	public BuildingGui()
	{
		setBounds(bufferFromSideOfScreen, bufferFromTopOfScreen, windowX, windowY);
		BorderLayout frameLayout = new BorderLayout();
		setLayout(frameLayout);

		//input panel
		double inputFractionOfWindow = .3;
		Dimension inputDim = new Dimension((int)(windowX * inputFractionOfWindow), windowY);
		bankInputPanel.setPreferredSize(inputDim);
		bankInputPanel.setMinimumSize(inputDim);
		bankInputPanel.setMaximumSize(inputDim);
		bankInputPanel.setVisible(false);
		add(bankInputPanel, frameLayout.WEST);
		housingInputPanel.setPreferredSize(inputDim);
		housingInputPanel.setMinimumSize(inputDim);
		housingInputPanel.setMaximumSize(inputDim);
		housingInputPanel.setVisible(false);
		add(housingInputPanel, frameLayout.WEST);
		marketInputPanel.setPreferredSize(inputDim);
		marketInputPanel.setMinimumSize(inputDim);
		marketInputPanel.setMaximumSize(inputDim);
		marketInputPanel.setVisible(false);
		add(marketInputPanel, frameLayout.WEST);
		alfredRestaurantInputPanel.setPreferredSize(inputDim);
		alfredRestaurantInputPanel.setMinimumSize(inputDim);
		alfredRestaurantInputPanel.setMaximumSize(inputDim);
		alfredRestaurantInputPanel.setVisible(false);
		add(alfredRestaurantInputPanel, frameLayout.WEST);
		anjaliRestaurantInputPanel.setPreferredSize(inputDim);
		anjaliRestaurantInputPanel.setMinimumSize(inputDim);
		anjaliRestaurantInputPanel.setMaximumSize(inputDim);
		anjaliRestaurantInputPanel.setVisible(false);
		add(anjaliRestaurantInputPanel, frameLayout.WEST);
		cherysRestaurantInputPanel.setPreferredSize(inputDim);
		cherysRestaurantInputPanel.setMinimumSize(inputDim);
		cherysRestaurantInputPanel.setMaximumSize(inputDim);
		cherysRestaurantInputPanel.setVisible(false);
		add(cherysRestaurantInputPanel, frameLayout.WEST);
		jesusRestaurantInputPanel.setPreferredSize(inputDim);
		jesusRestaurantInputPanel.setMinimumSize(inputDim);
		jesusRestaurantInputPanel.setMaximumSize(inputDim);
		jesusRestaurantInputPanel.setVisible(false);
		add(jesusRestaurantInputPanel, frameLayout.WEST);
		joshRestaurantInputPanel.setPreferredSize(inputDim);
		joshRestaurantInputPanel.setMinimumSize(inputDim);
		joshRestaurantInputPanel.setMaximumSize(inputDim);
		joshRestaurantInputPanel.setVisible(false);
		add(joshRestaurantInputPanel, frameLayout.WEST);

		//animation panel
		double animationFractionOfWindow = .6;
		Dimension animDim = new Dimension((int)(windowX * animationFractionOfWindow), windowY);
		bankAnimationPanel.setPreferredSize(animDim);
		bankAnimationPanel.setMinimumSize(animDim);
		bankAnimationPanel.setMaximumSize(animDim);
		add(bankAnimationPanel, frameLayout.CENTER);
		housingAnimationPanel.setPreferredSize(animDim);
		housingAnimationPanel.setMinimumSize(animDim);
		housingAnimationPanel.setMaximumSize(animDim);
		add(housingAnimationPanel, frameLayout.CENTER);
		marketAnimationPanel.setPreferredSize(animDim);
		marketAnimationPanel.setMinimumSize(animDim);
		marketAnimationPanel.setMaximumSize(animDim);
		add(marketAnimationPanel, frameLayout.CENTER);
		alfredRestaurantAnimationPanel.setPreferredSize(animDim);
		alfredRestaurantAnimationPanel.setMinimumSize(animDim);
		alfredRestaurantAnimationPanel.setMaximumSize(animDim);
		add(alfredRestaurantAnimationPanel, frameLayout.CENTER);
		anjaliRestaurantAnimationPanel.setPreferredSize(animDim);
		anjaliRestaurantAnimationPanel.setMinimumSize(animDim);
		anjaliRestaurantAnimationPanel.setMaximumSize(animDim);
		add(anjaliRestaurantAnimationPanel, frameLayout.CENTER);
		cherysRestaurantAnimationPanel.setPreferredSize(animDim);
		cherysRestaurantAnimationPanel.setMinimumSize(animDim);
		cherysRestaurantAnimationPanel.setMaximumSize(animDim);
		add(cherysRestaurantAnimationPanel, frameLayout.CENTER);
		jesusRestaurantAnimationPanel.setPreferredSize(animDim);
		jesusRestaurantAnimationPanel.setMinimumSize(animDim);
		jesusRestaurantAnimationPanel.setMaximumSize(animDim);
		add(jesusRestaurantAnimationPanel, frameLayout.CENTER);
		joshRestaurantAnimationPanel.setPreferredSize(animDim);
		joshRestaurantAnimationPanel.setMinimumSize(animDim);
		joshRestaurantAnimationPanel.setMaximumSize(animDim);
		add(joshRestaurantAnimationPanel, frameLayout.CENTER);
	}
}