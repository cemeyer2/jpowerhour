package ncrossley.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;


/**
 * Get the path name of the containing jar file.
 *
 * @author ndjc
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public final class JarUtilities
{
	private static final String FILE_PROTOCOL = "file"; //$NON-NLS-1$
	private static final String JAR_SUFFIX    = ".jar"; //$NON-NLS-1$

	private String jarPath = null;


	/**
	 * Construct a new JarUtilities for the jar file from which this code itself
	 * was loaded.  This allows code to inspect its own jar file, if any.
	 */
	public JarUtilities()
	{
		this(JarUtilities.class);
	}


	/**
	 * Construct a new JarUtilities for the jar file from which the the given class
	 * was loaded, if any.
	 * @param cls the class whose containing jar file is to be examined
	 */
	public JarUtilities(Class<?> cls)
	{
		CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
		if (codeSource != null)
		{
			URL url = codeSource.getLocation();
			if (url.getProtocol().equals(FILE_PROTOCOL))
			{
				String path = url.getPath();
				if (new File(path).exists() && path.endsWith(JAR_SUFFIX))
				{
					jarPath = path;
				}
			}
		}
	}


	/**
	 * Construct a new JarUtilities for a named jar file.
	 * @param jarPath the jar file to be examined
	 */
	public JarUtilities(String jarPath)
	{
		this.jarPath = jarPath;
	}


	/**
	 * Get the path to the jar file indentifed in the constructor.
	 * @return the path to the jar file if known; null otherwise
	 */
	public String getJarPath()
	{
		return jarPath;
	}


	/**
	 * Get a string value from a main attribute in the jar manifest.
	 * @param attrName the attribute name
	 * @return the attribute value, or null if the attribute is not set
	 * @throws IOException if the jar manifest cannot be read
	 */
	public String getManifestString(String attrName) throws IOException
	{
		if (jarPath == null)
		{
			return null;
		}
		else
		{
			JarFile jar = new JarFile(jarPath);
			Manifest manifest = jar.getManifest();
			Attributes attrs = manifest.getMainAttributes();
			return attrs.getValue(attrName);
		}
	}


	/**
	 * Get a long value from a main attribute in the jar manifest,
	 * allowing a suffix K or M, to mean a multiplier of 1024 or 1024*1024
	 * respectively.
	 * @param attrName the attribute name
	 * @return the attribute value, if set
	 * @throws IOException if the jar manifest cannot be read
	 * @throws NumberFormatException if the format of the long value is incorrect
	 */
	public long getManifestLong(String attrName) throws IOException
	{
		String strval = getManifestString(attrName);
		if (strval == null)
		{
			throw new IOException("No manifest field "+attrName+" in "+jarPath); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			int last = strval.length() - 1;
			long multiplier = 1L;
			char suffix = strval.charAt(last);
			if (suffix == 'K' || suffix == 'k')
			{
				multiplier = 1024L;
				strval = strval.substring(0,last);
			}
			else if (suffix == 'M' || suffix == 'm')
			{
				multiplier = 1024L * 1024L;
				strval = strval.substring(0,last);
			}
			return Long.parseLong(strval) * multiplier;
		}
	}
}
