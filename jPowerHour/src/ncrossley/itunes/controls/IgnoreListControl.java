package ncrossley.itunes.controls;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ncrossley.itunes.ITunesLogger;

/**
 * An IgnoreListControl handles the IGNORE LIST control file entries.
 * The IGNORE LIST entries are optional; there may be any number of them.
 * For each IGNORE LIST entry, the contents of the specified playlist are
 * removed from DIR cross-check warnings, except when checking that named
 * playlist (if the IGNORE LIST entry is also specified as a DIR entry).
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class IgnoreListControl extends AbstractControl
{
	private Set<String> ignoreLists = new HashSet<String>();

	@Override
	public boolean action()
	{
		String playlist = getParam();
		ignoreLists.add(playlist);
		ITunesLogger.getLogger().log(Level.FINE,"itunes.dbg.found_ign_list_control", //$NON-NLS-1$
			new String[]{playlist});
		return true;
	}

	/**
	 * Get the set of playlists whose contents should be ignored when cross-checking DIR entries
	 * (except while checking that named playlist itself).
	 * @return a set of playlist names.
	 */
	public Set<String> getData()
	{
		return Collections.unmodifiableSet(ignoreLists);
	}
}
