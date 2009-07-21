package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import javazoom.jlgui.basicplayer.BasicPlayerException;

import net.charliemeyer.jpowerhour.PowerHourSong;
import net.charliemeyer.jpowerhour.gui.JPowerHourGUI;
import net.charliemeyer.jpowerhour.gui.util.MusicFilter;

public class LowerButtonPanel extends JPanel implements ActionListener
{
	private JButton play, pause, stop, add, remove, up, down;
	
	private SongListPanel songListPanel;
	
	public LowerButtonPanel(JPowerHourGUI parent)
	{
		super();
		
		this.songListPanel = parent.getSongListPanel();
		
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
		// TODO Auto-generated method stub
		
	}

	private void handleUpAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleRemoveAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleAddAction() 
	{
		JFileChooser chooser = new JFileChooser();		
		chooser.setFileFilter(new MusicFilter());
		int retval = chooser.showOpenDialog(this);

        if (retval == JFileChooser.APPROVE_OPTION) 
        {
            File file = chooser.getSelectedFile();
            try
            {
            	PowerHourSong song = new PowerHourSong(file);
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

	private void handlePauseAction() {
		// TODO Auto-generated method stub
		
	}

	private void handleStopAction() {
		// TODO Auto-generated method stub
		
	}

	private void handlePlayAction() {
		// TODO Auto-generated method stub
		
	}
}