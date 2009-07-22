package net.charliemeyer.jpowerhour.gui.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.charliemeyer.jpowerhour.util.Extension;

public class JPowerHourPlaylistFilter extends FileFilter
{

	@Override
	public boolean accept(File f) 
	{
		if (f.isDirectory()) {
			return true;
		}

		String extension = Extension.getExtension(f);
		if (extension != null) 
		{
			if (extension.equals(Extension.jph))
			{
				return true;
			} 
			else 
			{
				return false;
			}
		}
		return false;
	}

	@Override
	public String getDescription() 
	{
		return "jPowerHour Playlist Files";
	}

}
