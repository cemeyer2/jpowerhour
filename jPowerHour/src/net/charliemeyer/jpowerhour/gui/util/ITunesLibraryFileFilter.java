package net.charliemeyer.jpowerhour.gui.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.charliemeyer.jpowerhour.util.Extension;

public class ITunesLibraryFileFilter extends FileFilter
{
	public boolean accept(File f) 
	{
		if (f.isDirectory()) {
			return true;
		}

		if(f.getName().equals("iTunes Music Library.xml"))
		{
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() 
	{
		return "iTunes Music Library Files";
	}

}