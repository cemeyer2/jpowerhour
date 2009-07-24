package net.charliemeyer.jpowerhour.util.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.charliemeyer.jpowerhour.JPowerHourInterlude;
import net.charliemeyer.jpowerhour.JPowerHourSong;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class SavePlaylist 
{
	public static void savePlaylist(ArrayList<JPowerHourSong> songs, ArrayList<JPowerHourInterlude> interludes, File dst) throws IOException
	{
		if(!dst.getCanonicalPath().endsWith(".jph"))
		{
			dst = new File(dst.getCanonicalPath()+".jph");
		}
		Element root = new Element("jPowerHourPlaylist");
		root.setAttribute("songCount",songs.size()+"");
		root.setAttribute("interludeCount",interludes.size()+"");
        Document doc = new Document(root);
        
        Element songsElement = new Element("songs");
        for(int i = 0; i < songs.size(); i++)
        {
        	JPowerHourSong song = songs.get(i);
        	
        	Element songElement = new Element("jPowerHourSong");
        	
        	songElement.setAttribute("index", i+"");
        	try 
        	{
				songElement.setAttribute("path", song.getSongFile().getCanonicalPath());
			} 
        	catch (IOException e) 
        	{
				e.printStackTrace();
			}
        	songElement.setAttribute("start",song.getStartTime()+"");
        	songElement.setAttribute("length", song.getPlayLengthMs()+"");
        	songsElement.addContent(songElement);
        }
        root.addContent(songsElement);
        
        Element interludesElement = new Element("interludes");
        for(int i = 0; i < interludes.size(); i++)
        {
        	JPowerHourInterlude interlude = interludes.get(i);
        	
        	Element interludeElement = new Element("jPowerHourInterlude");
        	
        	interludeElement.setAttribute("index", i+"");
        	interludeElement.setAttribute("default",interlude.isDefault()+"");
        	if(interlude.isDefault())
        	{
        		interludeElement.setAttribute("defaultNumber", interlude.getDefaultNumber()+"");
        	}
        	else
        	{
	        	try 
	        	{
					interludeElement.setAttribute("path", interlude.getSongFile().getCanonicalPath());
				} 
	        	catch (IOException e) 
	        	{
					e.printStackTrace();
				}
        	}
        	interludesElement.addContent(interludeElement);
        }
        root.addContent(interludesElement);
        
        
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileWriter out = new FileWriter(dst);
        outputter.output(doc, out);
	}
}
