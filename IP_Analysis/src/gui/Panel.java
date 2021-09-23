package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logic.IpAddress;
import logic.IpMath;

public class Panel extends JPanel{
	
	private static final int IP_BITS_LENGTH = 32;
	private static final Font bitFont = new Font("Dialog", Font.PLAIN, 20);
	private static final Font dotFont = new Font("Dialog", Font.PLAIN, 30);
	private static final Font numberFont = new Font("Dialog", Font.PLAIN, 30);
	private static final Color WHITE = new Color(255, 255, 255);
	private ArrayList<JLabel> octets = new ArrayList<>();
	private ArrayList<JLabel> ipBits = new ArrayList<>();
	private ArrayList<JRadioButton> ipBitsButtons = new ArrayList<>();
	private JSpinner mask;
	private JLabel address;
	private JLabel range;
	private JLabel broadcast;
	private JLabel hosts;

	public Panel() {
		
		this.setLayout(new BorderLayout());
		
		//-------------UPPER PANEL--------------
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new GridBagLayout());
		this.addBits(upperPanel); //adds most of the components
		
		
		this.add(upperPanel, BorderLayout.NORTH);
		
		
		//------------LOWER PANEL---------------
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridBagLayout());
		
		setInitialLabels(lowerPanel);
		
		this.add(lowerPanel, BorderLayout.CENTER);
		
		
	}
	private void addLabel(String leftLabelText, JLabel rightLabel, JPanel panel, int y) { //add regex to ip fields
		
		JLabel leftLab = new JLabel(leftLabelText, SwingConstants.RIGHT);
		leftLab.setFont(numberFont);
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = y;
		c.gridx = 0;
		panel.add(leftLab, c);
		c.gridx = 1;
		panel.add(rightLabel, c);
		rightLabel.setFont(numberFont);
		rightLabel.setForeground(Color.gray);
	}
	
	private void addBits(JPanel panel) {
		
		GridBagConstraints c = new GridBagConstraints();
		
		int gridIndex = 0; //To keep track of the panel component addtions
		int bitIndex = 0; // '''''''''''''''''''' real bits. Useful to get the real index of each bit for further calculations.
		
		for(int i = 0; i < 4; i ++) {
			
			JPanel subPanel = new JPanel();
			subPanel.setLayout(new GridBagLayout());
			
			for(int j = 0; j < 8; j++) {
			
				JLabel bit = new JLabel("0");
				bit.setFont(bitFont);
				ipBits.add(bit);
				
			
				JRadioButton radioButton = new JRadioButton();
				ipBitsButtons.add(radioButton);
				radioButton.addActionListener(new RadioButtonListener(bit));
			
				c.gridx = gridIndex; //maybe put all this code in a method
				c.gridy = 0;
				subPanel.add(bit, c);
				c.gridy = 1;
				subPanel.add(radioButton, c);
			
				gridIndex++;
			}
			
			//Dot addition
			
			JLabel dot = new JLabel();
			
			if(i != 3){
			
				dot.setText(".");
				
			}else {
				
				dot.setText(" "); //To organize last octet
				
			}
			
				dot.setFont(dotFont);
				dot.setForeground(Color.RED);
				c.gridx = gridIndex;
				c.gridy = 0;
				subPanel.add(dot, c);
				
			//SubPanel '' 
				c.gridx = i;
				c.gridy = 1;
				panel.add(subPanel, c);
			
			//Upper Number ''
				
				JPanel numberPanel = new JPanel();
				JLabel label = new JLabel("0");
				octets.add(label);
				label.setFont(numberFont);
				numberPanel.add(label);
				c.gridy = 0;
				panel.add(numberPanel, c);
			
			
			gridIndex++;
		}
		
		JLabel maskBar = new JLabel("/");
		maskBar.setFont(numberFont);
		c.gridx = 4;
		c.gridy = 0;
		panel.add(maskBar, c);
		
		mask = new JSpinner(new SpinnerNumberModel(24, 1, 30, 1));
		mask.addChangeListener(new SpinnerListener());
		
		JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor)mask.getEditor();
		JFormattedTextField field = editor.getTextField();
		field.setHorizontalAlignment(JTextField.CENTER);
		//field.setEditable(false);
		field.setEnabled(false);
		mask.setFont(numberFont);
		mask.setValue(24);
		c.gridx = 6;
		c.gridy = 0;
		panel.add(mask, c);
		
		paintMask((int)mask.getValue()); //Temporary. maybe put it inside a method which purpose is to get the initial position or appearence for every component.
	}
	
	private class RadioButtonListener implements ActionListener{
		
		private final JLabel bit;
		
		public RadioButtonListener(JLabel bit) {
			
			this.bit = bit;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JRadioButton button = (JRadioButton)e.getSource();
			
			String text = (button.isSelected()) ? "1" : "0";
			
			bit.setText(text);
			
			update();
			
		}
		
	}
	
	private class SpinnerListener implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
		
			update();
			
		}
	}
	
	private void update() { //This method has to have every calculation but maybe it can be done into a switch, for every specific calculation/update.
		
		int ip = 0;
		
		for(int i = 0; i < IP_BITS_LENGTH; i++) { 
			
			JLabel label = ipBits.get(i);
			
			if(label.getText().equals("1")) { //checks for every set bit and creates an integer of 32 bits representing the ip
				
				int n = 1 << (ipBits.size() - 1) - i;
				
				ip += n;
			}
		}
		
		ArrayList<Integer> ipOctets = IpMath.convertRawIp(ip);
		
		for(int i = 0; i < octets.size(); i++) { 
			
			octets.get(i).setText(String.valueOf(ipOctets.get(i)));
			
		}
		
		updateLabelCalculations(ipOctets);
		paintMask((int)mask.getValue()); 
		
	}
	
	private void paintMask(int mask) {
		
		for(int i = 0; i < IP_BITS_LENGTH; i++) {
			
			JLabel bit = ipBits.get(i);
			
			Color c = null;
			
			if(i < mask) 
			
				c = Color.GRAY;
			else 
				
				c = Color.BLACK;
				
			
			bit.setForeground(c);
			
		}
		
	}
	
	private void updateLabelCalculations(ArrayList<Integer> octets) {
		
		int m = (int)mask.getValue();
		
		IpAddress ip = new IpAddress(octets, m);
		
		address.setText(IpMath.getAddress(ip, IpMath.NETWORK_ADDRESS).toText());
		
		IpAddress firstAddress = IpMath.getAddress(ip, IpMath.FIRST_ASSIGNABLE_ADDRESS);
		IpAddress lastAddress = IpMath.getAddress(ip, IpMath.LAST_ASSIGNABLE_ADDRESS);
		
		range.setText(firstAddress.toText() +" - "+lastAddress.toText());
		
		broadcast.setText(IpMath.getAddress(ip, IpMath.BROADCAST_ADDRESS).toText());
		
		hosts.setText(String.valueOf(IpMath.getMaxHosts(ip)));
		
	}
	
	private void setInitialLabels(JPanel panel) {
		
		this.addLabel("Address: ",  address = new JLabel("192.168.0.0", SwingConstants.LEFT), panel, 0);
		this.addLabel("IPs Range: ", range = new JLabel("192.168.0.1 - 192.168.0.254", SwingConstants.LEFT), panel, 1);
		this.addLabel("Broadcast: ", broadcast = new JLabel("192.168.0.255", SwingConstants.LEFT), panel, 2);
		this.addLabel("Max. Hosts: ", hosts = new JLabel("254", SwingConstants.LEFT), panel, 3);
		
		octets.get(0).setText("192");
		octets.get(1).setText("168");
		octets.get(2).setText("0");
		octets.get(3).setText("0");
		
		ipBits.get(0).setText("1");
		ipBits.get(1).setText("1");
		ipBits.get(8).setText("1");
		ipBits.get(10).setText("1");
		ipBits.get(12).setText("1");
		
		ipBitsButtons.get(0).setSelected(true);
		ipBitsButtons.get(1).setSelected(true);
		ipBitsButtons.get(8).setSelected(true);
		ipBitsButtons.get(10).setSelected(true);
		ipBitsButtons.get(12).setSelected(true);
		
	}
}
