package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import net.charliemeyer.jpowerhour.JPowerHourPlayer;
import net.charliemeyer.jpowerhour.JPowerHourListener;
import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.gui.JPowerHourGUI;

public class UpperStatusPanel extends JPanel implements BasicPlayerListener, JPowerHourListener
{
	private ArrayList<String> texts;
	private JLabel upperLabel;
	private JLabel lowerLeft;
	private JLabel lowerRight;
	private JProgressBar progress;
	private JPowerHourSong currentlyPlayingSong;
	
	public UpperStatusPanel(JPowerHourGUI parent)
	{
		super();
		
		JPowerHourPlayer.getJPowerHourPlayer().addBasicPlayerListener(this);
		parent.getPowerHourThread().addPowerHourListener(this);
		
		setLayout(new GridLayout(2,1));
		
		initializeTexts();
		
		upperLabel = new JLabel();
		lowerLeft = new JLabel("0:00");
		lowerRight = new JLabel("0:00");
		progress = new JProgressBar(0, 100);
		progress.setStringPainted(true);
		progress.setString("");
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());
		lowerPanel.add(lowerLeft, BorderLayout.WEST);
		lowerPanel.add(progress, BorderLayout.CENTER);
		lowerPanel.add(lowerRight, BorderLayout.EAST);
		
		add(upperLabel);
		add(lowerPanel);
		
		Thread th = new Thread(new Runnable()
		{
			int cur = 0;
			
			public void run()
			{
				while(true)
				{
					if(cur == texts.size())
						cur = 0;
					upperLabel.setText(texts.get(cur));
					cur++;
					try
					{
						Thread.sleep(5000);
					}
					catch(InterruptedException ie)
					{
						ie.printStackTrace();
					}
				}
			}
		});
		th.start();
	}

	@Override
	public void opened(Object arg0, Map arg1) {
		progress.setValue(0);
	}

	@Override
	public void progress(int arg0, long microseconds, byte[] arg2, Map arg3) 
	{
		long start = currentlyPlayingSong.getStartTime();
		long end = start + currentlyPlayingSong.getPlayLength()*1000;
		long pos = microseconds / 1000;
		int progress = (int) (((double)(pos)/(double)(end-start))*100);
		
		long cur = start+pos;
		int totalSec = (int) (cur/1000);
		
		int mins = totalSec/60;
		int secs = totalSec % 60;
		
		String str = mins+":"+((secs < 10)?"0"+secs : secs);
		
		this.progress.setValue(progress);
		this.progress.setString(str);
		
		
	}

	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paused() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void songChange(JPowerHourSong currentlyPlaying, int currentlyPlayingNumber) 
	{
		currentlyPlayingSong = currentlyPlaying;
		long start = currentlyPlaying.getStartTime();
		int duration = currentlyPlaying.getPlayLength();
		
		int startTotalSeconds = (int) (start/1000);
		int startMins = startTotalSeconds / 60;
		int startSecs = startTotalSeconds % 60;
		
		int endTotalSeconds = startTotalSeconds + duration;
		int endMins = endTotalSeconds / 60;
		int endSecs = endTotalSeconds % 60;
		
		String startStr = startMins+":"+((startSecs < 10)?"0"+startSecs : startSecs);
		String endStr = endMins+":"+((endSecs < 10)?"0"+endSecs : endSecs);
		
		lowerLeft.setText(startStr);
		lowerRight.setText(endStr);
		
		initializeTexts();
		texts.add("Currently Playing: "+currentlyPlayingSong.toString());
	}
	
	private void initializeTexts()
	{
		texts = new ArrayList<String>();
		texts.add("Welcome to jPowerHour");
	}
}
