package net.charliemeyer.jpowerhour.util.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.charliemeyer.jpowerhour.JPowerHourSong;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class SavePlaylist 
{
	public static void savePlaylist(ArrayList<JPowerHourSong> songs, File dst) throws IOException
	{
		if(!dst.getCanonicalPath().endsWith(".jph"))
			dst = new File(dst.getCanonicalPath()+".jph");
		Element root = new Element("jPowerHourPlaylist");
		root.setAttribute("songCount",songs.size()+"");
        Document doc = new Document(root);
        
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
        	songElement.setAttribute("length", song.getPlayLength()+"");
        	root.addContent(songElement);
        }
        
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileWriter out = new FileWriter(dst);
        outputter.output(doc, out);
	}
}
