package simcity.market.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MarketInputPanel extends JPanel implements ActionListener
{
	static final int LSPACE = 10;
	static final int GROWS = 3;
	static final int GCOLUMNS = 1;
	
	public String name;
	public MarketGui marketGui;
	public JButton goBack;
	private JPanel marketLabel = new JPanel();
    JLabel marketInfo = new JLabel();
	
	public MarketInputPanel(MarketGui mG, String n)
	{
		name = n;
		marketGui = mG;
		
		goBack = new JButton ("Top View");
		goBack.setPreferredSize(new Dimension(10, 20));
        goBack.addActionListener(this);
        
        setLayout(new GridLayout(LSPACE, LSPACE));
        add(goBack);
	}
	
	private void initMarketLabel() {
        marketLabel.setLayout(new BorderLayout());
        marketInfo.setText(
                "<html><br/>"
                + "<h3>Welcome to " + name + "!</h3>"
                		+ "<h4><u> Menu</u></h4><table>"
                		+ "<tr><td>Steak</td><td>$15.00</td></tr>"
                		+ "<tr><td>Salad</td><td>$5.00</td></tr>"
                		+ "<tr><td>Pizza</td><td>$8.00</td></tr>"
                		+ "</table><br></html>");

        marketLabel.setBorder(BorderFactory.createBevelBorder(0));
        marketLabel.add(marketInfo, BorderLayout.CENTER);
        marketLabel.add(new JLabel("      "), BorderLayout.EAST);
        marketLabel.add(new JLabel("      "), BorderLayout.WEST);
    }
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == goBack) {
			marketGui.changeView(false);
			
		}
		
	}
}
