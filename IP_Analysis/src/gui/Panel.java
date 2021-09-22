package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Panel extends JPanel{
	
	private JLabel label;
	private JRadioButton radioButton;
	private static final int ipBitsLength = 32;
	private static final Font bitFont = new Font("Dialog", Font.PLAIN, 20);
	private static final Font dotFont = new Font("Dialog", Font.PLAIN, 30);
	private static final Font numberFont = new Font("Dialog", Font.PLAIN, 30);

	public Panel() {
		
		
		
		this.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridBagLayout());
		
		/*northPanel.add(label = new JLabel("0"));
		northPanel.add(radioButton = new JRadioButton());*/
		this.addBits(northPanel);
		
		this.add(northPanel, BorderLayout.NORTH);
		
		
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
			
			/*c.gridx = gridIndex;
			c.gridy = 0;
			JLabel number = new JLabel("0");
			number.setFont(defaultFont);
			
			panel.add(number, c);*/
			
			JPanel subPanel = new JPanel();
			subPanel.setLayout(new GridBagLayout());
			
			for(int j = 0; j < 8; j++) {
			
				JLabel bit = new JLabel("0");
				bit.setFont(bitFont);
			
				JRadioButton radioButton = new JRadioButton();
			
				radioButton.addActionListener(new RadioButtonListener(bit, bitIndex++));
			
				c.gridx = gridIndex;
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
				
				dot.setText(" "); //To not unorganize last octet
				
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
				JLabel label = new JLabel("255");
				label.setFont(numberFont);
				numberPanel.add(label);
				c.gridy = 0;
				panel.add(numberPanel, c);
			
			
			gridIndex++;
		}
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
			
		}
		
		
	}
}
