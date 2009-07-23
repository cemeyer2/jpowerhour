package net.charliemeyer.jpowerhour.itunes;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.charliemeyer.jpowerhour.gui.util.ITunesLibraryFileFilter;

public class ITunesUtils 
{
	public static File getItunesLibrary()
	{
		String os = System.getProperty("os.name");
		
		if(os.startsWith("Mac OS X"))
		{
			File iTunesDir = new File(System.getProperty("user.home")+File.separator+"Music"+File.separator+"iTunes");
			if(!iTunesDir.exists())
			{
				return promptForITunesLibrary();
			}
			for(File f : iTunesDir.listFiles())
			{
				if(f.getName().equals("iTunes Music Library.xml"))
				{
					return f;
				}
			}
			return promptForITunesLibrary();
		}
		else if(os.startsWith("Windows XP"))
		{
			File iTunesDir = new File(System.getProperty("user.home")+File.separator+"My Documents"+File.separator+"My Music"+File.separator+"iTunes");
			if(!iTunesDir.exists())
			{
				return promptForITunesLibrary();
			}
			for(File f : iTunesDir.listFiles())
			{
				if(f.getName().equals("iTunes Music Library.xml"))
				{
					return f;
				}
			}
			return promptForITunesLibrary();
		}
		return promptForITunesLibrary();
	}
	
	private static File promptForITunesLibrary()
	{
		int retVal = JOptionPane.showConfirmDialog(null, "Could not find iTunes library file, search for it?", "Could Not Find iTunes", JOptionPane.YES_NO_OPTION);
		if(retVal == JOptionPane.YES_OPTION)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new ITunesLibraryFileFilter());
			int retVal2 = chooser.showOpenDialog(null);
			if(retVal2 == JFileChooser.APPROVE_OPTION)
			{
				return chooser.getSelectedFile();
			}
			return null;
		}
		else
		{
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		System.out.println(getItunesLibrary());
	}
}