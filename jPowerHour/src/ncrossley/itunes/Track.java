package ncrossley.itunes;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import ncrossley.utils.FileUtils;
import ncrossley.utils.WordUtils;


/**
 * Represents one iTunes track.
 * <p>
 * A Track has a unique ID, and a name which might not be unique.
 * A Track also has a reference to the audio file.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 *
 */
public class Track
{
	private final Integer	id;
	private String			name;
	private String			path;
	private String			artist;
	private String			composer;
	private String			album;
	private boolean			isPodcastTrack;


	/**
	 * Create a new track with the specified ID.
	 * @param id The ID of the new track.  Track IDs must be unique.
	 */
	public Track(Integer id)
	{
		this.id = id;
	}


	/**
	 * Returns the name of the track.
	 * @return String The name of the track.
	 * Track names are not necessarily unique.
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * Sets the name of the track.
	 * @param name The name to set.
	 * Track names are not necessarily unique.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * Returns the path to the audio file, in the form of a URL.
	 * @return String A file://xxx URL that refers to the audio file.
	 * @see #setPath(String)
	 */
	public String getPath()
	{
		return path;
	}


	/**
	 * Sets the path to the audio file.
	 * @param path A file://xxx URL that refers to the audio file.
	 * @see #getPath()
	 */
	public void setPath(String path)
	{
		this.path = path;
	}


	/**
	 * Returns the id of the track.
	 * @return Integer The unique ID of the track.
	 */
	public Integer getID()
	{
		return id;
	}


	/**
	 * Returns the album name.
	 * @return the album name.  Can be null.
	 * @see #setAlbum(String)
	 */
	public String getAlbum()
	{
		return album;
	}


	/**
	 * Set the album name.
	 * @param album the album name to set.
	 * @see #getAlbum()
	 */
	public void setAlbum(String album)
	{
		this.album = album;
	}


	/**
	 * Returns the artist name.
	 * @return the artist name.  Can be null.
	 * @see #setArtist(String)
	 */
	public String getArtist()
	{
		return artist;
	}


	/**
	 * Set the artist name.
	 * @param artist the artist name to set.
	 * @see #getArtist()
	 */
	public void setArtist(String artist)
	{
		this.artist = artist;
	}


	/**
	 * Returns the composer name.
	 * @return the composer name.  Can be null.
	 * @see #setComposer(String)
	 */
	public String getComposer()
	{
		return composer;
	}


	/**
	 * Set the composer name.
	 * @param composer the composer to set.
	 * @see #getComposer()
	 */
	public void setComposer(String composer)
	{
		this.composer = composer;
	}


	/**
	 * Check if this track is a podcast.
	 * @return true if this track is a podcast, false if it is not.
	 * @see #setPodcastTrack(boolean)
	 */
	public boolean isPodcastTrack()
	{
		return isPodcastTrack;
	}


	/**
	 * Set the isPodcastTrack property.
	 * @param isPodcastTrack true if this track is a podcast, false if it is not.
	 * @see #isPodcastTrack()
	 */
	public void setPodcastTrack(boolean isPodcastTrack)
	{
		this.isPodcastTrack = isPodcastTrack;
	}


	/**
	 * Returns a File referring to the audio file.
	 * @return File A reference to the audio file.
	 */
	public File getFile()
	{
		try
		{
			return FileUtils.urlToFile(new URL(getPath()));
		}
		catch (MalformedURLException e)
		{
			throw new ITunesException(Resources.getString(
					"itunes.msgs.bad_url_in_library",getPath(),getName()),e); //$NON-NLS-1$
		}
	}


	/**
	 * Check that Track name and other string properties are good.
	 * @param badWords a set of deprecated starting words.
	 * @param exempt a set of names that are valid even if they do start with bad words
	 * @return true if the track, file, album, artist, and composer names are valid,
	 * and false if any starts with a deprecated word.
	 */
	public boolean checkWords(Set<String> badWords, Set<String> exempt)
	{
		boolean success = true;

		String fileName = getFile().getName();
		success = success & WordUtils.checkPhrase(getName(),badWords,exempt,
					new BadNameMessageGetter("itunes.msgs.white_space_track", //$NON-NLS-1$
						"itunes.msgs.bad_start_track")); //$NON-NLS-1$
		success = success & WordUtils.checkPhrase(fileName,badWords,exempt,
					new BadNameMessageGetter("itunes.msgs.white_space_file", //$NON-NLS-1$
						"itunes.msgs.bad_start_file",getName())); //$NON-NLS-1$
		success = success & WordUtils.checkPhrase(getAlbum(),badWords,exempt,
					new BadNameMessageGetter("itunes.msgs.white_space_album", //$NON-NLS-1$
						"itunes.msgs.bad_start_album",getName())); //$NON-NLS-1$
		success = success & WordUtils.checkPhrase(getArtist(),badWords,exempt,
					new BadNameMessageGetter("itunes.msgs.white_space_artist", //$NON-NLS-1$
						"itunes.msgs.bad_start_artist",getName())); //$NON-NLS-1$
		success = success & WordUtils.checkPhrase(getComposer(),badWords,exempt,
					new BadNameMessageGetter("itunes.msgs.white_space_composer", //$NON-NLS-1$
						"itunes.msgs.bad_start_composer",getName())); //$NON-NLS-1$

		return success;
	}
}
