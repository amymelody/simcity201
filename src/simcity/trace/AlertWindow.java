package simcity.trace;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A quick demo of some squares moving around and logging when they change color zones.  You can also
 * click the mouse in either color to generate a different kind of message. <br><br>
 * 
 * TUTORIAL: You can search through this file for the word "TUTORIAL" to see relevant pieces of 
 * code with instructions on how to use the {@link AlertLog} and {@link TracePanel}.
 * 
 * @author Keith DeRuiter
 *
 */
@SuppressWarnings("serial")
public class AlertWindow extends JFrame {

	ControlPanel controlPanel;
	
	//============================ TUTORIAL ==========================================
	//You declare a TracePanel just like any other variable, and it extends JPanel, so you use
	//it in the same way.
	TracePanel tracePanel;
	//================================================================================
	
	private final int WINDOWX = 1100;
	private final int WINDOWY = 200;
	private final int BUFFERTOP = 530;
	private final int BUFFERSIDE = 15;


	public AlertWindow() {
		this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
		this.setMinimumSize(new Dimension(WINDOWX, WINDOWY));
		setBounds(BUFFERSIDE, BUFFERTOP, WINDOWX, WINDOWY);
		
		this.tracePanel = new TracePanel();
		this.controlPanel = new ControlPanel(tracePanel);
		tracePanel.setPreferredSize(new Dimension(800, 300));
		
		this.setLayout(new BorderLayout());
		this.add(controlPanel, BorderLayout.WEST);
		this.add(tracePanel, BorderLayout.CENTER);
	}

	public void start() {
		
		//============================ TUTORIAL ==========================================
		//We have to tell the trace panel what kinds of messages to display.  Here we say to display
		//normal MESSAGEs tagged with the PERSON tag (the type I use in this demo).  You can also 
		//hide messages of certain Levels, or messages tagged a certain way (eg. Don't show anything
		//that says it is from a MARKET_EMPLOYEE).  Here we decide to hide debug messages and things
		//tagged as AlertTag.BUS_STOP
		tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);		//THESE PRINT RED, WARNINGS PRINT YELLOW on a black background... :/
		tracePanel.showAlertsWithLevel(AlertLevel.INFO);		//THESE PRINT BLUE
		tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);		//THESE SHOULD BE THE MOST COMMON AND PRINT BLACK
		
		tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
		
		tracePanel.hideAlertsWithTag(AlertTag.PERSON);
		tracePanel.hideAlertsWithTag(AlertTag.BANK_CUSTOMER);
		
		tracePanel.hideAlertsWithTag(AlertTag.BUS_STOP);
		//
		//You will have to add your own AlertTag types to the AlertTag enum for your project.
		//There are two helper methods that enable all AlertLevels and all AlertTags that you can use
		//if you don't want to manually enable them all.  IF NOTHING APPEARS, CHECK THAT YOU HAVE 
		//THE RIGHT LEVELS AND TAGS TURNED ON.  That will likely be the most common problem.
		//================================================================================
	
		//============================ TUTORIAL ==========================================
		//We need to let the log know that the trace panel wants to listen for alert messages.
		//This is how the trace panel will be automatically told when anyone logs a message, and 
		//will store or display it as appropriate.  Note how we are accessing the AlertLog: using
		//the fact that it is a globally accessible singleton (there is only ever ONE log, and 
		//everybody accesses the same one).
		AlertLog.getInstance().addAlertListener(tracePanel);
		//================================================================================		
	}

	//CONTROL PANEL CLASS
	private class ControlPanel extends JPanel {
		TracePanel tp;	//Hack so I can easily call showAlertsWithLevel for this demo.
		
		JButton messagesButton;		
		JButton errorButton;		
		JButton bankCustTagButton;
		JButton personTagButton;
		JButton joshRestaurantTagButton;
		
		public ControlPanel(final TracePanel tracePanel) {
			this.tp = tracePanel;
			messagesButton = new JButton("Hide Level: MESSAGE");
			errorButton = new JButton("Show Level: ERROR");
			personTagButton = new JButton("Show Tag: PERSON");
			bankCustTagButton = new JButton("Show Tag: BANK_CUSTOMER");
			joshRestaurantTagButton = new JButton("Show Tag: JOSH_RESTAURANT");
			
			
			messagesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (messagesButton.getText().equals("Show Level: MESSAGE")) {
						tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
						messagesButton.setText("Hide Level: MESSAGE");
					} else if (messagesButton.getText().equals("Hide Level: MESSAGE")) {
						tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
						messagesButton.setText("Show Level: MESSAGE");
					}
				}
			});
			errorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (errorButton.getText().equals("Show Level: ERROR")) {
						tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
						errorButton.setText("Hide Level: ERROR");
					} else if (errorButton.getText().equals("Hide Level: ERROR")) {
						tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
						errorButton.setText("Show Level: ERROR");
					}
				}
			});
			personTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (personTagButton.getText().equals("Show Tag: PERSON")) {
						tracePanel.showAlertsWithTag(AlertTag.PERSON);
						personTagButton.setText("Hide Tag: PERSON");
					} else if (personTagButton.getText().equals("Hide Tag: PERSON")) {
						tracePanel.hideAlertsWithTag(AlertTag.PERSON);
						personTagButton.setText("Show Tag: PERSON");
					}
				}
			});
			bankCustTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (bankCustTagButton.getText().equals("Show Tag: BANK_CUSTOMER")) {
						tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
						bankCustTagButton.setText("Hide Tag: BANK_CUSTOMER");
					} else if (bankCustTagButton.getText().equals("Hide Tag: BANK_CUSTOMER")) {
						tracePanel.hideAlertsWithTag(AlertTag.BANK_CUSTOMER);
						bankCustTagButton.setText("Show Tag: BANK_CUSTOMER");
					}
				}
			});
			joshRestaurantTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (joshRestaurantTagButton.getText().equals("Show Tag: JOSH_RESTAURANT")) {
						tracePanel.showAlertsWithTag(AlertTag.JOSH_RESTAURANT);
						joshRestaurantTagButton.setText("Hide Tag: JOSH_RESTAURANT");
					} else if (joshRestaurantTagButton.getText().equals("Hide Tag: JOSH_RESTAURANT")) {
						tracePanel.hideAlertsWithTag(AlertTag.JOSH_RESTAURANT);
						joshRestaurantTagButton.setText("Show Tag: JOSH_RESTAURANT");
					}
				}
			});
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(messagesButton);
			this.add(errorButton);
			this.add(personTagButton);
			this.add(bankCustTagButton);
			this.add(joshRestaurantTagButton);
			this.setMinimumSize(new Dimension(50, 600));
		}
	}

}