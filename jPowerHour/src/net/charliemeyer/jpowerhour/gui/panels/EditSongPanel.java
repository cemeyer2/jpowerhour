package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.gui.util.JButtonIconizer;
import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;

public class EditSongPanel extends JPanel implements ActionListener
{
	private JPowerHourSong song;
	private JSlider startSlider, durationSlider;
	private JButton previewStart, previewEnd, ok, cancel;
	private JPowerHourFrame frame;
	
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
		durationSlider.setValue((int)(song.getPlayLengthMs()/1000));

		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,4));
		
		previewStart = new JButton();
		previewEnd = new JButton();
		ok = new JButton();
		cancel = new JButton();
		
		previewStart.addActionListener(this);
		previewEnd.addActionListener(this);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		
		JButtonIconizer.iconize(previewStart, "play.png", "Preview Start", "Preview From Start of Interval", false);
		JButtonIconizer.iconize(previewEnd, "play.png", "Preview End", "Preview From End of Interval", false);
		JButtonIconizer.iconize(ok, "ok.png", "OK", "OK", true);
		JButtonIconizer.iconize(cancel, "cancel.png", "Cancel", "Cancel", true);
		
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
		
		frame = new JPowerHourFrame("Edit Song: "+song.toString());
		frame.setSize(dim);
		frame.setContentPane(this);
		
	}
	
	public void show()
	{
		frame.setVisible(true);
	}

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
		long oldlength = song.getPlayLengthMs();
		
		song.setPlayLengthMs(6000);
		song.setStartPos(startSlider.getValue()*1000+(durationSlider.getValue()-10)*1000);
		previewStart.setEnabled(false);
		try 
		{
			song.playSong();
		} 
		catch (BasicPlayerException e) 
		{
			String message = "Unable to get audio device, please quit all programs that may be using the audio device";
			JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		previewStart.setEnabled(true);
		song.setPlayLengthMs(oldlength);
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
		
		song.setPlayLengthMs(length*1000);
		song.setStartPos(start*1000);
		frame.setVisible(false);
	}

	private void handlePreviewStartAction() 
	{
		long oldstart = song.getStartTime();	
		long oldlength = song.getPlayLengthMs();
		
		song.setPlayLengthMs(6000);
		song.setStartPos(startSlider.getValue()*1000);
		previewStart.setEnabled(false);
		try 
		{
			song.playSong();
		} 
		catch (BasicPlayerException e) 
		{
			String message = "Unable to get audio device, please quit all programs that may be using the audio device";
			JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		previewStart.setEnabled(true);
		song.setPlayLengthMs(oldlength);
		song.setStartPos(oldstart);
	}
}
