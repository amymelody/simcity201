package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel. This holds the scroll panes for the customers
 * and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

	private static final int NEW_CUSTOMER_PANEL_WIDTH = 200;
	private static final int NEW_CUSTOMER_PANEL_HEIGHT = 30;

	public JScrollPane pane = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private List<JButton> list = new ArrayList<JButton>();
	private JButton addPersonB = new JButton("Add");

	private JTextField txtNewCustomer = new JTextField();
	private JCheckBox chkNewHungry = new JCheckBox("Hungry");

	private RestaurantPanel restPanel;
	private String type;
	private RestauranGUI restaurantGui;
	/**
	 * Constructor for ListPanel. Sets up all the gui
	 * 
	 * @param rp
	 *            reference to the restaurant panel
	 * @param type
	 *            indicates if this is for customers or waiters
	 */
	public ListPanel(RestauranGUI restaurantGui, RestaurantPanel rp, String type) {
		
		restPanel = rp;
		this.type = type;
		this.restaurantGui = restaurantGui;
		
		setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
		add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

		addPersonB.addActionListener(this);
		add(addPersonB);

		JPanel newCustomerPanel = new JPanel();
		newCustomerPanel.setLayout(new GridLayout(1, 2));
		newCustomerPanel.add(txtNewCustomer);
		newCustomerPanel.add(chkNewHungry);
		newCustomerPanel.setMaximumSize(new Dimension(NEW_CUSTOMER_PANEL_WIDTH,
				NEW_CUSTOMER_PANEL_HEIGHT));
		add(new JLabel("<html><br/></html>"));
		add(newCustomerPanel);
		add(new JLabel("<html><br/></html>"));

		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);
		add(pane);
	}

	/**
	 * Method from the ActionListener interface. Handles the event of the add
	 * button being pressed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPersonB) {
			// Chapter 2.19 describes showInputDialog()
			// addPerson(JOptionPane.showInputDialog("Please enter a name:"));
			if (txtNewCustomer.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null,
						"Please enter customer name!");
			} else {
				addPerson(txtNewCustomer.getText().trim(),
						chkNewHungry.isSelected());
				// clear
				txtNewCustomer.setText("");
				chkNewHungry.setSelected(false);
			}
			
			restaurantGui.customerWaitingArea.repaint();
			restaurantGui.waiterWaitingArea.repaint();
		} else {
			// Isn't the second for loop more beautiful?
			/*
			 * for (int i = 0; i < list.size(); i++) { JButton temp =
			 * list.get(i);
			 */
			for (JButton temp : list) {
				if (e.getSource() == temp)
					restPanel.showInfo(type, temp.getText());
			}
		}
	}

	/**
	 * If the add button is pressed, this function creates a spot for it in the
	 * scroll pane, and tells the restaurant panel to add a new person.
	 * 
	 * @param name
	 *            name of new person
	 */
	public void addPerson(String name, boolean hungry) {
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int) (paneSize.height / 7));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			list.add(button);
			view.add(button);
			restPanel.addPerson(type, name, hungry);// puts customer on list
			restPanel.showInfo(type, name);// puts hungry button on panel
			
			
			validate();
		}
	}
}
