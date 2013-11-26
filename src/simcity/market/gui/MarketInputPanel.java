package simcity.market.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MarketInputPanel extends JPanel implements ActionListener
{
	public String name;
	public JButton goBack;
	public MarketGui marketGui;
	
	public MarketInputPanel(MarketGui mG, String n)
	{
		name = n;
		goBack = new JButton ("Top View");
		marketGui = mG;
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == goBack) {
			marketGui.setVisible(false);
		}
		
	}
}
