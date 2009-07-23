package ncrossley.itunes.controls;

import java.util.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ncrossley.itunes.ITunesLogger;
import ncrossley.itunes.Resources;

/**
 * A NoStartControl handles the NOSTART control file entry.
 * The NOSTART entry is optional.  If present, it contains a set of words (space separated)
 * that should not be used at the start of track names, file names, album names, artist names,
 * and playlist names.
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class NoStartControl extends AbstractControl
{
	private Set<String> noStartWords = new HashSet<String>();

	@Override
	public boolean action()
	{
		String[] noStarts = getParam().split(
				Resources.getString("itunes.int.space_comma_sep")); //$NON-NLS-1$
		noStartWords.addAll(Arrays.asList(noStarts));
		ITunesLogger.getLogger().log(Level.FINE,"itunes.dbg.found_nostart_control",noStarts); //$NON-NLS-1$
		return true;
	}

	/**
	 * Get the set of deprecated start words.
	 * @return the set of words that should not be used at the start of various fields,
	 * such as track names, artist names, composer names, etc.
	 */
	public Set<String> getData()
	{
		return Collections.unmodifiableSet(noStartWords);
	}
}
