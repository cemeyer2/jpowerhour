package ncrossley.itunes.controls;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncrossley.annotations.SuppressWarnings;
import ncrossley.itunes.Check;
import ncrossley.itunes.ITunesException;
import ncrossley.itunes.ITunesLogger;
import ncrossley.itunes.Resources;
import ncrossley.itunes.controls.DirControl.PlaylistControl;
import ncrossley.utils.Messages;


/**
 * This class parses the control file for the iTunes checker.
 * <p>
 * An iTunes control file contains entries, one per line, of the form:<br><dir><code>
 * XML = <i>path to an iTunes XML library</i><br>
 * MUSICROOT = <i>path to root of MP3 folder hierarchy</i><br>
 * DIR <i>playlist name</i> = <i>path to MP3 folder for this playlist</i><br>
 * CHECKS none
 * CHECKS duplicates,empty,nonmaster<br>
 * IGNORE LIST <i>playlist name</i><br>
 * IGNORE FILE <i>file pattern</i><br>
 * DISTINCT <i>playlist name</i>, <i>playlist name</i> ...<br>
 * NOSTART <i>word</i> ...<br>
 * EXEMPT <i>phrase</i><br>
 * INCLUDE <i>filename</i> [CHARSET <i>charset</i>]<br>
 * </code></dir>
 * Blank lines and comment lines (introduced with // or #) are ignored.
 * Lines may be continued using a single backslash at the end of a line;
 * white space at the start of the next line is ignored.
 * <p>
 * The XML and MUSICROOT entries are mandatory.
 * <p>
 * The DIR entry is optional; there may be any number of DIR entries.
 * For each DIR entry, the contents of the specified playlist are cross-checked against
 * the contents of the specified folder hierarchy.
 * <p>
 * The CHECKS entry is optional; if present it specifies a number of checks to be performed on the playlists.
 * The checks are specified as a comma separated set of words, in upper or lower case.
 * The checks are any of:
 * <dl>
 * <dt>{@code duplicates}</dt><dd>This checks that a playlist contains no duplicate tracks -
 * that is, a track may appear at most once in a playlist.</dd>
 * <dt>{@code empty}</dt><dd>This checks that a playlist is not empty.</dd>
 * <dt>{@code nonmaster}</dt><dd>This checks that each track in the library appears at least once in
 * a playlist other than the master, not counting non-built-in smart playlists.  Built-in playlists
 * other than the 'Music' playlist count as real playlists, unless you add them to an
 * IGNORE LIST control; the built-in 'Music' playlist is always ignored.</dd>
 * </dl>
 * <p>
 * In the absence of the CHECKS entry, all these checks are performed.
 * If the CHECKS entry is given, only the specified checks of this set are performed.
 * If CHECKS none is specified, none of these three checks are performed.
 * <p>
 * The IGNORE entries are optional; there may be any number of IGNORE entries.
 * For each IGNORE LIST entry, the contents of the specified playlist are removed from DIR cross-check warnings,
 * except when checking that named playlist (if the IGNORE LIST entry is also specified as a DIR entry).
 * For each IGNORE FILE entry, files matching that pattern are removed from all DIR and library root
 * cross-check warnings.
 * FIXME Need new control to say that a given list may be empty
 * (to be used for Audiobook Builder and iPhone ringtones)
 * <p>
 * The DISTINCT entry is optional; there may be any number of DISTINCT entries.
 * For each DISTINCT entry, the specified playlists may not contain the same track.
 * <p>
 * The NOSTART entry is optional; there should be zero or one such entry.
 * If present, it contains a set of words (space separated) that should not be used at the start of
 * track names, file names, album names, artist names, and playlist names.
 * Specific names may be exempt from this check by using an EXEMPT control.
 * <p>
 * The EXEMPT entries are optional; there may be any number of such entries.
 * Each EXEMPT entry contains the exact name of a track, file, album, artist, or playlist
 * that is allowed, even if it starts with one of the otherwise deprecated words specified
 * in the NOSTART control.
 * <p>
 * The INCLUDE directive reads control lines from the specified file,
 * optionally in the specified encoding.
 * This may be used to have a common set of DIR, IGNORE, DISTINCT, and NOSTART
 * entries shared between several control files, with different XML and MUSICROOT entries.
 * <p>
 * By default, control files are read in the default encoding for the current locale.
 * This may be changed setting the system property {@code ituneschecker.controls.charset}
 * to the name of any supported character set.
 * A file in some other encoding may be included using an INCLUDE directive with the
 * character set name specified.
 * Note that this implies that control file names may not include spaces!
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class Controls
{
	private File							controlFile;
	private XMLControl						xmlControl;
	private RootControl						rootControl;
	private DirControl						dirControl;
	private ChecksControl					checksControl;
	private IgnoreListControl				ignoreListControl;
	private IgnoreFileControl				ignoreFileControl;
	private DistinctControl					distinctControl;
	private NoStartControl					noStartControl;
	private ExemptControl					exemptControl;
	private Map<Matcher, AbstractControl>	matchers;


	/**
	 * Create a dummy control set for unit testing purposes.
	 */
	Controls()
	{
		this.controlFile = new File("Testing"); //$NON-NLS-1$
	}


	/**
	 * Create an uninitialised control set;
	 * you must call the read method to initialise the controls.
	 * @param controlFile the file from which the controls will be read.
	 */
	public Controls(File controlFile)
	{
		this.controlFile = controlFile;
		xmlControl = new XMLControl(this);
		rootControl = new RootControl(this);
		dirControl = new DirControl(this);
		checksControl = new ChecksControl(this);
		ignoreListControl = new IgnoreListControl();
		ignoreFileControl = new IgnoreFileControl();
		distinctControl = new DistinctControl();
		noStartControl = new NoStartControl();
		exemptControl = new ExemptControl();
		matchers = setupMatchers();
	}


	private Map<Matcher,AbstractControl> setupMatchers()
	{
		Map<Matcher,AbstractControl> myMatchers = new HashMap<Matcher,AbstractControl>();
		addMatcher(myMatchers,"itunes.int.xml_pattern",xmlControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.root_pattern",rootControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.dir_pattern",dirControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.checks_pattern",checksControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.ignlist_pattern",ignoreListControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.ignfile_pattern",ignoreFileControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.distinct_pattern",distinctControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.nostart_pattern",noStartControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.exempt_pattern",exemptControl); //$NON-NLS-1$
		addMatcher(myMatchers,"itunes.int.incl_pattern",new IncludeFileControlMatcher()); //$NON-NLS-1$
		return myMatchers;
	}


	private void addMatcher(Map<Matcher,AbstractControl> map,String pattern,AbstractControl controlMatcher)
	{
		Matcher matcher = Pattern.compile(Resources.getString(pattern)).matcher(""); //$NON-NLS-1$
		controlMatcher.setMatcher(matcher);
		map.put(matcher,controlMatcher);
	}


	/**
	 * Get the current input file (for error messages, etc.).
	 * @return the current input control file.
	 */
	public File getControlFile()
	{
		return controlFile;
	}


	/**
	 * Get the path of an iTunes XML library.
	 * @return the path of an iTunes XML library.
	 */
	public String getITunesXML()
	{
		return xmlControl.getITunesXML();
	}


	/**
	 * Gets the path of an overall iTunes music hierarchy root.
	 * @return the path of an overall iTunes music hierarchy root.
	 */
	public String getMusicRoot()
	{
		return rootControl.getMusicRoot();
	}


	/**
	 * Gets a set of (playlist name,directory) pairs to be checked for consistency.
	 * @return the set of PlaylistControl nodes.
	 */
	public Set<PlaylistControl> getPlaylistControls()
	{
		return dirControl.getData();
	}


	/**
	 * Test if a specified check is to be performed on the playlists.
	 * @param check the Check we are enquiring about
	 * @return true if the given Check is to be performed, false if it is not.
	 */
	public boolean shouldCheck(Check check)
	{
		return checksControl.shouldCheck(check);
	}


	/**
	 * Returns the set of playlists whose contents are to be ignored
	 * when cross-checking other playlists and folders.
	 * @return the set of playlists whose contents are not to be
	 * cross-checked against playlist folders.  Can be null,
	 * indicating that no tracks are to be ignored.
	 */
	public Set<String> getIgnorePlaylists()
	{
		return ignoreListControl.getData();
	}


	/**
	 * Returns the set of file patterns to be ignored
	 * when cross-checking other playlists and folders.
	 * @return a set of patterns of file names that are not to be
	 * cross-checked against playlist folders.  Can be null,
	 * indicating that there are no patterns to be ignored.
	 */
	public Set<String> getIgnoreFiles()
	{
		return ignoreFileControl.getData();
	}


	/**
	 * Returns a set of sets of playlists to be checked for non-overlap.
	 * @return the set of sets of playlists that should not overlap in content.
	 */
	public Set<Set<String>> getDistinctLists()
	{
		return distinctControl.getData();
	}


	/**
	 * Returns a set of words that should not be used at the start various names.
	 * @return a set of words that should not be used at the start of track names,
	 * file names, album names, artist names, and playlist names.
	 * @see #getExemptStartWords()
	 */
	public Set<String> getNoStartWords()
	{
		return noStartControl.getData();
	}


	/**
	 * Returns a set of phrases that are exempt from the normal checks on deprecated start words.
	 * @return a set of phrases that may be used as track names,
	 * file names, album names, artist names, and playlist names,
	 * even if they start with otherwise deprecated start words.
	 * @see #getNoStartWords()
	 */
	public Set<String> getExemptStartWords()
	{
		return exemptControl.getData();
	}


	/**
	 * Read the iTunes checker controls from the control file.
	 * @return true if the control file is located and parsed successfully.
	 */
	public boolean read()
	{
		if (!readOneFile(controlFile,System.getProperty("ituneschecker.controls.charset"))) //$NON-NLS-1$
		{
			// Error message already logged
			return false;
		}
		else if (!rootControl.isSupplied() || !xmlControl.isSupplied())
		{
			// File parsed successfully, but missing vital bits
			Messages.stream().println(Resources.getString(
					"itunes.msgs.missing_control_file_entries", //$NON-NLS-1$
					controlFile.getPath()));
			return false;
		}
		else
		{
			return true;
		}
	}


	boolean readNestedFile(String newFile, String charset)
	{
		// For nested files, treat as relative to outer file
		File newControlFile = new File(newFile);
		if (controlFile != null && !newControlFile.isAbsolute())
		{
			newControlFile = new File(controlFile.getParent(),newFile);
		}
		File savedControlFile = controlFile;
		try
		{
			return readOneFile(newControlFile,charset);
		}
		finally
		{
			controlFile = savedControlFile;
		}
	}


	private boolean readOneFile(File file, String charset)
	{
		boolean success = true;
		BufferedReader reader;
		controlFile = file;
		String csName = charset != null ? charset :	Charset.defaultCharset().name();

		try
		{
			reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(controlFile),csName));
		}
		catch (FileNotFoundException fnf)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.control_file_does_not_exist", //$NON-NLS-1$
					controlFile.getPath()));
			return false;
		}
		catch (UnsupportedEncodingException ue)
		{
			Messages.stream().println(Resources.getString(
				"itunes.msgs.bad_charset_name", //$NON-NLS-1$
				csName,controlFile.getPath()));
			return false;
		}

		ITunesLogger.getLogger().log(Level.CONFIG,"itunes.dbg.reading_control_file", //$NON-NLS-1$
			new Object[]{file,csName});
		try
		{
			success = parseLines(reader);
		}
		catch (IOException ioe)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.cannot_read_control_file", //$NON-NLS-1$
					controlFile.getPath()));
			return false;
		}
		catch (ITunesException e2)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.cannot_parse_control_file", //$NON-NLS-1$
					controlFile.getPath(),e2.getMessage()));
			return false;
		}
		return success;
	}


	private boolean parseLines(BufferedReader reader) throws IOException, ITunesException
	{
		boolean success = true;
		String line;
		while ((line = getLine(reader)) != null)
		{
			success = success & parseOneLine(line);
		}
		return success;
	}


	private boolean parseOneLine(String line)
	{
		for (Matcher matcher : matchers.keySet())
		{
			if (matcher.reset(line).matches())
			{
				AbstractControl controlMatcher = matchers.get(matcher);
				return controlMatcher.action();
			}
		}
		// Did not match any pattern
		throw new ITunesException(line);
	}


	@SuppressWarnings(
			value="SBSC",
			justification="The strings concatenated are small, and not too many of them")
	private String getLine(BufferedReader reader) throws IOException, ITunesException
	{
		String line;
		while ((line = reader.readLine()) != null)
		{
			line = line.trim();
			if (line.length() > 0 && line.charAt(0) == 0x00FEFF)
			{
				// Skip Unicode BOMs added by Notepad and some other tools
				line = line.substring(1);
			}
			if (line.length() == 0
				|| line.startsWith("#")      //$NON-NLS-1$
				|| line.startsWith("//"))    //$NON-NLS-1$
			{
				continue;
			}

			while (line.endsWith("\\")) //$NON-NLS-1$
			{
				line = line.substring(0,line.length()-1);
				String continuation = reader.readLine();
				if (continuation == null)
				{
					throw new ITunesException(Resources.getString(
							"itunes.msgs.eof_after_continuation")); //$NON-NLS-1$
				}
				line = line + continuation.trim();
			}
			return line;
		}
		return null;
	}


	private class IncludeFileControlMatcher extends AbstractControl
	{
		@Override
		public boolean action()
		{
			return readNestedFile(getParam(1),getParam(2));
		}
	}
}
