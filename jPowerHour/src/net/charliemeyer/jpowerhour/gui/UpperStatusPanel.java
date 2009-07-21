package net.charliemeyer.jpowerhour.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class UpperStatusPanel extends JPanel 
{
	private ArrayList<String> texts;
	private JLabel upperLabel;
	private JLabel lowerLeft;
	private JLabel lowerRight;
	private JProgressBar progress;
	
	public UpperStatusPanel()
	{
		super();
		setLayout(new GridLayout(2,1));
		
		texts = new ArrayList<String>();
		texts.add("Welcome to jPowerHour");
		
		upperLabel = new JLabel();
		lowerLeft = new JLabel("0:00");
		lowerRight = new JLabel("0:00");
		progress = new JProgressBar(0, 100);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());
		lowerPanel.add(lowerLeft, BorderLayout.WEST);
		lowerPanel.add(progress, BorderLayout.CENTER);
		lowerPanel.add(lowerRight, BorderLayout.EAST);
		
		add(upperLabel);
		add(lowerPanel);
		
		Thread th = new Thread(new Runnable()
		{
			int cur = 0;
			
			public void run()
			{
				while(true)
				{
					if(cur == texts.size())
						cur = 0;
					upperLabel.setText(texts.get(cur));
					cur++;
					try
					{
						Thread.sleep(5000);
					}
					catch(InterruptedException ie)
					{
						ie.printStackTrace();
					}
				}
			}
		});
		th.start();
	}
}
