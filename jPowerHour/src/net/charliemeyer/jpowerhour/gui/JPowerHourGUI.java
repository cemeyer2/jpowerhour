package net.charliemeyer.jpowerhour.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javazoom.jlgui.basicplayer.BasicPlayerException;

import net.charliemeyer.jpowerhour.PowerHourSong;
import net.charliemeyer.jpowerhour.gui.panels.LowerButtonPanel;
import net.charliemeyer.jpowerhour.gui.panels.SongListPanel;
import net.charliemeyer.jpowerhour.gui.panels.UpperStatusPanel;

public class JPowerHourGUI 
{
	private JFrame frame;
	private JPanel panel;
	private SongListPanel songListPanel;
	private LowerButtonPanel lowerButtonPanel;
	private UpperStatusPanel upperStatusPanel;
	
	private final int GUI_WIDTH = 400;
	private final int GUI_HEIGHT = 800;
	
	
	
	public JPowerHourGUI()
	{
		frame = new JFrame("jPowerHour");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setJMenuBar(initializeMenuBar());
		panel = new JPanel();
		Dimension dim = new Dimension(GUI_WIDTH, GUI_HEIGHT);
		
		songListPanel = new SongListPanel(this);
		
		lowerButtonPanel = new LowerButtonPanel(this);
		
		upperStatusPanel = new UpperStatusPanel(this);
		
		
		panel.setLayout(new BorderLayout());
		panel.add(upperStatusPanel, BorderLayout.NORTH);
		panel.add(songListPanel, BorderLayout.CENTER);
		panel.add(lowerButtonPanel, BorderLayout.SOUTH);
		
		panel.setSize(dim);
		frame.setContentPane(panel);
		frame.setSize(dim);
		
		
	}
	
	private JMenuBar initializeMenuBar()
	{
		JMenuBar menubar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem quit = new JMenuItem("Quit");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem openItunes = new JMenuItem("Open iTunes Playlist");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveAs = new JMenuItem("Save As");
		
		file.add(open);
		file.add(openItunes);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(quit);
		menubar.add(file);
		
		JMenu help = new JMenu("Help");
		JMenuItem onlineHelp = new JMenuItem("Online Help");
		JMenuItem about = new JMenuItem("About");
		
		help.add(onlineHelp);
		help.add(about);
		menubar.add(help);
		
		return menubar;
	}
	
	public void show()
	{
		frame.setVisible(true);
	}
	
	public SongListPanel getSongListPanel()
	{
		return songListPanel;
	}
	
	public UpperStatusPanel getUpperStatusPanel()
	{
		return upperStatusPanel;
	}
	
	public LowerButtonPanel getLowerButtonPanel()
	{
		return lowerButtonPanel;
	}
	
	public void runPowerHour()
	{
		for(int i = 0; i < songListPanel.getPowerHourSongCount(); i++)
		{
			PowerHourSong song = songListPanel.getPowerHourSong(i);
			try 
			{
				song.playSong();
			} 
			catch (BasicPlayerException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JPowerHourGUI gui = new JPowerHourGUI();
		gui.show();
	}
}
