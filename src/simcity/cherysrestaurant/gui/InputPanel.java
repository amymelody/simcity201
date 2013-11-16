package simcity.cherysrestaurant.gui;

import simcity.cherysrestaurant.CustomerAgent;
import simcity.cherysrestaurant.HostAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of ListPanel.
 * Holds the name text field and checkbox for customers and waiters
 */
public class InputPanel extends JPanel implements ActionListener
{
    private ListPanel listPanel;
    private String type;
    private int textLength;
    private String originalText;
    JTextField name;
    JCheckBox check;

    /**
     * Constructor for InputPanel. Sets up all the gui
     * @param lp reference to the list panel
     * @param t  indicates if this is for customers or waiters
     */
    public InputPanel(ListPanel lp, String t)
    {
        listPanel = lp;
        type = t;
        textLength = 10;
        name = new JTextField("", textLength);
        name.addActionListener(this);
        check = new JCheckBox("", false);
    	check.setEnabled(true);
        check.addActionListener(this);
        setLayout(new FlowLayout());

        if(type.equals("Waiter"))
        {
        	originalText = "Break?";
        }
        if(type.equals("Customer"))
        {
        	originalText = "Hungry?";
        }
    	check.setText(originalText);
        
        add(name);
        add(check);
    }
    /**
     * Action listener method that reacts to the checkbox being clicked
     */
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == check)
        {
        	if(check.isSelected())
        	{
        		check.setEnabled(false);
        	}
        	else
    		{
    			check.setText(originalText);
    		}
        	listPanel.updatePerson(name.getText(), check.isSelected());
        }
    }
    /**
     * Disables the ability to edit the textfield once an agent has
     * already been added
     */
    public void disableText()
    {
    	name.setEditable(false);
    }
    /**
     * Enables the checkbox when a customer is not already hungry
     */
    public void enableCheck(String t)
    {
    	check.setEnabled(true);
    	if(t.equals("Customer"))
    	{
    		check.setSelected(false);
    	}
    	else if(t.equals("Waiter"))
    	{
    		if(check.isSelected())
    		{
    			check.setText("back-to-work");
    		}
    	}
    }
    /**
     * Resets the entry panel after it has been used
     */
    public void resetPanel()
    {
        name.setText("");
    	check.setSelected(false);
    	check.setEnabled(true);
    }
    
    public String getName()
    {
    	return name.getText();
    }
    public void setName(String n)
    {
    	name.setText(n);
    }
    public boolean getCheck()
    {
    	return check.isSelected();
    }
    /**
     * Sets the checkbox to the given value and disables it if it's
     * checked
     * @param tf new value of the checkbox
     */
    public void setCheck(boolean tf)
    {
    	check.setSelected(tf);
    	if(check.isSelected())
    	{
            check.setEnabled(false);
    	}
    }
    public String getType()
    {
    	return type;
    }
}