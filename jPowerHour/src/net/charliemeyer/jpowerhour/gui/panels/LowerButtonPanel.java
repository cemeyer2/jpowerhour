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
import net.charliemeyer.jpowerhour.gui.util.MusicFilter;

public class LowerButtonPanel extends JPanel implements ActionListener, JPowerHourListener
{
	private JButton play, pause, stop, add, remove, up, down;
	
	private SongListPanel songListPanel;
	private JPowerHourGUI parent;
	private File lastOpenedFolder;
	
	public LowerButtonPanel(JPowerHourGUI parent)
	{
		super();
		
		this.parent = parent;
		this.songListPanel = parent.getSongListPanel();
		parent.getPowerHourThread().addPowerHourListener(this);
		
		setLayout(new GridLayout(1,7));
		

		play = new JButton();
		pause = new JButton();
		stop = new JButton();
		add = new JButton();
		remove = new JButton();
		up = new JButton();
		down = new JButton();
		
		try
		{
			play.setIcon(new ImageIcon(ImageIO.read(new File("images/play.png"))));
			pause.setIcon(new ImageIcon(ImageIO.read(new File("images/pause.png"))));
			stop.setIcon(new ImageIcon(ImageIO.read(new File("images/stop.png"))));
			add.setIcon(new ImageIcon(ImageIO.read(new File("images/add.png"))));
			remove.setIcon(new ImageIcon(ImageIO.read(new File("images/remove.png"))));
			up.setIcon(new ImageIcon(ImageIO.read(new File("images/up.png"))));
			down.setIcon(new ImageIcon(ImageIO.read(new File("images/down.png"))));
		}
		catch(IOException ioe)
		{
			try 
			{
				play.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/play.png"))));
				pause.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/pause.png"))));
				stop.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/stop.png"))));
				add.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/add.png"))));
				remove.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/remove.png"))));
				up.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/up.png"))));
				down.setIcon(new ImageIcon(ImageIO.read(new URL("http://jpowerhour.sourceforge.net/images/down.png"))));
			} 
			catch (MalformedURLException e) 
			{
				play.setText("Play");
				pause.setText("Pause");
				stop.setText("Stop");
				add.setText("Add Song");
				remove.setText("Remove Song");
				up.setText("Move Up");
				down.setText("Move Down");
			} 
			catch (IOException e) 
			{
				play.setText("Play");
				pause.setText("Pause");
				stop.setText("Stop");
				add.setText("Add Song");
				remove.setText("Remove Song");
				up.setText("Move Up");
				down.setText("Move Down");
			}
		}
		
		
		
		play.addActionListener(this);
		pause.addActionListener(this);
		stop.addActionListener(this);
		add.addActionListener(this);
		remove.addActionListener(this);
		up.addActionListener(this);
		down.addActionListener(this);
		
		add(add);
		add(remove);
		add(up);
		add(down);
		add(play);
		add(pause);
		add(stop);
	}

	@Override
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
		add.setEnabled(false);
		remove.setEnabled(false);
		up.setEnabled(false);
		down.setEnabled(false);
		play.setEnabled(false);
		parent.runPowerHour();
		
	}

	@Override
	public void powerHourFinished() {
		add.setEnabled(true);
		remove.setEnabled(true);
		up.setEnabled(true);
		down.setEnabled(true);
		play.setEnabled(true);		
	}

	@Override
	public void powerHourPaused() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void powerHourResumed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void songChange(JPowerHourSong currentlyPlaying,
			int currentlyPlayingNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void powerHourStarted() {
		// TODO Auto-generated method stub
		
	}
}