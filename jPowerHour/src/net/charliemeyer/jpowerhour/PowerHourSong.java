package net.charliemeyer.jpowerhour;

import java.io.File;

import javazoom.jlgui.basicplayer.BasicPlayerException;

public class PowerHourSong 
{
	private long startTime;
	private File songFile;
	private int playLength;
	
	public PowerHourSong(File songFile)
	{
		this.songFile = songFile;
		this.startTime = 0;
		this.playLength = 60;
	}
	
	public void setStartPos(int mins, int sec) throws BasicPlayerException
	{
		long ms = (60*mins+sec)*1000;
		JPowerHourPlayer player = JPowerHourPlayer.getJPowerHourPlayer();
		player.stop();
		player.openFile(songFile);
		long songMs = player.getSongLengthInMs();
		long lengthMs = playLength * 1000;
		//if the requested starting position doesnt leave enough room to play the requested length
		//move the starting pos back playLength number of ms from the end of the song
		if(ms+lengthMs > songMs)
		{
			ms = songMs - lengthMs;
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
}
