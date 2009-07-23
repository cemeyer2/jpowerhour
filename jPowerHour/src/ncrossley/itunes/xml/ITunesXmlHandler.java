package ncrossley.itunes.xml;

import ncrossley.itunes.Library;
import ncrossley.itunes.Playlist;
import ncrossley.itunes.Track;

import static ncrossley.itunes.xml.ITunesXmlConstants.*;

/**
 * An ITunesXmlHandler is an implementation of the {@link ITunesXmlListener}
 * interface.  The {@link ITunesXmlParser} arranges for the listener to be
 * notified of the higher-level events generated while parsing the XML
 * defining an iTunes library.  This handler uses those callbacks to create
 * the tracks and playlists, and to add them to the {@link Library}.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class ITunesXmlHandler implements ITunesXmlListener
{
	private Library			library;	// the Library to which we are adding
	private Playlist		playlist;	// the current Playlist
	private Track			track;		// the current Track


	/**
	 * Construct a new ITunesXmlHandler, adding tracks and playlists to
	 * the given library.
	 * @param library an existing library to which the tracks and playlists
	 * will be added.
	 */
	public ITunesXmlHandler(Library library)
	{
		this.library = library;
	}


	/**
	 * Create a new track, with just a track ID number.
	 * Other useful information such as name, file, etc.,
	 * will be added later as a result of calls to {@link #handleTrack(String, String)}.
	 * @see ITunesXmlListener#startTrack(java.lang.String)
	 */
	public void startTrack(String trackNumber)
	{
		track = new Track(Integer.valueOf(trackNumber));
	}


	/**
	 * Update the properties of an existing track, once the property
	 * has been found in the XML.
	 * @see ITunesXmlListener#handleTrack(String, String)
	 */
	public void handleTrack(String trackTag, String trackData)
	{
		if (trackTag.equals(XML_TAG_NAME))
		{
			track.setName(trackData);
		}
		else if (trackTag.equals(XML_TAG_LOCATION))
		{
			track.setPath(trackData);
		}
		else if (trackTag.equals(XML_TAG_ARTIST))
		{
			track.setArtist(trackData);
		}
		else if (trackTag.equals(XML_TAG_ALBUM))
		{
			track.setAlbum(trackData);
		}
		else if (trackTag.equals(XML_TAG_COMPOSER))
		{
			track.setComposer(trackData);
		}
	}


	/**
	 * Called after all a track's properties have been set;
	 * we add the track to the library, unless this track has no id, name or path,
	 * indicating it is still in the process of being downloaded.
	 */
	public void completeTrack()
	{
		if (track.getID() != null && track.getName() != null && track.getPath() != null)
		{
			library.add(track);
		}
	}


	/**
	 * Adds the given track to the current playlist.  The current
	 * playlist is set by a call to {@link #startPlaylist()}, and
	 * is kept in a field of this handler.
	 * @see ITunesXmlListener#addTracktoPlaylist(String)
	 */
	public void addTracktoPlaylist(String trackNumber)
	{
		library.addTracktoPlaylist(Integer.valueOf(trackNumber), playlist);
	}


	/**
	 * Create a new empty playlist, and save it as the current playlist.
	 * Just as in the equivalent action with tracks, we do not add the new
	 * empty playlist to the library; we might decide to skip this playlist,
	 * and never add it to the library.  For those we do wish to add, there
	 * will be a call to {@link #completePlaylist()}.
	 * @see ITunesXmlListener#startPlaylist()
	 */
	public void startPlaylist()
	{
		playlist = new Playlist();
	}


	public void handlePlaylist(String playlistTag, String playlistData)
	{
		if (playlistTag.equals(XML_TAG_NAME))
		{
			playlist.setName(playlistData);
		}
	}


	public void setMaster(boolean isMaster)
	{
		playlist.setMaster(isMaster);
	}


	public void setPodcastList(boolean isPodcast)
	{
		playlist.setPodcastList(isPodcast);
	}


	public void setPodcastTrack(boolean isPodcast)
	{
		track.setPodcastTrack(isPodcast);
	}


	public void setBuiltIn(boolean isBuiltIn)
	{
		playlist.setBuiltIn(isBuiltIn);
	}


	public void setSmartList()
	{
		playlist.setSmartList(true);
	}


	/**
	 * Add a completed playlist to the library, excluding smart playlists that
	 * are not built-in playlists.  For example, we add the 'Movies' playlist,
	 * but exclude user-define smart playlists.  We also exclude two of the
	 * built-in playlists - 'Music' and 'Party Shuffle' - because their
	 * contents should never be considered as significant.
	 * @see ITunesXmlListener#completePlaylist()
	 */
	public void completePlaylist()
	{
		if (playlist.isBuiltIn() || !playlist.isSmartList())
		{
			library.add(playlist);
		}
	}
}
