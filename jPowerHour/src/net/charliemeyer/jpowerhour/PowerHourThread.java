package net.charliemeyer.jpowerhour;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import net.charliemeyer.jpowerhour.player.JPowerHourPlayer;

public class PowerHourThread implements Runnable 
{
	private ArrayList<JPowerHourSong> songs;
	private int currentlyPlayingSong = 0;
	private ArrayList<JPowerHourListener> listeners;
	private JPowerHourSong currentlyPlaying;
	private ArrayList<JPowerHourPlayer> players;

	public PowerHourThread()
	{
		this.songs = new ArrayList<JPowerHourSong>();
		listeners = new ArrayList<JPowerHourListener>();
		players = new ArrayList<JPowerHourPlayer>();
	}

	public void addPowerHourListener(JPowerHourListener listener)
	{
		listeners.add(listener);
	}

	public void addPlayer(JPowerHourPlayer player)
	{
		players.add(player);
		this.addPowerHourListener(player);
	}

	public ArrayList<JPowerHourPlayer> getPlayers()
	{
		return players;
	}

	public void clearPlayers()
	{
		players.clear();
	}

	public boolean removePlayer(JPowerHourPlayer player)
	{
		return players.remove(player);
	}

	public void setSongs(ArrayList<JPowerHourSong> songs)
	{
		this.songs = songs;
	}

	@Override
	public void run() 
	{
		for(JPowerHourListener listener : listeners)
		{
			listener.powerHourStarted();
		}
		try
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


				song.playSong();
			}

		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
			String message = "Unable to open audio device.\nTry quitting all other programs that may be using the audio device";
			JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
		}

		for(JPowerHourListener listener : listeners)
		{
			listener.powerHourFinished();
		}
	}

	public void stop()
	{
		try 
		{
			JPowerHourAudioPlayer.getJPowerHourPlayer().stop();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
		for(JPowerHourListener listener : listeners)
		{
			listener.powerHourFinished();
		}
	}

	public void pause()
	{
		for(JPowerHourListener listener : listeners)
		{
			listener.powerHourPaused();
		}
		try 
		{
			JPowerHourAudioPlayer.getJPowerHourPlayer().pause();
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
			listener.powerHourResumed();
		}
		try
		{
			JPowerHourAudioPlayer.getJPowerHourPlayer().resume();
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
