package ncrossley.itunes;


/**
 * This is an enumeration of the optional checks performed on each playlist.
 *
 * @see ncrossley.itunes.controls.ChecksControl
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public enum Check
{
	/** Check that a track does not appear more than once in a playlist. */
	duplicates,

	/** Check that a playlist is not empty. */
	empty,

	/**
	 * Check that each track appears in at least one non-master playlist,
	 * excluding non-built-in smart playlists and the built-in 'Music' and
	 * 'Party Shuffle' playlists.
	 */
	nonmaster;


	/**
	 * Get the Check value corresponding to the given string, in any case;
	 * note that this depends on the enum names all being lower case!
	 * @param checkName the name of a check, in any mix of upper or lower case.
	 * @return the Check value corresponding to the given name.
	 * @throws IllegalArgumentException if {@code checkName} does not correspond
	 * to a Check value.
	 */
	public static Check checkValue(String checkName)
	{
		return Check.valueOf(checkName.toLowerCase());
	}
}
