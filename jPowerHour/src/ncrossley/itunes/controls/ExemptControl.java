package ncrossley.itunes.controls;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ncrossley.itunes.ITunesLogger;

/**
 * An ExemptControl handles the EXEMPT control file entries.
 * The EXEMPT entries are optional; there may be any number of them.
 * Each EXEMPT entry contains the exact name of a track, file, album, artist, or playlist
 * that is allowed, even if it starts with one of the otherwise deprecated words specified
 * in the NOSTART control.
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class ExemptControl extends AbstractControl
{
	private Set<String> exemptPhrases = new HashSet<String>();

	@Override
	public boolean action()
	{
		String phrase = getParam();
		exemptPhrases.add(phrase);
		ITunesLogger.getLogger().log(Level.FINE,"itunes.dbg.found_exempt_control", //$NON-NLS-1$
				new String[]{phrase});
		return true;
	}

	/**
	 * Get the set of phrases to be ignored during deprecated first word checking.
	 * @return a set of phrases; names with exactly these strings will be ignored during
	 * deprecated first word checking.
	 */
	public Set<String> getData()
	{
		return Collections.unmodifiableSet(exemptPhrases);
	}
}
