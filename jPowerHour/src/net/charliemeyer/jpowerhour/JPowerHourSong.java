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
	private long playLength;
	private long durationMs;
	private String artist, title;
	Object lock;
	private boolean shortFile = false; //hack
	
	public JPowerHourSong(File songFile) throws BasicPlayerException
	{
		lock = new Object();
		
		this.songFile = songFile;
		this.startTime = 0;
		this.playLength = 60*1000;
		
		JPowerHourAudioPlayer player = JPowerHourAudioPlayer.getJPowerHourPlayer();
		player.stop();
		player.openFile(songFile);
		durationMs = player.getSongLengthInMs();
		artist = player.getArtist();
		title = player.getTitle();
		
		//if the song is less than 1 minute, make the play length the song length
		if(durationMs < playLength)
		{
			playLength = durationMs;
		}
		if(durationMs < 5000)
		{
			shortFile = true;
		}
	}
	
	public void setStartPos(int mins, int sec)
	{
		long ms = (60*mins+sec)*1000;
		
		setStartPos(ms);
	}
	
	public void setStartPos(long ms)
	{
		long lengthMs = playLength;
		//if the requested starting position doesnt leave enough room to play the requested length
		//move the starting pos back playLength number of ms from the end of the song
		if(ms+lengthMs > durationMs)
		{
			ms = durationMs - lengthMs;
		}
		startTime = ms;
	}
	
	public void setPlayLengthMs(long length)
	{
		if(length < 1)
		{
			return;
		}
		else playLength = length;
	}
	
	public void playSong() throws BasicPlayerException
	{
		JPowerHourAudioPlayer player = JPowerHourAudioPlayer.getJPowerHourPlayer();
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
	
	public long getPlayLengthMs()
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
		if(microseconds/1000 >= playLength)
		{
			synchronized(lock)
			{
				if(!shortFile)
				{
					lock.notify();
				}
			}
		}
	}

	@Override
	public void setController(BasicController arg0) {
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent event) {
		if(shortFile)
		{
			if(event.getCode()==BasicPlayerEvent.STOPPED)
			{
				synchronized(lock)
				{
					lock.notify();
				}
			}
		}
	}
	
	public long getDurationMs()
	{
		return durationMs;
	}
	
	public File getSongFile()
	{
		return songFile;
	}

	public boolean isShortFile() {
		return shortFile;
	}

	public void setShortFile(boolean shortFile) {
		this.shortFile = shortFile;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
