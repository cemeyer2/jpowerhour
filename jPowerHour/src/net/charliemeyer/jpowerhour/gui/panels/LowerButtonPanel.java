package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import net.charliemeyer.jpowerhour.JPowerHourAudioPlayer;
import net.charliemeyer.jpowerhour.JPowerHourListener;
import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.gui.JPowerHourGUI;
import net.charliemeyer.jpowerhour.gui.util.JButtonIconizer;
import net.charliemeyer.jpowerhour.gui.util.MusicFilter;
import net.charliemeyer.jpowerhour.util.Extension;

public class LowerButtonPanel extends JPanel implements ActionListener, JPowerHourListener
{
	private JButton play, pause, stop, add, addFolder, remove, up, down;
	
	private SongListPanel songListPanel;
	private JPowerHourGUI parent;
	private File lastOpenedFolder;
	
	public LowerButtonPanel(JPowerHourGUI parent)
	{
		super();
		
		this.parent = parent;
		this.songListPanel = parent.getSongListPanel();
		parent.getPowerHourThread().addPowerHourListener(this);
		
		setLayout(new GridLayout(1,8));
		

		play = new JButton();
		pause = new JButton();
		stop = new JButton();
		add = new JButton();
		addFolder = new JButton();
		remove = new JButton();
		up = new JButton();
		down = new JButton();
		
		JButtonIconizer.iconize(play, "play.png", "Play", "Play Power Hour", true);
		JButtonIconizer.iconize(pause, "pause.png", "Pause", "Pause Power Hour", true);
		JButtonIconizer.iconize(stop, "stop.png", "Stop", "Stop and Reset Power Hour", true);
		JButtonIconizer.iconize(add, "add.png", "Add", "Add Song to Power Hour", true);
		JButtonIconizer.iconize(remove, "remove.png", "Remove", "Remove Selected Song from Power Hour", true);
		JButtonIconizer.iconize(up, "up.png", "Up", "Move Selected Song Up", true);
		JButtonIconizer.iconize(down, "down.png", "Down", "Move Selected Song Down", true);
		JButtonIconizer.iconize(addFolder, "addFolder.png", "Add Folder", "Add Folder to Power Hour", true);
				
		play.addActionListener(this);
		pause.addActionListener(this);
		stop.addActionListener(this);
		add.addActionListener(this);
		remove.addActionListener(this);
		up.addActionListener(this);
		down.addActionListener(this);
		addFolder.addActionListener(this);
		
		add(add);
		add(addFolder);
		add(remove);
		add(up);
		add(down);
		add(play);
		add(pause);
		add(stop);
	}

	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		if(source.equals(play))
		{
			handlePlayAction();
		}
		else if(source.equals(stop))
		{
			handleStopAction();
		}
		else if(source.equals(pause))
		{
			handlePauseAction();
		}
		else if(source.equals(add))
		{
			handleAddAction();
		}
		else if(source.equals(remove))
		{
			handleRemoveAction();
		}
		else if(source.equals(up))
		{
			handleUpAction();
		}
		else if(source.equals(down))
		{
			handleDownAction();
		}
		else if(source.equals(addFolder))
		{
			handleAddFolderAction();
		}
	}

	private void handleAddFolderAction() 
	{
		JFileChooser chooser = new JFileChooser();
		if(lastOpenedFolder != null)	
		{
			chooser.setCurrentDirectory(lastOpenedFolder);
		}
		chooser.setFileFilter(new MusicFilter());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retval = chooser.showOpenDialog(this);

        if (retval == JFileChooser.APPROVE_OPTION) 
        {
            File file = chooser.getSelectedFile();
            lastOpenedFolder = file.getParentFile();
            try
            {
            	File[] files = file.listFiles();
            	for(File f : files)
            	{
            		if(f.isFile())
            		{
	            		if(Extension.getExtension(f).equals(Extension.mp3))
	            		{
	            			JPowerHourSong song = new JPowerHourSong(f);
	                    	this.songListPanel.addPowerHourSong(song);
	            		}
            		}
            	}
            	
            }
            catch(BasicPlayerException bpe)
            {
            	bpe.printStackTrace();
            }
        } 
        else 
        {
        	//user clicked cancel
        }
	}

	private void handleDownAction() {
		songListPanel.down();
	}

	private void handleUpAction() {
		songListPanel.up();
		
	}

	private void handleRemoveAction() {
		songListPanel.removePowerHourSong();		
	}

	private void handleAddAction() 
	{
		JFileChooser chooser = new JFileChooser();
		if(lastOpenedFolder != null)	
		{
			chooser.setCurrentDirectory(lastOpenedFolder);
		}
		chooser.setFileFilter(new MusicFilter());
		int retval = chooser.showOpenDialog(this);

        if (retval == JFileChooser.APPROVE_OPTION) 
        {
            File file = chooser.getSelectedFile();
            lastOpenedFolder = file.getParentFile();
            try
            {
            	JPowerHourSong song = new JPowerHourSong(file);
            	this.songListPanel.addPowerHourSong(song);
            }
            catch(BasicPlayerException bpe)
            {
            	bpe.printStackTrace();
            }
        } 
        else 
        {
        	//user clicked cancel
        }

	}

	private void handlePauseAction() 
	{
		int status = JPowerHourAudioPlayer.getJPowerHourPlayer().getBasicPlayerStatus();
		if(status == BasicPlayer.PLAYING)
		{
			parent.getPowerHourThread().pause();
		}
		else if(status == BasicPlayer.PAUSED)
		{
			parent.getPowerHourThread().resume();
		}
	}

	private void handleStopAction() 
	{
		parent.getPowerHourThread().stop();
	}

	private void handlePlayAction() {
		
		parent.runPowerHour();
		
	}

	public void powerHourFinished() {
		add.setEnabled(true);
		remove.setEnabled(true);
		up.setEnabled(true);
		down.setEnabled(true);
		play.setEnabled(true);	
		addFolder.setEnabled(true);
	}

	public void powerHourPaused() {
		// TODO Auto-generated method stub
		
	}

	public void powerHourResumed() {
		// TODO Auto-generated method stub
		
	}

	public void songChange(JPowerHourSong currentlyPlaying,
			int currentlyPlayingNumber) {
		// TODO Auto-generated method stub
		
	}

	public void powerHourStarted() 
	{
		add.setEnabled(false);
		remove.setEnabled(false);
		up.setEnabled(false);
		down.setEnabled(false);
		play.setEnabled(false);
		addFolder.setEnabled(false);
	}
}