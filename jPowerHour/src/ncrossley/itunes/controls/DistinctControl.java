package ncrossley.itunes.controls;

import java.util.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ncrossley.itunes.ITunesLogger;
import ncrossley.itunes.Resources;

/**
 * A DistinctControl handles the DISTINCT control file entry.
 * The DISTINCT entry is optional; there may be any number of DISTINCT entries.
 * For each DISTINCT entry, the specified playlists may not contain the same track.
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class DistinctControl extends AbstractControl
{
	private Set<Set<String>> distinctLists = new HashSet<Set<String>>();

	@Override
	public boolean action()
	{
		String[] playlists = getParam().split(Resources.getString("itunes.int.comma_sep")); //$NON-NLS-1$
		distinctLists.add(new HashSet<String>(Arrays.asList(playlists)));
		ITunesLogger.getLogger().log(Level.FINE,"itunes.dbg.found_distinct_control",playlists); //$NON-NLS-1$
		return true;
	}

	/**
	 * Get the set of distinct lists of playlist names;
	 * each set of playlists will be checked to make sure they have no tracks in common.
	 * @return A set of sets, each set containing the names of playlists that should be distinct.
	 */
	public Set<Set<String>> getData()
	{
		return Collections.unmodifiableSet(distinctLists);
	}
}
