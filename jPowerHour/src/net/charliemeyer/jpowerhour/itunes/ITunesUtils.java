package net.charliemeyer.jpowerhour.itunes;

import java.io.File;

public class ITunesUtils 
{
	public static File getItunesLibrary()
	{
		String os = System.getProperty("os.name");
		System.out.println(os);
		
		if(os.startsWith("Mac OS X"))
		{
			File iTunesDir = new File(System.getProperty("user.home")+File.pathSeparator+"Music"+File.pathSeparator+"iTunes");
			if(!iTunesDir.exists())
			{
				return null;
			}
			for(File f : iTunesDir.listFiles())
			{
				if(f.getName().equals("iTunes Music Library.xml"))
				{
					return f;
				}
			}
			return null;
		}
		else if(os.startsWith("Windows"))
		{
			//find itunes windows
		}
		
		return null;
	}
	
	public static void main(String[] args)
	{
		System.out.println(getItunesLibrary());
	}
}