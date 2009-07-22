package net.charliemeyer.jpowerhour;

import java.io.File;
import java.util.Map;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class JPowerHourSong implements BasicPlayerListener
{
	private long startTime;
	private File songFile;
	private int playLength;
	private long durationMs;
	private String artist, title;
	Object lock;
	
	public JPowerHourSong(File songFile) throws BasicPlayerException
	{
		lock = new Object();
		
		this.songFile = songFile;
		this.startTime = 0;
		this.playLength = 10;
		
		JPowerHourPlayer player = JPowerHourPlayer.getJPowerHourPlayer();
		player.stop();
		player.openFile(songFile);
		durationMs = player.getSongLengthInMs();
		artist = player.getArtist();
		title = player.getTitle();
	}
	
	public void setStartPos(int mins, int sec)
	{
		long ms = (60*mins+sec)*1000;
		
		setStartPos(ms);
	}
	
	public void setStartPos(long ms)
	{
		long lengthMs = playLength * 1000;
		//if the requested starting position doesnt leave enough room to play the requested length
		//move the starting pos back playLength number of ms from the end of the song
		if(ms+lengthMs > durationMs)
		{
			ms = durationMs - lengthMs;
		}
		startTime = ms;
	}
	
	public void setPlayLength(int length)
	{
		if(length < 1)
		{
			return;
		}
		else playLength = length;
	}
	
	public void playSong() throws BasicPlayerException
	{
		JPowerHourPlayer player = JPowerHourPlayer.getJPowerHourPlayer();
		player.addBasicPlayerListener(this);
		player.stop();
		player.openFile(songFile);
		player.seek(startTime);
		player.play();
		try 
		{
			synchronized(lock)
			{
				lock.wait();
			}
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		player.stop();
		player.removeBasicPlayerListener(this);
	}
	
	public String toString()
	{
		return artist+" - "+title;
	}
	
	public String getArtist()
	{
		return artist;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public int getPlayLength()
	{
		return playLength;
	}
	
	public long getStartTime()
	{
		return startTime;
	}

	@Override
	public void opened(Object arg0, Map arg1) {
		
	}

	@Override
	public void progress(int arg0, long microseconds, byte[] arg2, Map arg3) {
		if(microseconds/1000 >= playLength*1000)
		{
			synchronized(lock)
			{
				lock.notify();
			}
		}
	}

	@Override
	public void setController(BasicController arg0) {
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent arg0) {
		
	}
	
	public long getDurationMs()
	{
		return durationMs;
	}
}
