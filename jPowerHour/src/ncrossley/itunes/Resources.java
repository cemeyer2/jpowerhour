package ncrossley.itunes;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * String resource handler for ITunes program.
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public final class Resources
{
	/**
	 * This is the name of the iTunes message resource bundle.
	 * It has default access so it can be used by the getLogger call in ITunesLogger;
	 * a getter function seems overkill for this simple read-only access.
	 */
	static final String BUNDLE_NAME = Resources.class.getPackage().getName() + ".itunes"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);


	private Resources()
	{
		// No instances of this class should ever be created
	}


	/**
	 * Return a string from the resource property file in this same bundle.
	 * @param key The resource key
	 * @return The (possibly translated) string
	 */
	public static String getString(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			ITunesLogger.getLogger().log(Level.WARNING,"itunes.dbg.missing_resource",e); //$NON-NLS-1$
			return '!' + key + '!';
		}
	}


	/**
	 * Return a string from the resource property file in this same bundle,
	 * with any number of parameters inserted.
	 * @param key The resource key
	 * @param param the parameters to insert
	 * @return The (possibly translated) string
	 */
	public static String getString(String key, Object... param)
	{
		try
		{
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key),param);
		}
		catch (MissingResourceException e)
		{
			ITunesLogger.getLogger().log(Level.WARNING,"itunes.dbg.missing_resource",e); //$NON-NLS-1$
			return '!' + key + '!' + Arrays.deepToString(param) + '!';
		}
	}


}
