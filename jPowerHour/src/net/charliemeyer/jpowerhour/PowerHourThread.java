package net.charliemeyer.jpowerhour;

import java.util.ArrayList;

import javazoom.jlgui.basicplayer.BasicPlayerException;

public class PowerHourThread implements Runnable 
{
	private ArrayList<PowerHourSong> songs;
	private int currentlyPlayingSong = 0;
	private ArrayList<PowerHourListener> listeners;
	private PowerHourSong currentlyPlaying;
	
	public PowerHourThread()
	{
		this.songs = new ArrayList<PowerHourSong>();
		listeners = new ArrayList<PowerHourListener>();
	}
	
	public void addPowerHourListener(PowerHourListener listener)
	{
		listeners.add(listener);
	}
	
	public void setSongs(ArrayList<PowerHourSong> songs)
	{
		this.songs = songs;
	}
	
	@Override
	public void run() 
	{
		for(int i = 0; i < songs.size(); i++)
		{
			PowerHourSong song = songs.get(i);
			currentlyPlayingSong = i;
			currentlyPlaying = song;
			for(PowerHourListener listener : listeners)
			{
				listener.songChange(song, i);
			}
			
			try
			{
				song.playSong();
			} 
			catch (BasicPlayerException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void pause()
	{
		for(PowerHourListener listener : listeners)
		{
			listener.paused();
		}
		try 
		{
			JPowerHourPlayer.getJPowerHourPlayer().pause();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void resume()
	{
		for(PowerHourListener listener : listeners)
		{
			listener.resumed();
		}
		try
		{
			JPowerHourPlayer.getJPowerHourPlayer().play();
		}
		catch(BasicPlayerException bpe)
		{
			bpe.printStackTrace();
		}
	}
	
	public int getCurrentlyPlayingSong()
	{
		return currentlyPlayingSong;
	}

}
