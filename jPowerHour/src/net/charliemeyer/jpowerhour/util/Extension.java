package net.charliemeyer.jpowerhour.util;

import java.io.File;

public class Extension 
{

	public final static String mp3 = "mp3";
	public final static String wav = "wav";
	public final static String jph = "jph";

	/*
	 * Get the extension of a file.
	 */  
	public static String getExtension(File f) 
	{
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		if(ext == null)
			ext = "";
		return ext;
	}

}
