package net.charliemeyer.jpowerhour.gui;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class LowerButtonPanel extends JPanel 
{
	private JButton play, pause, stop, add, remove, up, down;
	
	public LowerButtonPanel()
	{
		super();
		
		setLayout(new GridLayout(1,7));
		
		Image playImage = null, pauseImage = null, stopImage = null, addImage = null, removeImage = null, upImage = null, downImage = null;

		try
		{
	        playImage = ImageIO.read(new File("images/play.png"));
	        pauseImage = ImageIO.read(new File("images/pause.png"));
	        stopImage = ImageIO.read(new File("images/stop.png"));
	        addImage = ImageIO.read(new File("images/add.png"));
	        removeImage = ImageIO.read(new File("images/remove.png"));
	        upImage = ImageIO.read(new File("images/up.png"));
	        downImage = ImageIO.read(new File("images/down.png"));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		play = new JButton(new ImageIcon(playImage));
		pause = new JButton(new ImageIcon(pauseImage));
		stop = new JButton(new ImageIcon(stopImage));
		add = new JButton(new ImageIcon(addImage));
		remove = new JButton(new ImageIcon(removeImage));
		up = new JButton(new ImageIcon(upImage));
		down = new JButton(new ImageIcon(downImage));
		
		add(add);
		add(remove);
		add(up);
		add(down);
		add(play);
		add(pause);
		add(stop);
	}
}