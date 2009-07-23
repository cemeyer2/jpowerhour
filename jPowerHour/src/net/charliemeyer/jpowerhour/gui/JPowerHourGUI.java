package net.charliemeyer.jpowerhour.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.PowerHourThread;
import net.charliemeyer.jpowerhour.gui.panels.AboutPanel;
import net.charliemeyer.jpowerhour.gui.panels.LowerButtonPanel;
import net.charliemeyer.jpowerhour.gui.panels.SongListPanel;
import net.charliemeyer.jpowerhour.gui.panels.UpperStatusPanel;
import net.charliemeyer.jpowerhour.gui.panels.players.ListPlayersPanel;
import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;
import net.charliemeyer.jpowerhour.gui.util.JPowerHourPlaylistFilter;
import net.charliemeyer.jpowerhour.player.JPowerHourPlayer;
import net.charliemeyer.jpowerhour.util.xml.LoadPlaylist;
import net.charliemeyer.jpowerhour.util.xml.SavePlaylist;

import org.jdom.JDOMException;

public class JPowerHourGUI implements ActionListener
{
	private JPowerHourFrame frame;
	private JPanel panel;
	private SongListPanel songListPanel;
	private LowerButtonPanel lowerButtonPanel;
	private UpperStatusPanel upperStatusPanel;
	private PowerHourThread thread;
	private File currentlyLoadedFile;
	private ListPlayersPanel listPlayersPanel;
	
	private final int GUI_WIDTH = 400;
	private final int GUI_HEIGHT = 800;
	
	private JMenuItem quit,open,openItunes,save,saveAs,onlineHelp,about,managePlayers,manageInterludes;
	
	public JPowerHourGUI()
	{
		thread = new PowerHourThread();
		listPlayersPanel = new ListPlayersPanel();
		
		frame = new JPowerHourFrame("jPowerHour");
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
		quit = new JMenuItem("Quit");
		open = new JMenuItem("Open jPowerHour Playlist");
		openItunes = new JMenuItem("Open iTunes Playlist");
		save = new JMenuItem("Save Playlist");
		saveAs = new JMenuItem("Save Playlist As");
		
		quit.addActionListener(this);
		open.addActionListener(this);
		openItunes.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		
		openItunes.setEnabled(false);
		
		file.add(open);
		file.add(openItunes);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(quit);
		menubar.add(file);
		
		JMenu options = new JMenu("Options");
		managePlayers = new JMenuItem("Manage Players");
		manageInterludes = new JMenuItem("Manage Interludes");
		
		managePlayers.addActionListener(this);
		manageInterludes.addActionListener(this);
		
		options.add(managePlayers);
		options.add(manageInterludes);
		menubar.add(options);
		
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
		thread.clearPlayers();
		for(int i = 0; i < listPlayersPanel.getPlayerCount(); i++)
		{
			thread.addPlayer(listPlayersPanel.getPlayer(i));
		}
		ArrayList<JPowerHourSong> songs = new ArrayList<JPowerHourSong>();
		for(int i = 0; i < getSongListPanel().getPowerHourSongCount(); i++)
		{
			songs.add(getSongListPanel().getPowerHourSong(i));
		}
		thread.setSongs(songs);
		Thread th = new Thread(thread);
		th.start();
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
		else if(manageInterludes.equals(source))
		{
			handleManageInterludesAction();
		}
		else if(managePlayers.equals(source))
		{
			handleManagePlayersAction();
		}
	}

	private void handleManageInterludesAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleManagePlayersAction() {
		listPlayersPanel.show();
	}

	private void handleAboutAction() {
		AboutPanel panel = new AboutPanel();
		panel.show();
	}

	private void handleOnlineHelpAction() 
	{
		String url = "http://jpowerhour.sourceforge.net";
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
            else
            {
            	String message = "Please visit "+url;
            	JOptionPane.showMessageDialog(frame, message, "Help", JOptionPane.INFORMATION_MESSAGE);
            }
		}
		else
        {
        	String message = "Please visit "+url;
        	JOptionPane.showMessageDialog(frame, message, "Help", JOptionPane.INFORMATION_MESSAGE);
        }
	}

	private void handleSaveAsAction() 
	{
		ArrayList<JPowerHourSong> songs = new ArrayList<JPowerHourSong>();
		for(int i = 0; i < getSongListPanel().getPowerHourSongCount(); i++)
		{
			songs.add(getSongListPanel().getPowerHourSong(i));
		}
		JFileChooser chooser = new JFileChooser();		
		chooser.setFileFilter(new JPowerHourPlaylistFilter());
		int retval = chooser.showSaveDialog(frame);

        if (retval == JFileChooser.APPROVE_OPTION) 
        {
            File file = chooser.getSelectedFile();
            try 
            {
				SavePlaylist.savePlaylist(songs, file);
			} 
            catch (IOException e) 
            {
				e.printStackTrace();
			}
        }		
	}

	private void handleSaveAction() 
	{
		if(currentlyLoadedFile == null)
		{
			handleSaveAsAction();
		}
		else
		{
			ArrayList<JPowerHourSong> songs = new ArrayList<JPowerHourSong>();
			for(int i = 0; i < getSongListPanel().getPowerHourSongCount(); i++)
			{
				songs.add(getSongListPanel().getPowerHourSong(i));
			}
			try 
			{
				SavePlaylist.savePlaylist(songs, currentlyLoadedFile);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	private void handleOpenItunesAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleOpenAction() 
	{
		JFileChooser chooser = new JFileChooser();		
		chooser.setFileFilter(new JPowerHourPlaylistFilter());
		int retval = chooser.showOpenDialog(frame);

        if (retval == JFileChooser.APPROVE_OPTION) 
        {
            File file = chooser.getSelectedFile();
            try 
            {
				ArrayList<JPowerHourSong> songs = LoadPlaylist.loadPlaylist(file);
				songListPanel.clearSongs();
				for(JPowerHourSong song : songs)
				{
					songListPanel.addPowerHourSong(song);
				}
				currentlyLoadedFile = file;
			} 
            catch (IOException e) 
            {
				e.printStackTrace();
			} 
            catch (JDOMException e) 
			{
				e.printStackTrace();
			}
        }
	}

	private void handleQuitAction() 
	{
		frame.setVisible(false);
		System.exit(0);		
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	
		JPowerHourGUI gui = new JPowerHourGUI();
		gui.show();
	}
}
