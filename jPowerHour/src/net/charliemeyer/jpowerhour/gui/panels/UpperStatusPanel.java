package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import net.charliemeyer.jpowerhour.JPowerHourAudioPlayer;
import net.charliemeyer.jpowerhour.JPowerHourListener;
import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.gui.JPowerHourGUI;
import net.charliemeyer.jpowerhour.player.JPowerHourPlayer;

public class UpperStatusPanel extends JPanel implements BasicPlayerListener, JPowerHourListener
{
	private ArrayList<String> texts;
	private JLabel upperLabel;
	private JLabel lowerLeft;
	private JLabel lowerRight;
	private JProgressBar progress;
	private JPowerHourSong currentlyPlayingSong;
	private JPowerHourGUI parent;
	
	public UpperStatusPanel(JPowerHourGUI parent)
	{
		super();
		
		this.parent = parent;
		
		JPowerHourAudioPlayer.getJPowerHourPlayer().addBasicPlayerListener(this);
		parent.getPowerHourThread().addPowerHourListener(this);
		
		setLayout(new GridLayout(2,1));
		
		initializeTexts();
		
		upperLabel = new JLabel("",SwingConstants.CENTER);
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
					if(cur >= texts.size())
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
		},"Upper Label Changer");
		th.start();
	}


	public void opened(Object arg0, Map arg1) {
		progress.setValue(0);
	}


	public void progress(int arg0, long microseconds, byte[] arg2, Map arg3) 
	{
		if(currentlyPlayingSong != null)
		{
			long start = currentlyPlayingSong.getStartTime();
			long end = start + currentlyPlayingSong.getPlayLengthMs();
			long pos = microseconds / 1000;
			int progress = (int) (((double)(pos)/(double)(end-start))*100);
			
			long cur = start+pos;
			int totalSec = (int) (cur/1000);
			
			int mins = totalSec/60;
			int secs = totalSec % 60;
			
			String str = mins+":"+((secs < 10)?"0"+secs : secs);
			
			//this.progress.setValue(progress);
			this.progress.setValue((int) (pos+start));
			this.progress.setString(str);
		}
		
		
	}


	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}


	public void stateUpdated(BasicPlayerEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void powerHourPaused() {
		// TODO Auto-generated method stub
		
	}


	public void powerHourResumed() {
		// TODO Auto-generated method stub
		
	}


	public void songChange(JPowerHourSong currentlyPlaying, int currentlyPlayingNumber) 
	{
		currentlyPlayingSong = currentlyPlaying;
		long start = currentlyPlaying.getStartTime();
		long duration = currentlyPlaying.getPlayLengthMs();
		
		int startTotalSeconds = (int) (start/1000);
		int startMins = startTotalSeconds / 60;
		int startSecs = startTotalSeconds % 60;
		
		int endTotalSeconds = startTotalSeconds + (int)(duration/1000);
		int endMins = endTotalSeconds / 60;
		int endSecs = endTotalSeconds % 60;
		
//		System.out.println(currentlyPlaying.getSongFile().getName());
//		System.out.println("start: "+start);
//		System.out.println("duration: "+duration);
//		System.out.println();
		
		this.progress.setMinimum((int) currentlyPlaying.getStartTime());
		this.progress.setMaximum((int) (currentlyPlaying.getStartTime()+currentlyPlaying.getPlayLengthMs()));
		
		String startStr = startMins+":"+((startSecs < 10)?"0"+startSecs : startSecs);
		String endStr = endMins+":"+((endSecs < 10)?"0"+endSecs : endSecs);
		
		lowerLeft.setText(startStr);
		lowerRight.setText(endStr);
		
		int songCount = parent.getSongListPanel().getPowerHourSongCount();
		
		initializeTexts();
		texts.add("Currently Playing ("+(currentlyPlayingNumber+1)+"/"+songCount+"): "+currentlyPlayingSong.toString());
		ArrayList<JPowerHourPlayer> players = parent.getPowerHourThread().getPlayers();
		for(JPowerHourPlayer player : players)
		{
			double bac = player.computeBAC();
			texts.add("BAC for "+player.getName()+": "+bac);
		}
	}
	
	private void initializeTexts()
	{
		texts = new ArrayList<String>();
		texts.add("Welcome to jPowerHour");
	}


	public void powerHourFinished() {
		initializeTexts();
		texts.add("Power Hour Complete!");
		progress.setValue(0);
		progress.setString("Power Hour Complete!");
		lowerLeft.setText("0:00");
		lowerRight.setText("0:00");
	}


	public void powerHourStarted() {
		// TODO Auto-generated method stub
		
	}
}
