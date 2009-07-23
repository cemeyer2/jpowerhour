package ncrossley.itunes;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

import ncrossley.utils.WordUtils;

/**
 * An in-memory representation of an iTunes library.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 *
 */
public class Library
{
	private Map<Integer,Track> tracks;
	private Map<String,Playlist> playlists;

	/** Create an empty iTunes library. */
	public Library()
	{
		tracks = new HashMap<Integer,Track>();
		playlists = new HashMap<String,Playlist>();
	}

	/**
	 * Create an iTunes library, reading the contents an XML file.
	 * @param file The XML file to be read.  This file was probably
	 * created using the Export-&gtLibrary menu item from iTunes.
	 */
	public Library(File file)
	{
		tracks = new HashMap<Integer,Track>();
		playlists = new HashMap<String,Playlist>();
		read(file);
	}

	/**
	 * Read the contents of an XML file (an iTunes library export)
	 * and add those tracks and playlists to this library.
	 *
	 * @param file The XML file to be read.  This file was probably
	 * created using the Export-&gtLibrary menu item from iTunes.
	 *
	 * @return int The number of tracks read.
	 */
	public int read(File file)
	{
		LibraryReader reader = new LibraryReader();
		return reader.read(this,file);
	}

	/**
	 * Query to get the number of tracks in the library.
	 * @return int The number of tracks in the library.
	 */
	public int getTrackCount()
	{
		return tracks.size();
	}

	/**
	 * Query to get the number of playlists in the library.
	 * @return int The number of playlists in the library.
	 */
	public int getPlaylistCount()
	{
		return playlists.size();
	}

	/**
	 * Add a track to the library.
	 *
	 * Tracks are not ordered, but have a unique ID.
	 * Each track appears only once in the library
	 * (though a track can appear in multiple playlists,
	 * and multiple times in a playlist.
	 *
	 * @param track The track to be added.
	 * @throws ITunesException If a track with this ID is already in the library.
	 */
	public void add(Track track) throws ITunesException
	{
		Integer id = track.getID();
		if (tracks.get(id) != null)
		{
			throw new ITunesException(MessageFormat.format(
				Resources.getString("itunes.msgs.add_duplicate_track"), //$NON-NLS-1$
				id));
		}
		tracks.put(id,track);
	}

	// This package-level method is used only for testing
	void removeTrack(Integer id)
	{
		tracks.remove(id);
	}

	/**
	 * Add a playlist to the library.
	 *
	 * Playlists are not ordered, but have a unique name.
	 * Each playlist appears only once in a library.
	 *
	 * @param playlist The playlist to be added.
	 * @throws ITunesException If a playlist with this name is already in the library.
	 */
	public void add(Playlist playlist) throws ITunesException
	{
		String name = playlist.getName();
		if (playlists.get(name) != null)
		{
			throw new ITunesException(MessageFormat.format(
				Resources.getString("itunes.msgs.add_duplicate_playlist"), //$NON-NLS-1$
				name));
		}
		playlists.put(name,playlist);
	}

	/**
	 * Find a track, given its unique ID.
	 * @param id The ID of the required track.
	 * @return Track The track found, or null if the specified track does not exist.
	 */
	public Track findTrackbyID(Integer id)
	{
		return tracks.get(id);
	}

	/**
	 * Add a track with a specified ID to a given playlist.
	 * @param id The ID of the track to be added.
	 * @param playlist The playlist to which the track is to be added.
	 * @throws ITunesException If the specified track cannot be found in the library,
	 * and the playlist is not a podcast playlist, or the master playlist.
	 * Missing tracks in podcast playlists correspond to podcasts not yet downloaded,
	 * and are ignored.
	 * Unfortunately, the presence of a missing track in a podcast playlist, or an
	 * out-of-date smart playlist, seems to make the track appear in the folder
	 * 'all items' playlist and also the master playlist.  So we have to ignore
	 * missing tracks in the master playlist as well!
	 */
	public void addTracktoPlaylist(Integer id, Playlist playlist) throws ITunesException
	{
		Track track = findTrackbyID(id);
		if (track != null)
		{
			playlist.add(track);
		}
		else if (!playlist.isPodcastList() && !playlist.isMaster())
		{
			throw new ITunesException(MessageFormat.format(
				Resources.getString("itunes.msgs.cannot_find_track"), //$NON-NLS-1$
				id));
		}
	}

	/**
	 * Find a playlist, given its unique name.
	 * @param name The name of the required playlist.
	 * @return Playlist The playlist found, or null if the specified playlist does not exist.
	 */
	public Playlist findPlaylistbyName(String name)
	{
		return playlists.get(name);
	}

	/**
	 * Each iTunes library should have a Master playlist.  The master
	 * playlist contains each track once and only once.
	 * @return The Master playlist, or null if there is no such playlist.
	 */
	public Playlist findMasterPlaylist()
	{
		for (Playlist playlist : getPlaylists())
		{
			if (playlist.isMaster())
			{
				return playlist;
			}
		}
		return null;
	}

	/**
	 * Returns a readonly Collection of the tracks in this library.
	 * @return An unmodifiable Collection of the tracks in the library.
	 */
	public Collection<Track> getTracks()
	{
		return Collections.unmodifiableCollection(tracks.values());
	}

	/**
	 * Returns a readonly Collection of the playlists in this library.
	 * @return An unmodifiable Collection of the playlists in the library.
	 */
	public Collection<Playlist> getPlaylists()
	{
		return Collections.unmodifiableCollection(playlists.values());
	}

	/**
	 * Check that Playlist names are good.
	 * Podcast playlists are exempt from this check, since their
	 * names are not really user-controlled.
	 * @param badWords a set of deprecated starting words.
	 * @param exempt a set of names that are valid even if they do start with bad words
	 * @return true if all playlist names are valid,
	 * and false if any starts with a deprecated word.
	 */
	public boolean checkPlaylistWords(Set<String> badWords, Set<String> exempt)
	{
		boolean success = true;
		for (Playlist playlist : getPlaylists())
		{
			if (!playlist.isPodcastList())
			{
				success = success & WordUtils.checkPhrase(playlist.getName(),badWords,exempt,
					new BadNameMessageGetter("itunes.msgs.white_space_playlist", //$NON-NLS-1$
							"itunes.msgs.bad_start_playlist")); //$NON-NLS-1$
			}
		}
		return success;
	}

	/**
	 * Get a set of all the files in podcast playlists.
	 * @return a set of files.
	 */
	public Collection<File> podcastFiles()
	{
		Set<File> podcasts = new HashSet<File>();
		for (Playlist playlist : getPlaylists())
		{
			if (playlist.isPodcastList())
			{
				podcasts.addAll(playlist.getFiles());
			}
		}
		return podcasts;
	}
}
