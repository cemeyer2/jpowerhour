package net.charliemeyer.jpowerhour.gui.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.charliemeyer.jpowerhour.util.Extension;

public class MusicFilter extends FileFilter
{
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = Extension.getExtension(f);
		if (extension != null) 
		{
			if (extension.equals(Extension.mp3) || extension.equals(Extension.wav))
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
		return "MP3 and WAV Files";
	}

}
