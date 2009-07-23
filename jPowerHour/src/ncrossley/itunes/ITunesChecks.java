package ncrossley.itunes;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

import ncrossley.itunes.controls.Controls;
import ncrossley.itunes.controls.DirControl.PlaylistControl;
import ncrossley.utils.Messages;

/**
 * An instance of ITunesChecks is responsible for running the various checks
 * and other features of the iTunes library analyser.
 * These checks include:
 * <ul>
 * <li>Read specified iTunes library XML export file.</li>
 * <li>Comment about iTunes playlist organisation,
 * noting any tracks that are in no playlist,
 * and optionally any tracks that are in multiple playlists.</li>
 * <li>Scan specified directory to build up table of files.</li>
 * <li>Cross-correlate the iTunes library with the table of files.</li>
 * </ul>

 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class ITunesChecks
{
	private boolean verbose;
	private Controls controls;
	private Library library;

	/**
	 * Configure an ITunesChecker with the desired parameters.
	 * @param verbose true iff the checks should display informational and progress messages.
	 * @param controls the control information, already parsed from files or GUI selections.
	 */
	public ITunesChecks(Controls controls, boolean verbose)
	{
		this.controls = controls;
		this.verbose = verbose;
	}

	private boolean makeLibrary()
	{
		try
		{
			File xmlFile = new File(controls.getITunesXML());
			library = new Library(xmlFile);
			return true;
		}
		catch (ITunesException ite)
		{
			Messages.stream().println(
					  Resources.getString("itunes.msgs.unexpected_exception_lib", //$NON-NLS-1$
							  controls.getITunesXML()));
			ite.printStackTrace(Messages.stream());
			return false;
		}
	}


	private boolean scanDirectory()
	{
		boolean passed = true;
		Set<String> ignoreFiles = controls.getIgnoreFiles();
		ConsistentLibrary scanner = new ConsistentLibrary(controls.getMusicRoot());
		passed &= scanner.checkAllFilesInLibrary(library,ignoreFiles);
		passed &= scanner.checkAllLibraryInFiles(library,library.podcastFiles());
		if (passed && verbose)
		{
			Messages.stream().println(Resources.getString("itunes.msgs.master_matches_root", //$NON-NLS-1$
			        controls.getMusicRoot()));
		}
		return passed;
	}

	private boolean checkConsistent()
	{
		ITunesLogger.getLogger().finer("itunes.msgs.checking_consistency"); //$NON-NLS-1$
		boolean passed = true;
		Set<String> ignoreFiles = controls.getIgnoreFiles();
		Set<PlaylistControl> playlistControls = controls.getPlaylistControls();
		for (PlaylistControl playlistControl : playlistControls)
		{
			ConsistentPlaylist scanner = new ConsistentPlaylist(playlistControl.getMusicDir());
			Playlist playlist = library.findPlaylistbyName(playlistControl.getPlaylistName());
			if (playlist == null)
			{
				Messages.stream().println(Resources.getString(
					"itunes.msgs.playlist_not_checked", //$NON-NLS-1$
					playlistControl.getPlaylistName()));
			}
			else
			{
				boolean thisPassed = true;
				Set<File> ignoreTunes = expand(controls.getIgnorePlaylists(),playlist);
				thisPassed &= scanner.checkAllFilesInPlaylist(playlist,ignoreFiles);
				thisPassed &= scanner.checkAllMusicInFiles(playlist,ignoreTunes);
				if (thisPassed && verbose)
				{
					Messages.stream().println(Resources.getString(
						"itunes.msgs.playlist_matches_dir", //$NON-NLS-1$
						playlistControl.getPlaylistName(),
						playlistControl.getMusicDir()));
				}
				passed &= thisPassed;
			}
		}
		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_consistency",passed); //$NON-NLS-1$
		return passed;
	}


	private Set<File> expand(Set<String> playlists,Playlist self)
	{
		Set<File> ignoreTunes = new HashSet<File>();
		for (String playlistName : playlists)
		{
			Playlist playlist = library.findPlaylistbyName(playlistName);
			if (playlist == null)
			{
			    Messages.stream().println(Resources.getString(
			    		"itunes.msgs.playlist_not_checked", //$NON-NLS-1$
						playlistName));
			}
			else if (playlist != self)
			{
			    ignoreTunes.addAll(playlist.getFiles());
				ITunesLogger.getLogger().log(Level.FINEST,
						"itunes.dbg.expanded_ignore",playlist.getName()); //$NON-NLS-1$
			}
		}
		return ignoreTunes;
	}


	/**
	 * Warn about tracks in two or more playlists that have been declared as distinct.
	 * @return Returns true if no distinct playlists tracks contain the same track,
	 * and false if some distinct playlists tracks contain the same track.
	 */
	private boolean checkNoOverlap()
	{
		ITunesLogger.getLogger().finer("itunes.msgs.checking_overlap"); //$NON-NLS-1$
		boolean noDuplicates = true;
		for (Set<String> distinct : controls.getDistinctLists())
		{
			noDuplicates &= noOverlap(distinct);
		}
		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_overlap",noDuplicates); //$NON-NLS-1$
		return noDuplicates;
	}


	private boolean noOverlap(Set<String> playlistNames)
	{
		boolean success = true;
		Set<Integer> seen = new HashSet<Integer>();
		StringBuilder names = new StringBuilder();
		for (String playlistName : playlistNames)
		{
			Playlist playlist = library.findPlaylistbyName(playlistName);
			if (playlist == null)
			{
				Messages.stream().println(
				    Resources.getString("itunes.msgs.playlist_not_checked",playlistName)); //$NON-NLS-1$
			}
			else
			{
				names.append(playlistName);
				names.append(',');
				success = success & checkListOverlap(seen, playlist);
			}
		}
		if (verbose && success && names.length()>0)
		{
		    names.setLength(names.length()-1);
		    Messages.stream().println(Resources.getString(
		    		"itunes.msgs.distinct",names.toString())); //$NON-NLS-1$
		}
		return success;
	}


	private boolean checkListOverlap(Set<Integer> seen, Playlist playlist)
	{
		boolean noOverlap = true;
		for (Track track : playlist.getTracks())
		{
			if (seen.contains(track.getID()))
			{
				Messages.stream().println(Resources.getString(
					"itunes.msgs.not_distinct", //$NON-NLS-1$
					track.getName(),playlist.getName()));
				noOverlap = false;
			}
			seen.add(track.getID());
		}
		return noOverlap;
	}


	// Warn about playlists, tracks, files, artists, albums, and composers
	// starting with words such as 'the'
	private boolean goodStartWords()
	{
		ITunesLogger.getLogger().finer("itunes.msgs.checking_start_words"); //$NON-NLS-1$
	    boolean success = true;
	    Set<String> exempt = controls.getExemptStartWords();
	    if (exempt.size() == 0)
	    {
			ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.no_start_words"); //$NON-NLS-1$
	    }

		// Check names of playlists
		success = success & library.checkPlaylistWords(controls.getNoStartWords(),exempt);

		// Check each track in master playlist
	    Playlist master = library.findMasterPlaylist();
		success = success & master.checkTrackWords(controls.getNoStartWords(),exempt);

		if (success && verbose)
		{
		    Messages.stream().println(Resources.getString(
		    		"itunes.msgs.good_start_words")); //$NON-NLS-1$
		}

		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_start_words",success); //$NON-NLS-1$
		return success;
	}


	/**
	 * Run all possible checks.
	 * @return true iff all tests passed.
	 */
	public boolean runAllChecks()
	{
		return makeLibrary() && checkData();
	}


	private boolean checkData()
	{
		boolean passed = true;
		LibraryCheck libCheck = new LibraryCheck(library,verbose);
		Set<String> ignoreFiles = controls.getIgnoreFiles();
		Set<String> ignorePlaylists = controls.getIgnorePlaylists();
		passed &= libCheck.masterCheck();
		passed &= libCheck.checkUniqueAudio();
		if (controls.shouldCheck(Check.empty))
		{
			passed &= libCheck.checkPlaylistsNotEmpty();
		}
		if (controls.shouldCheck(Check.nonmaster))
		{
			passed &= libCheck.checkOtherTracks(ignoreFiles,ignorePlaylists);
		}
		if (controls.shouldCheck(Check.duplicates))
		{
			passed &= libCheck.checkNoDuplicatedTracks();
		}
		passed &= scanDirectory();
		passed &= checkConsistent();
		passed &= checkNoOverlap();
		passed &= goodStartWords();
		return passed;
	}
}
