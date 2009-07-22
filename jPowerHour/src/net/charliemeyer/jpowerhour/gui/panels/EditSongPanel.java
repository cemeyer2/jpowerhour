package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import javazoom.jlgui.basicplayer.BasicPlayerException;

import net.charliemeyer.jpowerhour.JPowerHourSong;

public class EditSongPanel extends JPanel implements ActionListener
{
	private JPowerHourSong song;
	private JSlider startSlider, durationSlider;
	private JButton previewStart, previewEnd, ok, cancel;
	private JFrame frame;
	
	public EditSongPanel(JPowerHourSong song)
	{
		this.song = song;
		setLayout(new GridLayout(4,1));
		JLabel title = new JLabel(song.toString(),SwingConstants.CENTER);
		
		int songSeconds = (int) (song.getDurationMs()/1000d);
		int startSeconds = (int) (song.getStartTime()/1000d);
		
		startSlider = new JSlider();
		startSlider.setBorder(BorderFactory.createTitledBorder("Song Start Point (seconds)"));
		startSlider.setMajorTickSpacing(60);
		startSlider.setMinorTickSpacing(30);
		startSlider.setMaximum(songSeconds);
		startSlider.setPaintTicks(true);
		startSlider.setPaintLabels(true);
		startSlider.setValue(startSeconds);
		
		durationSlider = new JSlider();
		durationSlider.setBorder(BorderFactory.createTitledBorder("Length To Play (seconds)"));
		durationSlider.setMajorTickSpacing(30);
		durationSlider.setMinorTickSpacing(15);
		durationSlider.setMaximum(120);
		durationSlider.setPaintTicks(true);
		durationSlider.setPaintLabels(true);
		durationSlider.setValue(song.getPlayLength());

		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,4));
		
		previewStart = new JButton("Preview Start");
		previewEnd = new JButton("Preview End");
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		
		previewStart.addActionListener(this);
		previewEnd.addActionListener(this);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		try
		{
			previewStart.setIcon(new ImageIcon(ImageIO.read(new File("images/play.png"))));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		try
		{
			previewEnd.setIcon(new ImageIcon(ImageIO.read(new File("images/play.png"))));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		try
		{
			ok.setIcon(new ImageIcon(ImageIO.read(new File("images/ok.png"))));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		try
		{
			cancel.setIcon(new ImageIcon(ImageIO.read(new File("images/cancel.png"))));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		buttonPanel.add(previewStart);
		buttonPanel.add(previewEnd);
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		
		
		add(title);
		add(startSlider);
		add(durationSlider);
		add(buttonPanel);
		
		Dimension dim = new Dimension(600,450);
		setSize(dim);
		
		frame = new JFrame("Edit Song: "+song.toString());
		frame.setSize(dim);
		frame.setContentPane(this);
		Image image = null;
		try
		{
			image = ImageIO.read(new File("images/beer.png"));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		if(image != null)
		{
			frame.setIconImage(image);
		}
	}
	
	public void show()
	{
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		
		if(previewStart.equals(source))
		{
			handlePreviewStartAction();
		}
		else if(previewEnd.equals(source))
		{
			handlePreviewEndAction();
		}
		else if(ok.equals(source))
		{
			handleOKAction();
		}
		else if(cancel.equals(source))
		{
			handleCancelAction();
		}
	}

	private void handlePreviewEndAction() 
	{
		long oldstart = song.getStartTime();	
		int oldlength = song.getPlayLength();
		
		song.setPlayLength(10);
		song.setStartPos(startSlider.getValue()*1000+(durationSlider.getValue()-10)*1000);
		previewStart.setEnabled(false);
		try 
		{
			song.playSong();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
		previewStart.setEnabled(true);
		song.setPlayLength(oldlength);
		song.setStartPos(oldstart);
	}

	private void handleCancelAction() 
	{
		frame.setVisible(false);
		frame.dispose();
	}

	private void handleOKAction() 
	{
		int start = startSlider.getValue();
		int length = durationSlider.getValue();
		
		song.setPlayLength(length);
		song.setStartPos(start*1000);
		frame.setVisible(false);
	}

	private void handlePreviewStartAction() 
	{
		long oldstart = song.getStartTime();	
		int oldlength = song.getPlayLength();
		
		song.setPlayLength(10);
		song.setStartPos(startSlider.getValue()*1000);
		previewStart.setEnabled(false);
		try 
		{
			song.playSong();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
		previewStart.setEnabled(true);
		song.setPlayLength(oldlength);
		song.setStartPos(oldstart);
	}
}
