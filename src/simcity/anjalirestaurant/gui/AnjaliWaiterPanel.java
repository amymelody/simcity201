package simcity.anjalirestaurant.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class AnjaliWaiterPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addWaiterB = new JButton("Add");

    private AnjaliRestaurantInputPanel restPanel;
    private String type;
    private JTextField waiterName;
    //private JCheckBox isHungry;
   
    
    
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public AnjaliWaiterPanel(AnjaliRestaurantInputPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        addWaiterB.addActionListener(this);
        add(addWaiterB);
        
        waiterName = new JTextField(8);
    	view.add(waiterName);
    	//isHungry = new JCheckBox("Hungry", false);
    	//rp.getGui();
    	//stateCB = true;
    	
    	//view.add(isHungry);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
       pane.setPreferredSize(new Dimension(150,300));
        pane.setViewportView(view);
        add(pane);
    }
    
   
    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addWaiterB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	
        	if(waiterName.getText().length() != 0){
        	addWaiter(waiterName.getText());
        	waiterName.setText(null);
        	}
          
        
        }
        else {
        	// Isn't the second for loop more beautiful?
            //for (int i = 0; i < list.size(); i++) {
                //JButton temp = list.get(i);*/
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
    public void addWaiter(String name) {
     
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
           // view.add(name);
            //restPanel.addWaiter(type, name);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
            
        
    }
}