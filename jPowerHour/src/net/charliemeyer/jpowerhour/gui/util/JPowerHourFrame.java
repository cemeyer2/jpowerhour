package net.charliemeyer.jpowerhour.gui.util;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class JPowerHourFrame extends JFrame
{
	public JPowerHourFrame()
	{
		this("");
	}
	
	public JPowerHourFrame(String title)
	{
		super(title);
		
		try
		{
			this.setIconImage(ImageIO.read(new File("images/beer.png")));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
