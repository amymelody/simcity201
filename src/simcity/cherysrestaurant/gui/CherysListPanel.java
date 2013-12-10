package simcity.cherysrestaurant.gui; 

import simcity.cherysrestaurant.CherysCustomerRole;
import simcity.cherysrestaurant.CherysHostRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel. Holds the scroll panes for the customers
 * and waiters
 */
public class CherysListPanel extends JPanel implements ActionListener
{
    public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<CherysInputPanel> list = new ArrayList<CherysInputPanel>();
    private JButton addPerson = new JButton("Add");

    private CherysRestaurantInputPanel restPanel;
    private String type;
    private CherysInputPanel inPanel;

    /**
     * Constructor for ListPanel. Sets up all the gui.
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public CherysListPanel(CherysRestaurantInputPanel rp, String type) //* called from the RestaurantPanel
    {
        restPanel = rp;
        this.type = type;
        inPanel = new CherysInputPanel(this, type);

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        addPerson.addActionListener(this);
        add(addPerson);
        add(inPanel);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }
//
    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed.
     */
    public void actionPerformed(ActionEvent e)
    {
    	if(e.getSource() == addPerson)
    	{
    		addPerson();
    	}
    	
//        if (e.getSource() == custPanel1)
//        {
//        	if(custPanel1.getName() == null)
//        	{
//        		custPanel1.enableHungry(false);
//        	}
//        	else
//        	{
//        		custPanel1.enableHungry(true);
//                restPanel.addPerson(type, custPanel1.getName());//puts customer on list
//                restPanel.showInfo(type, custPanel1.getName());//puts hungry button on panel
//        	}
//        	// Chapter 2.19 describes showInputDialog()
//            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
//        }
//        else {
//        	// Isn't the second for loop more beautiful?
//            /*for (int i = 0; i < list.size(); i++) {
//                JButton temp = list.get(i);*/
//        	for (JButton temp:list){
//                if (e.getSource() == temp)
//                    restPanel.showInfo(type, temp.getText());
//            }
//        }
    }

    /**
     * Creates a spot for a new person in the scroll pane and tells the
     * restaurant panel to add them.
     */
      public void addPerson()
      {
	      if (inPanel.getName() != null)
	      {
	    	  CherysInputPanel panel = new CherysInputPanel(this, type);
	    	  panel.setName(inPanel.getName());
	    	  panel.setCheck(inPanel.getCheck());
//	    	  Dimension paneSize = pane.getSize();
//	    	  Dimension panelSize = new Dimension(paneSize.width - 20, (int) (paneSize.height / 7));
//	    	  panel.setPreferredSize(panelSize);
//	    	  panel.setMinimumSize(panelSize);
//	    	  panel.setMaximumSize(panelSize);
	    	  list.add(panel);
	    	  view.add(panel);
	          restPanel.addPerson(panel.getType(), panel.getName(), panel.getCheck());//puts customer or waiter on list
              panel.disableText();
              inPanel.resetPanel();
              validate();
          }
      }
      /**
       * 
	   * Updates a customer on their new state when their associated
	   * checkbox is clicked.
       * @param name name of the agent
       * @param tf new value of the agent's checkbox
       */
      public void updatePerson(String name, boolean tf) //* called from InputPanel.actionPerformed
      {
    	  restPanel.updateInfo(type, name, tf);
      }
      /**
       * Finds the customer with the given name and enables their checkbox.
       * @param n name of an agent
       */
      public void enableCheck(String type, String n) //* called from RestaurantPanel.enableCheck
      {
          for(int i = 0; i < list.size(); i++)
          {
        	  if(n.equals(list.get(i).getName()))
        	  {
        		  list.get(i).enableCheck(type);
        	  }
          }
      }
      /**
       * Finds the waiter with the given name and sets their checkbox to
       * the given value.
       * @param n name of an agent
       * @param tf value to set the checkbox to
       */
      public void setCheck(String n, boolean tf) //* called from RestaurantPanel.setCheck
      {
          for(int i = 0; i < list.size(); i++)
          {
        	  if(n.equals(list.get(i).getName()))
        	  {
        		  list.get(i).setCheck(tf);
        	  }
          }
      }
}
