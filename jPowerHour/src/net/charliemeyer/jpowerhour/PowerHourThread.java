package net.charliemeyer.jpowerhour;

import java.util.ArrayList;

import javazoom.jlgui.basicplayer.BasicPlayerException;

public class PowerHourThread implements Runnable 
{
	private ArrayList<JPowerHourSong> songs;
	private int currentlyPlayingSong = 0;
	private ArrayList<JPowerHourListener> listeners;
	private JPowerHourSong currentlyPlaying;
	
	public PowerHourThread()
	{
		this.songs = new ArrayList<JPowerHourSong>();
		listeners = new ArrayList<JPowerHourListener>();
	}
	
	public void addPowerHourListener(JPowerHourListener listener)
	{
		listeners.add(listener);
	}
	
	public void setSongs(ArrayList<JPowerHourSong> songs)
	{
		this.songs = songs;
	}
	
	@Override
	public void run() 
	{
		for(int i = 0; i < songs.size(); i++)
		{
			JPowerHourSong song = songs.get(i);
			currentlyPlayingSong = i;
			currentlyPlaying = song;
			for(JPowerHourListener listener : listeners)
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
		for(JPowerHourListener listener : listeners)
		{
			listener.finished();
		}
	}
	
	public void stop()
	{
		try 
		{
			JPowerHourPlayer.getJPowerHourPlayer().stop();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
		for(JPowerHourListener listener : listeners)
		{
			listener.finished();
		}
	}
	
	public void pause()
	{
		for(JPowerHourListener listener : listeners)
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
		for(JPowerHourListener listener : listeners)
		{
			listener.resumed();
		}
		try
		{
			JPowerHourPlayer.getJPowerHourPlayer().resume();
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
