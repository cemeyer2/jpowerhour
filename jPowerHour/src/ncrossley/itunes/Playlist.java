package ncrossley.itunes;

import java.io.File;
import java.util.*;

/**
 * Represents one iTunes playlist.
 * <p>
 * Each Playlist in a Library has a unique name.
 * A Playlist holds an ordered list of Tracks;
 * each Track may appear multiple times in a Playlist.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 *
 */
public class Playlist
{
	private String name;
	private boolean isMaster;
	private boolean isPodcastList;
	private boolean isBuiltIn;
	private boolean isSmartList;
	private List<Track> tracks;


	/** Create a new Playlist. */
	public Playlist()
	{
		isMaster = false;
		isPodcastList = false;
		isBuiltIn = false;
		tracks = new ArrayList<Track>();
	}

	/**
	 * Get the name of this playlist.
	 * @return String The unique name of this playlist.
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the playlist.
	 * @param name The name to set.  Each playlist in a library must
	 * have a unique name.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Check if this playlist is the master playlist.
	 * @return true if this is the master playlist,
	 * false if it is not.
	 * @see #setMaster(boolean)
	 */
	public boolean isMaster()
	{
		return isMaster;
	}

	/**
	 * Set the master property.
	 * @param isMaster true if this playlist is the master playlist,
	 * false if it is not.
	 * @see #isMaster()
	 */
	public void setMaster(boolean isMaster)
	{
		this.isMaster = isMaster;
	}

	/**
	 * Check if this playlist is a collection of podcasts.
	 * @return true if this playlist is a collection of podcasts,
	 * false if it is not.
	 * @see #setPodcastList(boolean)
	 */
	public boolean isPodcastList()
	{
		return isPodcastList;
	}

	/**
	 * Set the isPodcastList property.
	 * @param isPodcastList true if this playlist is a collection of podcasts,
	 * false if it is not.
	 * @see #isPodcastList()
	 */
	public void setPodcastList(boolean isPodcastList)
	{
		this.isPodcastList = isPodcastList;
	}

	/**
	 * Check if this playlist is one of the built-in playlists,
	 * such as the Videos and Audiobooks playlists.
	 * @return true if this playlist is one of the built-in playlists,
	 * false if it is not.
	 * @see #setBuiltIn(boolean)
	 */
	public boolean isBuiltIn()
	{
		return isBuiltIn;
	}

	/**
	 * Set the isBuiltIn property.
	 * @param isBuiltIn true if this playlist is one of the built-in playlists,
	 * such as the Videos playlist,
	 * false if it is not.
	 * @see #isBuiltIn()
	 */
	public void setBuiltIn(boolean isBuiltIn)
	{
		this.isBuiltIn = isBuiltIn;
	}

	/**
	 * Check if this playlist is a smart playlist.
	 * @return true if this playlist is a smart playlist,
	 * false if it is not.
	 * @see #setSmartList(boolean)
	 */
	public boolean isSmartList()
	{
		return isSmartList;
	}

	/**
	 * Set the isSmartList property.
	 * @param isSmartList true if this playlist is a smart playlist,
	 * false if it is not.
	 * @see #isSmartList()
	 */
	public void setSmartList(boolean isSmartList)
	{
		this.isSmartList = isSmartList;
	}

	/**
	 * Get the contents of this playlist.
	 * @return List The tracks in the playlist, returned in the
	 * order in which they were added.
	 */
	public List<Track> getTracks()
	{
		return Collections.unmodifiableList(tracks);
	}

	/**
	 * Add a track to this playlist.
	 * @param track The track to be added to this playlist.  The same
	 * track may be added to a playlist multiple times.
	 * @return Playlist The updated Playlist itself, for method chaining.
	 */
	public Playlist add(Track track)
	{
		assert track != null;
		tracks.add(track);
		return this;
	}

	// This package-level method is used only for testing
	// It removes the first occurrence of the given track
	void removeTrack(Integer id)
	{
		for (Iterator<Track> iter = tracks.iterator(); iter.hasNext();)
		{
			Track track = iter.next();
			if (track.getID().equals(id))
			{
				iter.remove();
				return;
			}
		}
	}

	/**
	 * Return a collection of all the music files in this playlist.
	 * @return returns the collection of Files.
	 */
	public Collection<File> getFiles()
	{
		Set<File> allFiles = new HashSet<File>();
		for (Track track : tracks)
		{
			allFiles.add(track.getFile());
		}
		return allFiles;
	}

	/**
	 * Check that Track names and other properties are good.
	 * Podcast playlists and tracks are exempt from this check,
	 * since the names of those are not really user-controlled.
	 * @param badWords a set of deprecated starting words.
	 * @param exempt a set of names that are valid even if they do start with bad words
	 * @return true if all track names and other properties are valid,
	 * and false if any starts with a deprecated word.
	 */
	public boolean checkTrackWords(Set<String> badWords, Set<String> exempt)
	{
		boolean success = true;
		if (!isPodcastList())
		{
			for (Track track : getTracks())
			{
				if (!track.isPodcastTrack())
				{
					success = success & track.checkWords(badWords,exempt);
				}
			}
		}
		return success;
	}
}
