package ncrossley.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A collection of file handling utilities.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public final class FileUtils
{
	private FileUtils()
	{
		// No instantiation
	}


	/**
	 * Get a File from the path part of a "file:" URL, decoding appropriately.
	 * @param url a URL using the "file:" protocol.
	 * @return a File converted from the decoded path of the given URL.
	 * If the canonical path to this file can be found, that is used;
	 * if not, the path as given in the URL is used.
	 * @throws MalformedURLException of the given URL is not valid.
	 */
	public static File urlToFile(URL url) throws MalformedURLException
	{
		if (!url.getProtocol().equals("file")) //$NON-NLS-1$
		{
			throw new MalformedURLException(url.toString());
		}

		String filePath;
		try
		{
			filePath = url.toURI().getPath();
		}
		catch (URISyntaxException e1)
		{
			throw new MalformedURLException(url.toString());
		}
		File file = new File(filePath);
		try
		{
			return file.getCanonicalFile();
		}
		catch (IOException e)
		{
			return file;
		}
	}
}
