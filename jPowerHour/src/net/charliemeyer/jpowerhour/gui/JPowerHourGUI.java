package net.charliemeyer.jpowerhour.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.charliemeyer.jpowerhour.PowerHourSong;
import net.charliemeyer.jpowerhour.PowerHourThread;
import net.charliemeyer.jpowerhour.gui.panels.LowerButtonPanel;
import net.charliemeyer.jpowerhour.gui.panels.SongListPanel;
import net.charliemeyer.jpowerhour.gui.panels.UpperStatusPanel;

public class JPowerHourGUI implements ActionListener
{
	private JFrame frame;
	private JPanel panel;
	private SongListPanel songListPanel;
	private LowerButtonPanel lowerButtonPanel;
	private UpperStatusPanel upperStatusPanel;
	private PowerHourThread thread;
	
	private final int GUI_WIDTH = 400;
	private final int GUI_HEIGHT = 800;
	
	private JMenuItem quit,open,openItunes,save,saveAs,onlineHelp,about;
	
	public JPowerHourGUI()
	{
		thread = new PowerHourThread();
		
		frame = new JFrame("jPowerHour");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(initializeMenuBar());
		
		Image icon = null;
		try
		{
			icon = ImageIO.read(new File("images/beer.png"));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		if(icon != null)
		{
			frame.setIconImage(icon);
		}
		
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
		quit = new JMenuItem("Quit");
		open = new JMenuItem("Open");
		openItunes = new JMenuItem("Open iTunes Playlist");
		save = new JMenuItem("Save");
		saveAs = new JMenuItem("Save As");
		
		quit.addActionListener(this);
		open.addActionListener(this);
		openItunes.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		
		file.add(open);
		file.add(openItunes);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(quit);
		menubar.add(file);
		
		JMenu help = new JMenu("Help");
		onlineHelp = new JMenuItem("Online Help");
		about = new JMenuItem("About");
		
		onlineHelp.addActionListener(this);
		about.addActionListener(this);
		
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
	
	public PowerHourThread getPowerHourThread()
	{
		return thread;
	}
	
	public void runPowerHour()
	{
		ArrayList<PowerHourSong> songs = new ArrayList<PowerHourSong>();
		for(int i = 0; i < getSongListPanel().getPowerHourSongCount(); i++)
		{
			songs.add(getSongListPanel().getPowerHourSong(i));
		}
		thread.setSongs(songs);
		Thread th = new Thread(thread);
		th.start();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JPowerHourGUI gui = new JPowerHourGUI();
		gui.show();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		if(quit.equals(source))
		{
			handleQuitAction();
		}
		else if(open.equals(source))
		{
			handleOpenAction();
		}
		else if(openItunes.equals(source))
		{
			handleOpenItunesAction();
		}
		else if(save.equals(source))
		{
			handleSaveAction();
		}
		else if(saveAs.equals(source))
		{
			handleSaveAsAction();
		}
		else if(onlineHelp.equals(source))
		{
			handleOnlineHelpAction();
		}
		else if(about.equals(source))
		{
			handleAboutAction();
		}
	}

	private void handleAboutAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleOnlineHelpAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleSaveAsAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleSaveAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleOpenItunesAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleOpenAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleQuitAction() 
	{
		frame.setVisible(false);
		System.exit(0);		
	}
}
