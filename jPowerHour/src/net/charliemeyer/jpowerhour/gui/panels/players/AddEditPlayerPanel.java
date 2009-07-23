package net.charliemeyer.jpowerhour.gui.panels.players;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.charliemeyer.jpowerhour.gui.util.AlcoholPercentageListRenderer;
import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;
import net.charliemeyer.jpowerhour.player.JPowerHourPlayer;

public class AddEditPlayerPanel extends JPanel implements ActionListener
{
	private JPowerHourPlayer player;
	private JTextField name;
	private JComboBox age, gender, shotGlassSize, alcoholPercentage;
	private JSlider weight;
	private JButton ok,cancel;
	private JPowerHourFrame frame;
	private ListPlayersPanel parent;
	
	public AddEditPlayerPanel(ListPlayersPanel parent)
	{
		this.parent = parent;
		name = new JTextField();
		name.setBorder(BorderFactory.createTitledBorder("Player Name"));
		
		DefaultComboBoxModel ageModel = new DefaultComboBoxModel();
		age = new JComboBox(ageModel);
		for(int i = 16; i <= 100; i++)
		{
			ageModel.addElement(new Integer(i));
		}
		age.setBorder(BorderFactory.createTitledBorder("Player Age"));
		
		String[] genders = {"Male","Female"};
		gender = new JComboBox(genders);
		gender.setBorder(BorderFactory.createTitledBorder("Player Gender"));
		
		weight = new JSlider();
		weight.setBorder(BorderFactory.createTitledBorder("Player Weight (lbs)"));
		weight.setMajorTickSpacing(50);
		weight.setMinorTickSpacing(25);
		weight.setMinimum(100);
		weight.setMaximum(300);
		weight.setPaintTicks(true);
		weight.setPaintLabels(true);
		
		String[] shotGlassSizes = {"1","1.5","2","2.5","3"};
		shotGlassSize = new JComboBox(shotGlassSizes);
		shotGlassSize.setBorder(BorderFactory.createTitledBorder("Shot Glass Size (oz)"));
		
		Double[] percentages = {new Double(0.027), new Double(0.035), new Double(0.049)};
		alcoholPercentage = new JComboBox(percentages);
		alcoholPercentage.setBorder(BorderFactory.createTitledBorder("Alcohol Percentage"));
		alcoholPercentage.setRenderer(new AlcoholPercentageListRenderer());
		
		ok = new JButton();
		ok.addActionListener(this);
		try
		{
			ok.setIcon(new ImageIcon(ImageIO.read(new File("images/ok.png"))));
		}
		catch(IOException ioe)
		{
			try 
			{
				ok.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/ok.png"))));
			} 
			catch (MalformedURLException e) 
			{
				ok.setText("OK");
			} 
			catch (IOException e) 
			{
				ok.setText("OK");
			}
		}
		
		cancel = new JButton();
		cancel.addActionListener(this);
		try
		{
			cancel.setIcon(new ImageIcon(ImageIO.read(new File("images/cancel.png"))));
		}
		catch(IOException ioe)
		{
			try 
			{
				cancel.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/cancel.png"))));
			} 
			catch (MalformedURLException e) 
			{
				cancel.setText("Cancel");
			} 
			catch (IOException e) 
			{
				cancel.setText("Cancel");
			}
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(ok);
		panel.add(cancel);
		
		
		setLayout(new GridLayout(7,1));
		add(name);
		add(age);
		add(gender);
		add(weight);
		add(shotGlassSize);
		add(alcoholPercentage);
		add(panel);
		
		Dimension dim = new Dimension(300,650);
		setSize(dim);
		frame = new JPowerHourFrame("Player Info");
		frame.setSize(dim);
		frame.setContentPane(this);
	}
	
	public AddEditPlayerPanel(ListPlayersPanel parent, JPowerHourPlayer playerToEdit)
	{
		this(parent);
		this.player = playerToEdit;
		
		name.setText(player.getName());
		age.setSelectedItem(new Integer(player.getAge()));
		String gender = (player.isMale())?"Male":"Female";
		this.gender.setSelectedItem(gender);
		weight.setValue(player.getWeight());
		shotGlassSize.setSelectedItem(player.getShotGlassSize()+"");
		alcoholPercentage.setSelectedItem(new Double(player.getAlcholPercentage()));
	}
	
	public void show()
	{
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		
		if(ok.equals(source))
		{
			handleOkAction();
		}
		else if(cancel.equals(source))
		{
			handleCancelAction();
		}
	}

	private void handleCancelAction() 
	{
		frame.setVisible(false);
		frame.dispose();
	}

	private void handleOkAction() 
	{
		//creating new player
		if(player == null)
		{
			player = new JPowerHourPlayer();
			player.setName(name.getText());
			player.setAge(((Integer)age.getSelectedItem()).intValue());
			player.setWeight(weight.getValue());
			boolean isMale = gender.getSelectedItem().toString().equals("Male");
			player.setMale(isMale);
			player.setShotGlassSize(Double.parseDouble(shotGlassSize.getSelectedItem().toString()));
			player.setAlcholPercentage(((Double)alcoholPercentage.getSelectedItem()).doubleValue());
			parent.addPlayer(player);
		}
		else //editing existing player
		{
			player.setName(name.getText());
			player.setAge(((Integer)age.getSelectedItem()).intValue());
			player.setWeight(weight.getValue());
			boolean isMale = gender.getSelectedItem().toString().equals("Male");
			player.setMale(isMale);
			player.setShotGlassSize(Double.parseDouble(shotGlassSize.getSelectedItem().toString()));
			player.setAlcholPercentage(((Double)alcoholPercentage.getSelectedItem()).doubleValue());
		}
		frame.setVisible(false);
		frame.dispose();
	}
}