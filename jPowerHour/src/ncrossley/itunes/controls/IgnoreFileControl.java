package ncrossley.itunes.controls;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ncrossley.itunes.ITunesLogger;

/**
 * An IgnoreFileControl handles the IGNORE FILE control file entries.
 * The IGNORE FILE entries are optional; there may be any number of them.
 * For each IGNORE FILE entry, files matching that pattern are removed from all
 * DIR and library root cross-check warnings.
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class IgnoreFileControl extends AbstractControl
{
	private Set<String> ignoreFiles = new HashSet<String>();

	@Override
	public boolean action()
	{
		String pattern = getParam();
		ignoreFiles.add(pattern);
		ITunesLogger.getLogger().log(Level.FINE,"itunes.dbg.found_ign_file_control", //$NON-NLS-1$
				new String[]{pattern});
		return true;
	}

	/**
	 * Get the set of file patterns to be ignored during DIR and library cross-checks.
	 * @return a set of patterns; files matching these patterns will be ignored during
	 * DIR and library cross-checks.
	 */
	public Set<String> getData()
	{
		return Collections.unmodifiableSet(ignoreFiles);
	}
}
