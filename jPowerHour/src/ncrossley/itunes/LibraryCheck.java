package ncrossley.itunes;

import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

import ncrossley.utils.Messages;

/**
 * Checks that the tracks and playlists in a library are self-consistent.
 * <p>
 * This class implements the following internal consistency checks on an iTunes library:
 * <ul>
 * <li>A library has a playlist called Master</li>
 * <li>Each track in the library appears once and only once in the master playlist</li>
 * <li>Playlists (other than smart playlists) are not empty</li>
 * </ul>
 * <p>
 * The process of constructing a library has already checked that:
 * <ul>
 * <li>Tracks in playlists are also in the library</li>
 * <li>Each track has a unique ID</li>
 * <li>Eack playlist has a unique name</li>
 * </ul>
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 *
 */
public class LibraryCheck
{
	private boolean verbose;
	private Library library;

	/**
	 * Create a LibraryCheck to check the specified iTunes library.
	 * @param library The already-populated library to be checked.
	 * @param verbose true if progress messages to be printed after successful checks.
	 */
	public LibraryCheck(Library library, boolean verbose)
	{
		this.library = library;
		this.verbose = verbose;
	}

	/**
	 * Perform the appropriate checks on the Master playlist.
	 * If any errors are found, appropriate messages are printed.
	 * @return true if the Master playlist checks pass, false otherwise.
	 */
	public boolean masterCheck()
	{
		// Get master playlist
		Playlist master = library.findMasterPlaylist();
		if (master == null)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.master_playlist_missing")); //$NON-NLS-1$
			return false;
		}

		// Get (readonly) track list from master playlist
		List<Track> masterTracks = master.getTracks();

		// Run various checks
		boolean passed = true;
		passed = passed & checkInMaster(masterTracks);
		passed = passed & checkDuplicatesInMaster(masterTracks);
		if (passed && verbose)
		{
			Messages.stream().println(Resources.getString("itunes.msgs.master_playlist_ok")); //$NON-NLS-1$
		}
		return passed;
	}


	private boolean checkDuplicatesInMaster(List<Track> masterTracks)
	{
		// Next, check to see if there are any duplicates in the Master playlist
		ITunesLogger.getLogger().finer("itunes.msgs.checking_dups_in_master"); //$NON-NLS-1$
		Set<Integer> trackSet = new HashSet<Integer>(masterTracks.size());
		Map<Integer,Track> duplicates = new HashMap<Integer,Track>();
		for (Track track : masterTracks)
		{
			Integer id = track.getID();
			if (trackSet.contains(id))
			{
				duplicates.put(id,track);
			}
			else
			{
				trackSet.add(id);
			}
		}
		if (duplicates.size() != 0)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.duplicated_tracks_in_master")); //$NON-NLS-1$
			for (Entry<Integer,Track> entry : duplicates.entrySet())
			{
				Integer id = entry.getKey();
				Track track = entry.getValue();
				Messages.stream().println("   "+id+": "+track.getName()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_dups_in_master",false); //$NON-NLS-1$
			return false;
		}
		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_dups_in_master",true); //$NON-NLS-1$
		return true;
	}


	private boolean checkInMaster(List<Track> masterTracks)
	{
		// Check all tracks in the library track list are in the Master playlist,
		// excluding any tracks in podcasts libraries.
		ITunesLogger.getLogger().finer("itunes.msgs.checking_tracks_in_master"); //$NON-NLS-1$
		Set<Track> allTracks = new HashSet<Track>(library.getTracks());
		allTracks.removeAll(masterTracks);
		removePodcasts(allTracks);
		if (allTracks.size() != 0)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.tracks_not_in_master")); //$NON-NLS-1$
			for (Track track : allTracks)
			{
				Messages.stream().println("   "  //$NON-NLS-1$
						+ track.getID()+": "+track.getName()); //$NON-NLS-1$
			}
			ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_tracks_in_master",false); //$NON-NLS-1$
			return false;
		}
		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_tracks_in_master",true); //$NON-NLS-1$
		return true;
	}


	private void removePodcasts(Set<Track> allTracks)
	{
		for (Playlist playlist : library.getPlaylists())
		{
			if (playlist.isPodcastList())
			{
				allTracks.removeAll(playlist.getTracks());
			}
		}
	}


	/**
	 * Check that playlists are not empty.
	 * @return true if all playlists contain at least one track;
	 * false if any track is empty.
	 * Note that non-built-in smart playlists are excluded from the library
	 * as it is read, but we have to exclude the built-in playlists here.
	 */
	public boolean checkPlaylistsNotEmpty()
	{
		ITunesLogger.getLogger().finer("itunes.msgs.checking_lists_nonempty"); //$NON-NLS-1$
		boolean emptyList = false;
		for (Playlist playlist : library.getPlaylists())
		{
			if (!playlist.isPodcastList() && !playlist.isBuiltIn()
					&& playlist.getTracks().size() == 0)
			{
				Messages.stream().println(Resources.getString(
					"itunes.msgs.empty_playlist", //$NON-NLS-1$
					playlist.getName()));
				emptyList = true;
			}
		}
		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_lists_nonempty",!emptyList); //$NON-NLS-1$
		return !emptyList;
	}


	/**
	 * Check that all tracks appear in at least one playlist besides the master playlist.
	 * @param ignoreFiles A set of files to be ignored, in that the given file is allowed
	 * to be in only the master playlist; may be null.
	 * @param ignorePlaylists A set of playlist names to be ignored, in that the given
	 * playlist does not count as containing any tracks; may be null.
	 * @return true if all tracks appear in at least one playlist besides the master playlist,
	 * and false if at least one track does not appear in any non-master playlist.
	 */
	public boolean checkOtherTracks(Set<String> ignoreFiles, Set<String> ignorePlaylists)
	{
		ITunesLogger.getLogger().finer("itunes.msgs.checking_nonmaster_lists"); //$NON-NLS-1$

		// Get master playlist
		Playlist master = library.findMasterPlaylist();

		// Get modifiable copy of all tracks in library
		Set<Track> allTracks = new HashSet<Track>(library.getTracks());

		// Now remove all tracks in all lists other than the master
		for (Playlist playlist : library.getPlaylists())
		{
			if (playlist != master
					&& (ignorePlaylists == null || !ignorePlaylists.contains(playlist.getName())))
			{
				allTracks.removeAll(playlist.getTracks());
			}
		}

		// Remove files we have been asked to ignore
		removeIgnoredFiles(ignoreFiles, allTracks);

		// And see if any tracks are left ...
		if (allTracks.size() != 0)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.tracks_not_in_playlist")); //$NON-NLS-1$
			for (Track track : allTracks)
			{
				Messages.stream().println("   "   //$NON-NLS-1$
					+ track.getID()+": "+track.getName());  //$NON-NLS-1$
			}
			ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_nonmaster_lists",false); //$NON-NLS-1$
			return false;
		}
		else if (verbose)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.other_playlists_complete")); //$NON-NLS-1$
		}

		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_nonmaster_lists",true); //$NON-NLS-1$
		return true;
	}


	private void removeIgnoredFiles(Collection<String> ignoreFiles, Set<Track> allTracks)
	{
		// Remove files we were told to ignore from the given set of tracks
		if (ignoreFiles != null)
		{
			for (Iterator<Track> trackIter = allTracks.iterator(); trackIter.hasNext();)
			{
				Track track = trackIter.next();
				for (String pattern : ignoreFiles)
				{
					if (track.getFile().getPath().matches(pattern))
					{
						trackIter.remove();
					}
				}
			}
		}
	}


	/**
	 * Warn about duplicated tracks in a playlist.
	 * Although this is perfectly valid as far as iTunes is concerned,
	 * it is a feature I do not use, and is probably a mistake if it happens.
	 * @return boolean Returns true if all tracks in all playlists are unique,
	 * and false if some tracks are in any playlist more than once.
	 */
	public boolean checkNoDuplicatedTracks()
	{
		ITunesLogger.getLogger().finer("itunes.msgs.checking_dup_tracks_in_list"); //$NON-NLS-1$
		boolean noDuplicates = true;
		for (Playlist playlist : library.getPlaylists())
		{
			Set<Integer> trackSet = new HashSet<Integer>(playlist.getTracks().size());

			for (Track track : playlist.getTracks())
			{
				if (trackSet.contains(track.getID()))
				{
					Messages.stream().println(MessageFormat.format(
							Resources.getString("itunes.msgs.duplicated_in_playlist"), //$NON-NLS-1$
							track.getID(),track.getName(),playlist.getName()));
					noDuplicates = false;
				}
				else
				{
					trackSet.add(track.getID());
				}
			}
		}
		if (noDuplicates && verbose)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.no_duplicated_tracks")); //$NON-NLS-1$
		}
		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_dup_tracks_in_list",noDuplicates); //$NON-NLS-1$
		return noDuplicates;
	}


	/**
	 * Warn about multiple tracks with the same audio file.
	 * @return Returns true if all tracks have unique audio files,
	 * and false if some tracks in a library share the same audio file.
	 */
	public boolean checkUniqueAudio()
	{
		ITunesLogger.getLogger().finer("itunes.msgs.checking_audio_files_unique"); //$NON-NLS-1$
		Map<String,Set<Track>> audioFiles = buildAudioFileMap();
		boolean allUnique = checkAudioFilesUnique(audioFiles);
		if (allUnique && verbose)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.no_duplicated_audio_files")); //$NON-NLS-1$
		}
		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_audio_files_unique",allUnique); //$NON-NLS-1$
		return allUnique;
	}


	private boolean checkAudioFilesUnique(Map<String,Set<Track>> audioFiles)
	{
		boolean allUnique = true;
		for (Entry<String,Set<Track>> entry : audioFiles.entrySet())
		{
			String fileURL = entry.getKey();
			Set<Track> tracks = entry.getValue();
			assert tracks != null;
			if (tracks.size() != 1)
			{
				Messages.stream().println(MessageFormat.format(
					Resources.getString("itunes.msgs.tracks_ref_same_file"), //$NON-NLS-1$
					fileURL));
				for (Track track : tracks)
				{
					Messages.stream().println("   "  //$NON-NLS-1$
						+ track.getID()+": "+track.getName());  //$NON-NLS-1$
				}
				allUnique = false;
			}
		}
		return allUnique;
	}


	private Map<String,Set<Track>> buildAudioFileMap()
	{
		Collection<Track> allTracks = library.getTracks();
		Map<String,Set<Track>> audioFiles = new HashMap<String,Set<Track>>(allTracks.size());
		for (Track track : allTracks)
		{
			String fileURL = track.getPath();
			if (audioFiles.get(fileURL) == null)
			{
				audioFiles.put(fileURL, new HashSet<Track>());
			}
			audioFiles.get(fileURL).add(track);
		}
		return audioFiles;
	}
}
