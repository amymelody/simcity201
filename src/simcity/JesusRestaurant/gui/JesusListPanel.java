package simcity.JesusRestaurant.gui;

import simcity.cherysrestaurant.CustomerAgent;
import simcity.cherysrestaurant.HostAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class JesusListPanel extends JPanel implements ActionListener {
	//static declarations
	static final int FONTSIZE = 14;
	static final int ADDCPWIDTH = 450;

	public JScrollPane pane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private List<JButton> list = new ArrayList<JButton>();
	private JButton addPersonB;

	private JesusRestaurantPanel restPanel;
	private String type;

	private JPanel addName = new JPanel();
	private JTextField agentName = new JTextField();
	private JLabel nameLabel;

	private JPanel hungry = new JPanel();
	private JTextField custHungry = new JTextField();
	private JLabel hungryLabel;

	/**
	 * Constructor for ListPanel.  Sets up all the gui
	 *
	 * @param rp   reference to the restaurant panel
	 * @param type indicates if this is for customers or waiters
	 */
	public JesusListPanel(JesusRestaurantPanel rp, String type) {
		restPanel = rp;
		this.type = type;

		addPersonB = new JButton("Add " + type);
		nameLabel = new JLabel("Name: ");
		hungryLabel = new JLabel("Lvl");

		setBorder(BorderFactory.createTitledBorder(type));

		setSize(350, 350);
		setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));

		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);
		add(pane);


		addName.setMaximumSize(new Dimension((int) (ADDCPWIDTH * 0.7), FONTSIZE));
		addName.setLayout(new BorderLayout(5,5));
		hungry.setMaximumSize(new Dimension((int) (ADDCPWIDTH * 0.3), FONTSIZE));
		hungry.setLayout(new GridLayout(1,2,5,5));
		agentName.setMaximumSize(new Dimension(ADDCPWIDTH/2, FONTSIZE-2));
		agentName.addActionListener(this);
		custHungry.setMaximumSize(new Dimension((int) (ADDCPWIDTH/2 * 0.3), FONTSIZE-2));
		custHungry.addActionListener(this);
		addName.add(nameLabel, BorderLayout.WEST);
		addName.add(agentName, BorderLayout.CENTER);
		hungry.add(hungryLabel);
		hungry.add(custHungry);
		addPersonB.addActionListener(this);
		addName.add(addPersonB, BorderLayout.SOUTH);
		if(type.equals("Customer"))
			addName.add(hungry, BorderLayout.EAST);
		add(addName);
	}

	/**
	 * Method from the ActionListener interface.
	 * Handles the event of the add button being pressed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPersonB) {
			if(agentName.getText().length() <= 0) {
				JOptionPane.showMessageDialog(this, "Please enter a " + type + " name.");
			}
			else if(type.equals("Customer") && custHungry.getText().length() <= 0)
				JOptionPane.showMessageDialog(this, "Please enter a hunger level.");
			else if(type.equals("Customer") && custHungry.getText().length() > 0) {
				boolean number;
				try{
					if(Integer.parseInt(custHungry.getText()) > 0)
						number = true;
					else
						number = false;
				}
				catch(NumberFormatException ex) {
					number = false;
				}
				if(number) {
					int hungry = 5;
					if(type.equals("Customer"))
						hungry = Integer.parseInt(custHungry.getText());
					addPerson(agentName.getText(), hungry);
					agentName.setText("");
					custHungry.setText("");
				}
				else {
					JOptionPane.showMessageDialog(this, "Please enter a positive integer for the hunger level.");
					custHungry.setText("");
				}
			}
			else {
				int hungry = 0;
				addPerson(agentName.getText(), hungry);
				agentName.setText("");
				custHungry.setText("");
			}
		}
		else {
			// Isn't the second for loop more beautiful?
			/*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
			for (JButton temp:list){
				if (e.getSource() == temp)
					restPanel.showInfo(type, temp.getText());
			}
		}
	}

	/**
	 * If the add button is pressed, this function creates
	 * a spot for it in the scroll pane, and tells the restaurant panel
	 * to add a new person.
	 *
	 * @param name name of new person
	 */
	public void addPerson(String name, int hungry) {
		if (name != null) {
			JButton button = new JButton(name);
			if(type == "Customer")
				button.setBackground(Color.WHITE);
			else if(type == "Waiter")
				button.setBackground(Color.ORANGE);
			else if(type == "Market")
				button.setBackground(Color.GREEN);
			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int) (paneSize.height / 7));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			list.add(button);
			view.add(button);
			restPanel.addPerson(type, name, hungry);
			restPanel.showInfo(type, name);
			validate();
		}
	}

	boolean tableInputCheck(String a, String b, String c) {
		try {
			Integer.parseInt(a);
			Integer.parseInt(b);
			Integer.parseInt(c);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
}
