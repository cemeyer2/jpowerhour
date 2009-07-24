package net.charliemeyer.jpowerhour;

import java.io.File;

import net.charliemeyer.jpowerhour.util.Wget;

import javazoom.jlgui.basicplayer.BasicPlayerException;

public class JPowerHourInterlude extends JPowerHourSong
{
	private static final String[] DEFAULT_INTERLUDES = {"beer1.wav","beer2.wav","beer3.wav","beer4.wav"};
	private static final String[] DEFAULT_ARTISTS = {"Homer","Homer","Moe","Homer"};
	private static final String[] DEFAULT_TITLES = {"Beer Beer","Love Beer","More Beer","Mmmm Beer"};
	
	private int defaultNumber = -1;
	
	public JPowerHourInterlude(File songFile) throws BasicPlayerException 
	{
		super(songFile);
	}
	
	public JPowerHourInterlude(int defaultNumber) throws BasicPlayerException
	{
		this(Wget.wgetInterlude(JPowerHourInterlude.DEFAULT_INTERLUDES[defaultNumber]));
			
		setArtist(JPowerHourInterlude.DEFAULT_ARTISTS[defaultNumber]);	
		setTitle(JPowerHourInterlude.DEFAULT_TITLES[defaultNumber]);
		
		this.defaultNumber = defaultNumber;
	}
	
	public int getDefaultNumber()
	{
		return defaultNumber;
	}
	
	public boolean isDefault()
	{
		return getDefaultNumber() != -1;
	}
	
	public static int getDefaultInterludeCount()
	{
		return JPowerHourInterlude.DEFAULT_INTERLUDES.length;
	}
}
