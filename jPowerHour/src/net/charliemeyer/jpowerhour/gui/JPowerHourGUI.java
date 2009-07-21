package net.charliemeyer.jpowerhour.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class JPowerHourGUI 
{
	JFrame frame;
	
	public JPowerHourGUI()
	{
		frame = new JFrame("jPowerHour");
		frame.setJMenuBar(initializeMenuBar());
	}
	
	private JMenuBar initializeMenuBar()
	{
		JMenuBar menubar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem about = new JMenuItem("About");
		JMenuItem quit = new JMenuItem("Quit");
		file.add(about);
		file.addSeparator();
		file.add(quit);
		
		menubar.add(file);
		
		return menubar;
	}
	
	public void show()
	{
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JPowerHourGUI gui = new JPowerHourGUI();
		gui.show();
	}
}
