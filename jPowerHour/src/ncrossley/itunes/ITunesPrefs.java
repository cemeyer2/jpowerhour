package ncrossley.itunes;

import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * A ITunesPrefs object represents the current user's preferences for the ITunesChecker program.
 * This uses the Singleton and Facade patterns; the class is a singleton, and implements a facade
 * to the Preferences class.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public final class ITunesPrefs
{
	private static final ITunesPrefs INSTANCE = new ITunesPrefs();

	private final Preferences userPrefs;

	// Provide private constructor to prevent other classes creating ITunesPrefs
	private ITunesPrefs()
	{
		userPrefs = Preferences.userNodeForPackage(this.getClass());
	}


	/**
	 * Puts a long integer value into the preferences node.
	 * @param key key with which the string form of value is to be associated.
	 * @param value value whose string form is to be associated with key.
	 * @see Preferences#putLong(java.lang.String, long)
	 * @see #getLong(String, long)
	 */
	public void putLong(String key, long value)
	{
		ITunesLogger.getLogger().log(Level.FINER,"itunes.dbg.save_pref_long", //$NON-NLS-1$
			new String[] {key,Long.toString(value)});
		userPrefs.putLong(key,value);
	}


	/**
	 * Puts a string value into the preferences node.
	 * @param key key with which the specified value is to be associated.
	 * @param value value to be associated with the specified key.
	 * @see Preferences#put(java.lang.String, java.lang.String)
	 * @see #getString(String, String)
	 */
	public void putString(String key, String value)
	{
		ITunesLogger.getLogger().log(Level.FINER,
				"itunes.dbg.save_pref_string", //$NON-NLS-1$
				new String[] {key,value});
		userPrefs.put(key,value);
	}


	/**
	 * Gets a long integer value from the preferences node.
	 * @param key key with which the string form of value is associated.
	 * @param defaultValue value to be returned if the preference is not set or cannot be read.
	 * @return the value of the specified key, or the default value if the preference is not set or cannot be read.
	 * @see Preferences#getLong(java.lang.String, long)
	 * @see #putLong(String, long)
	 */
	public long getLong(String key, long defaultValue)
	{
		long value = userPrefs.getLong(key,defaultValue);
		ITunesLogger.getLogger().log(Level.FINER,"itunes.dbg.read_pref_long", //$NON-NLS-1$
			new String[] {key,Long.toString(value)});
		return value;
	}


	/**
	 * Gets a string value from the preferences node.
	 * @param key key with which the specified value is associated.
	 * @param defaultValue value to be returned if the preference is not set or cannot be read.
	 * @return the value of the specified key, or the default value if the preference is not set or cannot be read.
	 * @see Preferences#get(java.lang.String, java.lang.String)
	 * @see #getString(java.lang.String)
	 * @see #putString(String, String)
	 */
	public String getString(String key, String defaultValue)
	{
		String value = userPrefs.get(key,defaultValue);
		ITunesLogger.getLogger().log(Level.FINER,
			"itunes.dbg.read_pref_string",new String[] {key,value}); //$NON-NLS-1$
		return value;
	}


	/**
	 * Gets a string value from the preferences node, with an empty string default.
	 * @param key key with which the specified value is associated.
	 * @return the value of the specified key, or an empty string if the preference is not set or cannot be read.
	 * @see #getString(java.lang.String, java.lang.String)
	 * @see #putString(String, String)
	 */
	public String getString(String key)
	{
		return getString(key,""); //$NON-NLS-1$
	}


	/**
	 * Writes modified nodes to backing store.
	 * @see Preferences#flush()
	 */
	public void flush()
	{
		try
		{
			userPrefs.flush();
		}
		catch (BackingStoreException bse)
		{
			ITunesLogger.getLogger().log(Level.WARNING,"itunes.msgs.pref_bse",bse); //$NON-NLS-1$
		}
	}


	/**
	 * Returns the singleton ITunesPrefs object,
	 * representing the current user's preferences for the ITunes program.
	 * @return the singleton ITunesPrefs object.
	 */
	public static ITunesPrefs getInstance()
	{
		return INSTANCE;
	}
}
