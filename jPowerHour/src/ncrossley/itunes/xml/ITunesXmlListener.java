package ncrossley.itunes.xml;


/**
 * The {@link ITunesXmlParser} arranges for an instance of this interface
 * to be notified of the higher-level events generated while parsing the XML
 * defining an iTunes library.  A handler implementing this interface would
 * use the callbacks to create the tracks and playlists, and to add them to
 * the {@link ncrossley.itunes.Library}.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public interface ITunesXmlListener
{
	/**
	 * Called from the XML parser when a new track is started.
	 * This method is called exactly once per track; it can be called
	 * before any playlists have been defined, and there is no implication
	 * that a track 'belongs to' any playlist that might or might not be
	 * in the process of being defined.
	 * <p>This call will be followed by a sequence of calls to
	 * {@link #handleTrack(String,String)}, and optionally a call to
	 * {@link #setPodcastTrack(boolean)}.
	 * Finally, there may be a call to {@link #completeTrack()}
	 * at most once per track.  Note we want to skip some tracks,
	 * based on information found later in the XML, so it is possible
	 * that a call to completeTrack() might not be made; in this
	 * case the incomplete track created by startTrack() should
	 * just be discarded.
	 * @param trackNumber a string holding a unique track ID number.
	 */
	void startTrack(String trackNumber);


	/**
	 * Called from the XML parser when information about the
	 * current track is found.  This method may be called multiple times after
	 * {@link #startTrack(String)}, each time with a different trackTag parameter.
	 * There is no corresponding call at the end of gathering all track information.
	 * @param trackTag the name of the just-closed track tag.  This tag
	 * might be one of
	 * {@code ITunesXmlConstants#XML_TAG_NAME},
	 * {@code ITunesXmlConstants#XML_TAG_LOCATION},
	 * {@code ITunesXmlConstants#XML_TAG_ARTIST},
	 * {@code ITunesXmlConstants#XML_TAG_ALBUM},
	 * or {@code ITunesXmlConstants#XML_TAG_COMPOSER}; such tags should be processed
	 * appropriately, setting properties of the current track.
	 * Other tags might also be reported; these may be ignored.
	 * @param trackData the content of the just-closed track tag.
	 */
	void handleTrack(String trackTag, String trackData);


	/**
	 * Called from the XML parser once per track after all information about a track
	 * has been gathered.  If this method is called, we probably want to keep information about this
	 * track.  There can be tracks for which this method is not called, or tracks which
	 * this method decides to discard.
	 */
	void completeTrack();


	/**
	 * Called from the XML parser when a new playlist should be started.
	 * At the time of this call, nothing is known about the playlist.
	 * This call will be followed by a sequence of calls to
	 * {@link #handlePlaylist(String,String)}, optionally a call
	 * to {@link #setPodcastList(boolean)} or {@link #setBuiltIn(boolean)},
	 * then a sequence of calls to {@link #addTracktoPlaylist(String)};
	 * finally, there may be a call to {@link #completePlaylist()}
	 * at most once per playlist.  Note we want to skip some playlists,
	 * based on information found later in the XML, so it is possible
	 * that a call to completePlaylist() might not be made; in this
	 * case the incomplete playlist created by startPlaylist() should
	 * just be discarded.
	 */
	void startPlaylist();


	/**
	 * Called from the XML parser when information about the
	 * current playlist is found.  This method may be called multiple times after
	 * {@link #startPlaylist()}, each time with a different playlistTag parameter.
	 * @param playlistTag the name of the just-closed playlist tag.  This tag
	 * might be
	 * {@code ITunesXmlConstants#XML_TAG_NAME}; such a tag should be processed
	 * appropriately, setting properties of the current playlist.
	 * Other tags might also be reported; these may be ignored.
	 * @param playlistData the content of the just-closed playlist tag.
	 */
	void handlePlaylist(String playlistTag, String playlistData);


	/**
	 * Called from the XML parser to set if the current playlist is the master playlist;
	 * this will be done after {@link #startPlaylist()} has been called.
	 * @param isMaster true if the current playlist is a colleciton of podcasts.
	 */
	void setMaster(boolean isMaster);


	/**
	 * Called from the XML parser to set if the current playlist is a podcast playlist;
	 * this will be done after {@link #startPlaylist()} has been called.
	 * @param isPodcast true if the current playlist is a collection of podcasts.
	 */
	void setPodcastList(boolean isPodcast);


	/**
	 * Called from the XML parser to set if the current track is a podcast;
	 * this will be done after {@link #startTrack(String)} has been called.
	 * @param isPodcast true if the current playlist is a collection of podcasts.
	 */
	void setPodcastTrack(boolean isPodcast);


	/**
	 * Called from the XML parser to set if the current playlist is a built-in playlist;
	 * this will be done after {@link #startPlaylist()} has been called.
	 * @param isBuiltIn true if the current playlist is a built-in one,
	 * such as Videos or Party Shuffle.
	 */
	void setBuiltIn(boolean isBuiltIn);


	/**
	 * Called from the XML parser to set the current playlist as a smart playlist;
	 * this will be done after {@link #startPlaylist()} has been called.
	 */
	void setSmartList();


	/**
	 * Called from the XML parser each time a track is found as a member
	 * of the current playlist, as set by a preceding call to {@link #startPlaylist()}.
	 * @param trackNumber a string holding the track ID number.
	 */
	void addTracktoPlaylist(String trackNumber);


	/**
	 * Called from the XML parser once per playlist after all information about a playlist
	 * has been gathered.  If this method is called, we probably want to keep information about this
	 * playlist.  There can be playlists for which this method is not called, or playlists which
	 * this method decides to discard.
	 */
	void completePlaylist();
}
