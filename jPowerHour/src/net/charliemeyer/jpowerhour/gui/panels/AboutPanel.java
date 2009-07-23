package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;

public class AboutPanel extends JPanel implements MouseListener
{
	JLabel url, name;
	JPowerHourFrame frame;
	
	public AboutPanel()
	{
		setLayout(new GridLayout(3,1));
		
		JLabel title = new JLabel("jPowerHour version 0.1 alpha",SwingConstants.CENTER);
		url = new JLabel("http://sourceforge.net/projects/jpowerhour/", SwingConstants.CENTER);
		url.addMouseListener(this);
		
		name = new JLabel("written by Charlie Meyer, 2009", SwingConstants.CENTER);
		name.addMouseListener(this);
		
		add(title);
		add(name);
		add(url);
		
		Dimension dim = new Dimension(300,200);
		
		setSize(dim);
		
		frame = new JPowerHourFrame("About");
		
		frame.setSize(dim);
		frame.setContentPane(this);
	}

	@Override
	public void mouseClicked(MouseEvent event) 
	{
		String url = "";
		if(event.getSource().equals(url))
		{
			url = "http://sourceforge.net/projects/jpowerhour/";
		}
		else if(event.getSource().equals(name))
		{
			url = "http://www.charliemeyer.net";
		}
		if (Desktop.isDesktopSupported()) 
		{
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) 
            {
            	URI uri = null;
                try 
                {
                    uri = new URI(url);
                    desktop.browse(uri);
                }
                catch(IOException ioe) 
                {
                    ioe.printStackTrace();
                }
                catch(URISyntaxException use) 
                {
                    use.printStackTrace();
                }
            }
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		frame.setCursor(cursor);
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		Cursor cursor = Cursor.getDefaultCursor();
		frame.setCursor(cursor);		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void show()
	{
		frame.setVisible(true);
	}
}
