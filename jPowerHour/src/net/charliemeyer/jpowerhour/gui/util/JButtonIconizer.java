package net.charliemeyer.jpowerhour.gui.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JButtonIconizer 
{
	public static void iconize(JButton button, String imageFileName, String text, String tooltip, boolean preferOnlyImage)
	{
		try
		{
			button.setIcon(new ImageIcon(ImageIO.read(new File("images/"+imageFileName))));
		}
		catch(IOException ioe)
		{
			try 
			{
				button.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/"+imageFileName))));
			} 
			catch (MalformedURLException e) 
			{
				button.setText(text);
			} 
			catch (IOException e) 
			{
				button.setText(text);
			}
		}
		if(!preferOnlyImage)
		{
			button.setText(text);
		}
		button.setToolTipText(tooltip);
	}
}
