package restaurant.gui;

import restaurant.CustomerAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import restaurant.HostAgent;
import restaurant.Table;

/**
 * Main GUI class. Contains the main frame and subsequent panels
 */
public class ControlRestaurantPanel extends JPanel implements ActionListener {
	/*
	 * The GUI has two frames, the control frame (in variable gui) and the
	 * animation frame, (in variable animationFrame within gui)
	 */
	// JFrame animationFrame = new JFrame("Restaurant Animation");

	/*
	 * restPanel holds 2 panels 1) the staff listing, menu, and lists of current
	 * customers all constructed in RestaurantPanel() 2) the infoPanel about the
	 * clicked Customer (created just below)
	 */
	private RestaurantPanel restPanel;

	/* infoPanel holds information about the clicked customer, if there is one */
	private JPanel infoPanel;
	private JLabel infoLabel; // part of infoPanel
	public JCheckBox stateCB;// part of infoLabel

	private Object currentPerson;/*
								 * Holds the agent that the info is about. Seems
								 * like a hack
								 */

	private static final int WINDOW_WIDTH = 850;
	private static final int WINDOW_HEIGHT = 600;

	private JButton btnAddTable = new JButton("Add Table");
	private JButton btnAddWaiter = new JButton("Add Waiter");
	private JButton btnUpdateTable = new JButton("Update Table");
	private JButton btnPause = new JButton("Pause");

	private JTextField txtTableSize = new JTextField(3);
	private JTextField txtTableXPos = new JTextField(3);
	private JTextField txtTableYPos = new JTextField(3);

	public HostAgent host = new HostAgent("Sarah");
	private JComboBox cboTablesName = new JComboBox();

	
	private JComboBox cboWaitersName = new JComboBox();
	private JButton btnWantToBeOnBreak = new JButton("Want to be On Break");
	private JButton btnBackToWork = new JButton("Back to Work");
	
	RestauranGUI gui;
	/**
	 * Constructor for RestaurantGui class. Sets up all the gui components.
	 */
	public ControlRestaurantPanel(RestauranGUI gui) {
		this.gui = gui;
		// animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
		// animationFrame.setVisible(true);
		// animationFrame.add(animationPanel);

		setBounds(50, 50, Configuration.WINDOWX, Configuration.WINDOWY);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		restPanel = new RestaurantPanel(gui, host);
//		customerWaitingArea.setRestaurantPanel(restPanel);
		
		
		
		Dimension restDim = new Dimension(Configuration.WINDOWX, 200);
		restPanel.setPreferredSize(restDim);
		restPanel.setMinimumSize(restDim);
		restPanel.setMaximumSize(restDim);
		add(restPanel);

		
		
		// Now, setup the info panel
		Dimension infoDim = new Dimension(Configuration.WINDOWX, 50);
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(infoDim);
		infoPanel.setMinimumSize(infoDim);
		infoPanel.setMaximumSize(infoDim);
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

		stateCB = new JCheckBox();
		stateCB.setVisible(false);
		stateCB.addActionListener(this);

		infoPanel.setLayout(new GridLayout(1, 2, 30, 0));

		infoLabel = new JLabel();
		infoLabel
				.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
		infoPanel.add(infoLabel);
		infoPanel.add(stateCB);
		add(infoPanel);

		btnAddTable.addActionListener(this);
		btnAddWaiter.addActionListener(this);
		btnUpdateTable.addActionListener(this);
		btnPause.addActionListener(this);

		JPanel tableConfigPanel = new JPanel();
		tableConfigPanel.setBorder(BorderFactory
				.createTitledBorder("Configuration"));
		txtTableSize.setText("" + HostGui.sizeTable);

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout((Container) tablePanel,
				BoxLayout.X_AXIS));
		tablePanel.add(new JLabel("Table size: "));
		tablePanel.add(txtTableSize);

		tablePanel.add(new JLabel(" Table #: "));
		MutableComboBoxModel model = (MutableComboBoxModel) cboTablesName
				.getModel();
		for (int i = 1; i <= host.NTABLES; i++) {
			model.addElement(String.valueOf(i));
		}
		cboTablesName.addActionListener(this);
		tablePanel.add(cboTablesName);
		
		JPanel waiterPanel = new JPanel();
		waiterPanel.setLayout(new BoxLayout((Container) waiterPanel,
				BoxLayout.X_AXIS));
		waiterPanel.add(new JLabel(" Waiter: "));
		MutableComboBoxModel waiterModel = (MutableComboBoxModel) cboWaitersName
				.getModel();
		for (int i = 0; i < host.NWAITERS; i++) {
			waiterModel.addElement(host.getWaiters().get(i).getIndex());
		}
		cboWaitersName.addActionListener(this);
		waiterPanel.add(cboWaitersName);
		btnWantToBeOnBreak.addActionListener(this);
		btnBackToWork.addActionListener(this);
		
		txtTableXPos.setText("" + host.getTablePosition(1).x);
		txtTableYPos.setText("" + host.getTablePosition(1).y);

		tablePanel.add(new JLabel("  "));
		tablePanel.add(new JLabel("Table position (x,y): "));
		tablePanel.add(txtTableXPos);
		tablePanel.add(txtTableYPos);

		tableConfigPanel.add(tablePanel);
		tableConfigPanel.add(waiterPanel);
		tableConfigPanel.add(btnUpdateTable);
		tableConfigPanel.add(btnAddTable);
		tableConfigPanel.add(btnAddWaiter);
		tableConfigPanel.add(btnWantToBeOnBreak);
		tableConfigPanel.add(btnBackToWork);
		add(tableConfigPanel);

		JPanel actionConfigPanel = new JPanel();
		actionConfigPanel.setBorder(BorderFactory.createTitledBorder("Action"));
		actionConfigPanel.add(btnPause);
		add(actionConfigPanel);

		host.setRestaurantGUI(this);
	}
	
	/**
	 * updateInfoPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 * 
	 * @param person
	 *            customer (or waiter) object
	 */
	public void updateInfoPanel(Object person) {
		stateCB.setVisible(true);
		currentPerson = person;

		if (person instanceof CustomerAgent) {
			CustomerAgent customer = (CustomerAgent) person;
			stateCB.setText("Hungry?");
			// Should checkmark be there?
			stateCB.setSelected(customer.getGui().isHungry());
			// Is customer hungry? Hack. Should ask customerGui
			stateCB.setEnabled(!customer.getGui().isHungry());
			// Hack. Should ask customerGui
			infoLabel.setText("<html><pre>     Name: " + customer.getName()
					+ " </pre></html>");
		}
		infoPanel.validate();
	}

	/**
	 * Action listener method that reacts to the checkbox being clicked; If it's
	 * the customer's checkbox, it will make him hungry For v3, it will propose
	 * a break for the waiter.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == stateCB) {
			if (currentPerson instanceof CustomerAgent) {
				CustomerAgent c = (CustomerAgent) currentPerson;
				c.getGui().setHungry();
				stateCB.setEnabled(false);
				
//				customerWaitingArea.repaint();
//				customerWaitingArea.invalidate();
//				customerWaitingArea.validate();
			}
		} else if (e.getSource() == btnAddTable) {
			HostAgent host = restPanel.getHost();
			host.increaseTable();
			MutableComboBoxModel model = (MutableComboBoxModel) cboTablesName
					.getModel();
			model.addElement(host.NTABLES);
			
//			customerWaitingArea.repaint();
//			customerWaitingArea.invalidate();
//			customerWaitingArea.validate();
			
		} else if (e.getSource() == btnAddWaiter) {
			HostAgent host = restPanel.getHost();
			host.increaseWaiter();
			MutableComboBoxModel model = (MutableComboBoxModel) cboWaitersName
					.getModel();
			model.addElement(host.NWAITERS);
			
//			customerWaitingArea.repaint();
//			customerWaitingArea.invalidate();
//			customerWaitingArea.validate();
			
		} else if (e.getSource() == btnUpdateTable) {
			int xTable = 0;
			int yTable = 0;
			try {
				xTable = Integer.parseInt(txtTableXPos.getText());
			} catch (Exception ee) {
				JOptionPane.showMessageDialog(null,
						"Please check table position!");
				return;
			}
			try {
				yTable = Integer.parseInt(txtTableYPos.getText());
			} catch (Exception ee) {
				JOptionPane.showMessageDialog(null,
						"Please check table position!");
				return;
			}
			Table table = host.getTable(Integer.parseInt(cboTablesName
					.getSelectedItem().toString()));
			table.setLocation(new Point(xTable, yTable));
			// HostGui.yTable = yTable;
		} else if (e.getSource() == btnPause) {
			if (btnPause.getText().equals("Pause")) {
				HostAgent host = restPanel.getHost();
				host.pause();
				btnPause.setText("Restart");
			} else {
				HostAgent host = restPanel.getHost();
				host.startThread();
				btnPause.setText("Pause");
			}
		} else if (e.getSource() == cboTablesName) {
			txtTableXPos.setText(""
					+ host.getTablePosition(Integer.parseInt(cboTablesName
							.getSelectedItem().toString())).x);
			txtTableYPos.setText(""
					+ host.getTablePosition(Integer.parseInt(cboTablesName
							.getSelectedItem().toString())).y);
		}else if (e.getSource() == btnWantToBeOnBreak) {
			int idex = Integer.parseInt(cboWaitersName.getSelectedItem().toString());
			idex--;
			host.getWaiters().get(idex).wantingToGoOnBreak = true;
		}else if (e.getSource() == btnBackToWork) {
			int idex = Integer.parseInt(cboWaitersName.getSelectedItem().toString());
			idex--;
			host.getWaiters().get(idex).backToWork();
			
		}
		gui.waiterWaitingArea.repaint();
	}

	/**
	 * Message sent from a customer gui to enable that customer's "I'm hungry"
	 * checkbox.
	 * 
	 * @param c
	 *            reference to the customer
	 */
	public void setCustomerEnabled(CustomerAgent c) {
		if (currentPerson instanceof CustomerAgent) {
			CustomerAgent cust = (CustomerAgent) currentPerson;
			if (c.equals(cust)) {
				stateCB.setEnabled(true);
				stateCB.setSelected(false);
			}
		}
	}

	
	/**
	 * @return the restPanel
	 */
	public RestaurantPanel getRestPanel() {
		return restPanel;
	}
}
