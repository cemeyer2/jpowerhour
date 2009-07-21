package net.charliemeyer.jpowerhour.cmdline;

import jargs.gnu.CmdLineParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import net.charliemeyer.jpowerhour.JPowerHourPlayer;
import net.charliemeyer.jpowerhour.PowerHourSong;

public class JPowerHourCmdLine 
{
	File playlistFile;
	int songLength;
	ArrayList<PowerHourSong> songsToPlay;
	
	public JPowerHourCmdLine(File playlistFile, int songLength)
	{
		this.playlistFile = playlistFile;
		this.songLength = songLength;
		songsToPlay = new ArrayList<PowerHourSong>();
		initSongsList();
	}
	
	private void initSongsList()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(playlistFile));
			String line = "";
			while((line = in.readLine()) != null)
			{
				String[] split = line.split(" ");
				String path = split[0];
				int mins = 0, sec = 0;
				if(split.length > 1)
				{
					String[] split2 = split[1].split(":");
					if(split2.length > 1)
					{
						mins = Integer.parseInt(split2[0]);
						sec = Integer.parseInt(split2[1]);
					}
				}
				File f = new File(path);
				if(!f.exists())
				{
					try
					{
						path = f.getCanonicalPath();
					}
					catch(IOException ioe)
					{
						//do nothing
					}
					System.err.println("Error, "+path+" does not exist, not adding to power hour");
				}
				else
				{
					PowerHourSong song = new PowerHourSong(f);
					song.setPlayLength(songLength);
					try
					{
						song.setStartPos(mins, sec);
					}
					catch(BasicPlayerException bpe)
					{
						//do nothing
					}
					songsToPlay.add(song);
				}
			}
		}
		catch(IOException ioe)
		{
			System.err.println(ioe.getMessage());
			System.err.println("Error, could not read playlist file");
		}
	}
	
	public void runPowerHour()
	{
		for(int songNumber = 0; songNumber < songsToPlay.size(); songNumber++)
		{
			PowerHourSong song = songsToPlay.get(songNumber);
			System.out.println("Playing song "+(songNumber+1)+" of "+songsToPlay.size());
			try
			{
				song.playSong();
			}
			catch(BasicPlayerException bpe)
			{
				bpe.printStackTrace();
			}
		}
	}
	
	private static void printUsage() 
	{
        System.err.println("Usage: JPowerHourCmdLine [{-1, --length} song_length] [{-p, --playlist} playlist_file]");
    }
 
	
	public static void main(String[] args)
	{
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option lengthOption = parser.addIntegerOption('l', "length");
        CmdLineParser.Option playlistOption = parser.addStringOption('p', "playlist");
        
        try 
        {
            parser.parse(args);
        }
        catch(CmdLineParser.OptionException e) 
        {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }
        
        Integer lengthValue = (Integer)parser.getOptionValue(lengthOption, new Integer(10));
        int length = lengthValue.intValue();
        
        String playlist = (String)parser.getOptionValue(playlistOption, "jPowerHour.jph");
        
        File playlistFile = new File(playlist);
        if(!playlistFile.exists())
        {
        	String path = playlist;
        	try 
        	{
				path = playlistFile.getCanonicalPath();
			} 
        	catch (IOException e) 
        	{
				e.printStackTrace();
			}
        	
        	System.err.println("Error, playlist file "+path+" does not exist");
        	System.exit(2);
        }
        
        JPowerHourCmdLine jpowerhour = new JPowerHourCmdLine(playlistFile, length);
        jpowerhour.runPowerHour();
	}
}
