package ncrossley.itunes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.MissingResourceException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;


/**
 * Singleton Logger class for ITunes program.
 * By default, the logging configuration is left to the JRE defaults
 * (which, out of the box, is to show INFO messages to the console).
 * However, if the system property or environment variable {@code ituneschecker.debug} exists,
 * or if a file of that name exists in the current directory,
 * then full logging is enabled to a file {@code ituneschecker_log.txt} in the current directory.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public final class ITunesLogger
{
	private static final String ITUNESCHECKER_LOG_TXT	= "ituneschecker_log.txt";	//$NON-NLS-1$
	private static final String	ITUNESCHECKER_DEBUG		= "ituneschecker.debug";	//$NON-NLS-1$

	private static final ITunesLogger INSTANCE = new ITunesLogger();
	private Logger logger;

	private ITunesLogger()
	{
		try
		{
			logger = Logger.getLogger(this.getClass().getPackage().getName(), Resources.BUNDLE_NAME);
		}
		catch (MissingResourceException mre)
		{
			logger = Logger.getLogger(this.getClass().getPackage().getName());
			logger.config(mre.toString());
		}

		if (isDebug())
		{
			debugLogger();
		}
	}


	private boolean isDebug()
	{
		return System.getProperty(ITUNESCHECKER_DEBUG) != null
			|| System.getenv(ITUNESCHECKER_DEBUG) != null
			|| System.getenv(ITUNESCHECKER_DEBUG.toUpperCase()) != null
			|| new File(ITUNESCHECKER_DEBUG).exists();
	}


	private void debugLogger()
	{
		Handler handler;
		try
		{
			OutputStream oStream = new FileOutputStream(ITUNESCHECKER_LOG_TXT,true);
			handler = new StreamHandler(oStream,new SimpleFormatter());
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			return;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		logger.setLevel(Level.ALL);
	}


	/**
	 * Return the singleton ITunes logger instance.
	 * @return the singleton ITunes logger instance.
	 */
	public static Logger getLogger()
	{
		return INSTANCE.logger;
	}
}
