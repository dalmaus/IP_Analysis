package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import logic.IpMath;

public class Panel extends JPanel{
	
	private JLabel label;
	private JRadioButton radioButton;
	private static final int IP_BITS_LENGTH = 32;
	private static final Font bitFont = new Font("Dialog", Font.PLAIN, 20);
	private static final Font dotFont = new Font("Dialog", Font.PLAIN, 30);
	private static final Font numberFont = new Font("Dialog", Font.PLAIN, 30);
	private static final Color WHITE = new Color(255, 255, 255);
	private ArrayList<JLabel> octets = new ArrayList<>();
	private ArrayList<JLabel> ipBits = new ArrayList<>();
	private ArrayList<JRadioButton> ipBitsButtons = new ArrayList<>();
	private JTextField mask;

	public Panel() {
		
		this.setLayout(new BorderLayout());
		
		//-------------NORTH PANEL--------------
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridBagLayout());
		this.addBits(northPanel);
		
		
		this.add(northPanel, BorderLayout.NORTH);
		
		
		//------------CENTER PANEL---------------
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		
		this.addLabel("Address: ",  new JLabel("192.168.0.0", SwingConstants.LEFT), centerPanel, 0);
		this.addLabel("IPs Range: ", new JLabel("192.168.0.1 - 192.168.0.254          ", SwingConstants.LEFT), centerPanel, 1);
		this.addLabel("Broadcast: ", new JLabel("192.168.0.255", SwingConstants.LEFT), centerPanel, 2);
		this.addLabel("Max. Hosts: ",  new JLabel("254", SwingConstants.LEFT), centerPanel, 3);
		
		
		this.add(centerPanel, BorderLayout.CENTER);
		
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
	
	
	/*private void addBits(JPanel panel) {
		
		GridBagConstraints c = new GridBagConstraints();
		
		int gridIndex = 0; //To keep track of the panel component addtions
		int bitIndex = 0; // '''''''''''''''''''' real bits. Useful to get the real index of each bit for further calculations.
		
		for(int i = 0; i < 4; i ++) {
			
			c.gridx = gridIndex;
			c.gridy = 0;
			JLabel number = new JLabel("0");
			number.setFont(defaultFont);
			
			panel.add(number, c);
			
			for(int j = 0; j < 8; j++) {
			
				JLabel bit = new JLabel("0");
				bit.setFont(defaultFont);
			
				JRadioButton radioButton = new JRadioButton();
			
				radioButton.addActionListener(new RadioButtonListener(bit, bitIndex++));
			
				c.gridx = gridIndex;
				c.gridy = 1;
				panel.add(bit, c);
				c.gridy = 2;
				panel.add(radioButton, c);
			
				gridIndex++;
			}
			
			if(i != 3) { //To avoid the last dot
			
				JLabel dot = new JLabel(".");
				dot.setFont(dotFont);
				c.gridx = gridIndex;
				c.gridy = 1;
			
				panel.add(dot, c);
			}
			
			gridIndex++;
		}
	}*/
	
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
				radioButton.addActionListener(new RadioButtonListener(bit, bitIndex++));
			
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
		
		mask = new JTextField("24", 2);
		mask.setHorizontalAlignment(JTextField.CENTER);
		mask.setFont(numberFont);
		c.gridx = 5;
		c.gridy = 0;
		panel.add(mask, c);
		
	}
	
	private class RadioButtonListener implements ActionListener{
		
		private final JLabel bit;
		private final int index; //Maybe this is useless as probably we are going to add them to an array.
		
		public RadioButtonListener(JLabel bit, int index) {
			
			this.bit = bit;
			this.index = index;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JRadioButton button = (JRadioButton)e.getSource();
			
			String text = (button.isSelected()) ? "1" : "0";
			
			bit.setText(text);
			
			update();
			
		}
		
	}
	
	private void update() {
		
		int ip = 0;
		
		for(int i = 0; i < IP_BITS_LENGTH; i++) {
			
			JLabel label = ipBits.get(i);
			
			if(label.getText().equals("1")) {
				
				int n = 1 << (ipBits.size() - 1) - i;
				
				ip += n;
			}
		}
		
		ArrayList<Integer> ipOctets = IpMath.convertRawIp(ip);
		
		for(int i = 0; i < octets.size(); i++) {
			
			octets.get(i).setText(String.valueOf(ipOctets.get(i)));
			
		}
	}
}
