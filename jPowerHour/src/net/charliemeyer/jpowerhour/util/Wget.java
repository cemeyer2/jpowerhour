package net.charliemeyer.jpowerhour.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.server.UID;

public class Wget 
{
	public static File wgetInterlude(String fileName)
	{
		try
		{
			URLConnection con;
			UID uid = new UID();

			con = new URL("http://jpowerhour.sourceforge.net/interludes/"+fileName).openConnection();
			con.connect();

			byte[] buffer = new byte[4 * 1024];
			int read;

			File f = File.createTempFile("jph", "."+Extension.getExtension(new File(fileName)));
			f.deleteOnExit();
			FileOutputStream os = new FileOutputStream(f);
			InputStream in = con.getInputStream();

			while ((read = in.read(buffer)) > 0) 
			{
				os.write(buffer, 0, read);
			}

			os.close();
			in.close();
			return f;
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		wgetInterlude("beer1.wav");
	}
}
