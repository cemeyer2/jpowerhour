package net.charliemeyer.jpowerhour;

import java.io.File;

import javazoom.jlgui.basicplayer.BasicPlayerException;

public class PowerHourSong 
{
	private long startTime;
	private File songFile;
	private int playLength;
	private long durationMs;
	private String artist, title;
	
	public PowerHourSong(File songFile) throws BasicPlayerException
	{
		this.songFile = songFile;
		this.startTime = 0;
		this.playLength = 60;
		
		JPowerHourPlayer player = JPowerHourPlayer.getJPowerHourPlayer();
		player.stop();
		player.openFile(songFile);
		durationMs = player.getSongLengthInMs();
		artist = player.getArtist();
		title = player.getTitle();
	}
	
	public void setStartPos(int mins, int sec) throws BasicPlayerException
	{
		long ms = (60*mins+sec)*1000;
		
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
		player.stop();
		player.openFile(songFile);
		player.seek(startTime);
		player.play(playLength);
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
}
