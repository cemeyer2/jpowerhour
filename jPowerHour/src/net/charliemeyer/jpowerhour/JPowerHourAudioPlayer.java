package net.charliemeyer.jpowerhour;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class JPowerHourAudioPlayer implements BasicPlayerListener
{
	private static JPowerHourAudioPlayer jPowerHourPlayer;
	
	//singleton pattern
	public static JPowerHourAudioPlayer getJPowerHourPlayer()
	{
		if(jPowerHourPlayer == null)
			jPowerHourPlayer = new JPowerHourAudioPlayer();
		return jPowerHourPlayer;
	}
	
	private BasicPlayer player;
	private Map audioInfo;
	Thread playThread;
	
	public JPowerHourAudioPlayer()
	{
		player = new BasicPlayer();
		player.addBasicPlayerListener(this);
	}
	
	public void openFile(File f) throws BasicPlayerException
	{
		player.open(f);
	}
	
	public int getSongLengthInBytes()
	{
		if(audioInfo != null)
		{
			if(audioInfo.containsKey("audio.length.bytes"))
			{
				return ((Integer)audioInfo.get("audio.length.bytes")).intValue();
			}
		}
		return -1;
	}
	
	public long getSongLengthInMs()
    {
        long milliseconds = -1;
        int byteslength = -1;
        if (audioInfo != null)
        {
            if (audioInfo.containsKey("audio.length.bytes"))
            {
                byteslength = ((Integer) audioInfo.get("audio.length.bytes")).intValue();
            }
            if (audioInfo.containsKey("duration"))
            {
                milliseconds = (int) (((Long) audioInfo.get("duration")).longValue()) / 1000;
            }
            else
            {
                // Try to compute duration
                int bitspersample = -1;
                int channels = -1;
                float samplerate = -1.0f;
                int framesize = -1;
                if (audioInfo.containsKey("audio.samplesize.bits"))
                {
                    bitspersample = ((Integer) audioInfo.get("audio.samplesize.bits")).intValue();
                }
                if (audioInfo.containsKey("audio.channels"))
                {
                    channels = ((Integer) audioInfo.get("audio.channels")).intValue();
                }
                if (audioInfo.containsKey("audio.samplerate.hz"))
                {
                    samplerate = ((Float) audioInfo.get("audio.samplerate.hz")).floatValue();
                }
                if (audioInfo.containsKey("audio.framesize.bytes"))
                {
                    framesize = ((Integer) audioInfo.get("audio.framesize.bytes")).intValue();
                }
                if (bitspersample > 0)
                {
                    milliseconds = (int) (1000.0f * byteslength / (samplerate * channels * (bitspersample / 8)));
                }
                else
                {
                    milliseconds = (int) (1000.0f * byteslength / (samplerate * framesize));
                }
            }
        }
        return milliseconds;
    }
	
	public void play() throws BasicPlayerException
	{
		player.play();
	}
	
	public void pause() throws BasicPlayerException
	{
		player.pause();
	}
	
	public void resume() throws BasicPlayerException
	{
		player.resume();
	}
	
	public void play(int seconds) throws BasicPlayerException
	{
		player.play();
		try 
		{
			Thread.sleep(seconds*1000);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		player.stop();
	}
	
	public void stop() throws BasicPlayerException
	{
		player.stop();
	}
	
	public void seek(int mins, int sec) throws BasicPlayerException
	{
		sec += 60*mins;
		long ms = 1000*sec;
		seek(ms);
	}
	
	public void seek(long ms) throws BasicPlayerException
	{
		if(ms > this.getSongLengthInMs())
			return;
		else
		{
			double rate = (double)ms / (double)this.getSongLengthInMs();
			if(audioInfo.containsKey("audio.length.bytes"))
            {
                long skipBytes = (long) Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue() * rate);
                player.seek(skipBytes);
            }
		}
	}
	
	public String getTitle()
	{
		if(audioInfo != null)
		{
			if(audioInfo.containsKey("title"))
			{
				return (String) audioInfo.get("title");
			}
		}
		return "";
	}
	
	public String getArtist()
	{
		if(audioInfo != null)
		{
			if(audioInfo.containsKey("author"))
			{
				return (String) audioInfo.get("author");
			}
		}
		return "";
	}

	public void opened(Object arg0, Map properties) 
	{
		audioInfo = properties;
//		for(Object key : properties.keySet())
//		{
//			System.out.println(key);
//		}
	}

	public void progress(int bytesRead, long microsecondsElapsed, byte[] pcmdata, Map properties) 
	{
		audioInfo = properties;
	}

	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}

	public void stateUpdated(BasicPlayerEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void addBasicPlayerListener(BasicPlayerListener listener)
	{
		player.addBasicPlayerListener(listener);
	}
	
	public void removeBasicPlayerListener(BasicPlayerListener listener)
	{
		player.removeBasicPlayerListener(listener);
	}
	
	public int getBasicPlayerStatus()
	{
		return player.getStatus();
	}
}
