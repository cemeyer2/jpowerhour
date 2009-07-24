package net.charliemeyer.jpowerhour.util.xml;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import net.charliemeyer.jpowerhour.JPowerHourInterlude;
import net.charliemeyer.jpowerhour.JPowerHourSong;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class LoadPlaylist 
{
	public static ArrayList<JPowerHourSong> loadPlaylistSongs(File src) throws JDOMException, IOException
	{
		SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(src);
        Element root = doc.getRootElement();
        
        int songCount = root.getAttribute("songCount").getIntValue();
        
        ArrayList<JPowerHourSong> songs = new ArrayList<JPowerHourSong>(songCount);
        
        Element songsElement = root.getChild("songs");
        List songElements = songsElement.getChildren();
        for(int i = 0; i < songElements.size(); i++)
        {
        	Element songElement = (Element) songElements.get(i);
        	
        	int index = songElement.getAttribute("index").getIntValue();
        	File file = new File(songElement.getAttribute("path").getValue());
        	long start = songElement.getAttribute("start").getLongValue();
        	long length = songElement.getAttribute("length").getLongValue();
        	
        	if(!file.exists())
        	{
        		String message = "Song "+file.getName()+" does not exist, removing from playlist";
        		try
        		{
        			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        		}
        		catch(HeadlessException he)
        		{
        			System.err.println(message);
        		}
        	}
        	else
        	{
	        	try 
	        	{
					JPowerHourSong song = new JPowerHourSong(file);
					song.setPlayLengthMs(length);
					song.setStartPos(start);
					songs.add(index, song);
				} 
	        	catch (BasicPlayerException e) 
	        	{
					e.printStackTrace();
				}
        	}
        }
        
        return songs;
	}
	
	public static ArrayList<JPowerHourInterlude> loadPlaylistInterludes(File src) throws JDOMException, IOException
	{
		SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(src);
        Element root = doc.getRootElement();
        
        int interludeCount = root.getAttribute("interludeCount").getIntValue();
        
        ArrayList<JPowerHourInterlude> interludes = new ArrayList<JPowerHourInterlude>(interludeCount);
        
        Element interludesElement = root.getChild("interludes");
        List interludeElements = interludesElement.getChildren();
        for(int i = 0; i < interludeElements.size(); i++)
        {
        	Element interludeElement = (Element) interludeElements.get(i);
        	
        	int index = interludeElement.getAttribute("index").getIntValue();
        	boolean def = interludeElement.getAttribute("default").getBooleanValue();
        	if(def)
        	{
        		int defaultNumber = interludeElement.getAttribute("defaultNumber").getIntValue();
        		JPowerHourInterlude interlude;
				try 
				{
					interlude = new JPowerHourInterlude(defaultNumber);
					interludes.add(interlude);
				} 
				catch (BasicPlayerException e) 
				{
					e.printStackTrace();
				}
        		
        	}
        	else
        	{
        		File file = new File(interludeElement.getAttribute("path").getValue());
            	
            	if(!file.exists())
            	{
            		String message = "Interlude "+file.getName()+" does not exist, removing from playlist";
            		try
            		{
            			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            		}
            		catch(HeadlessException he)
            		{
            			System.err.println(message);
            		}
            	}
            	else
            	{
    	        	try 
    	        	{
    					JPowerHourInterlude interlude = new JPowerHourInterlude(file);
    					interludes.add(index, interlude);
    				} 
    	        	catch (BasicPlayerException e) 
    	        	{
    					e.printStackTrace();
    				}
            	}
        	}
        	
        }
        
        return interludes;
	}
}
