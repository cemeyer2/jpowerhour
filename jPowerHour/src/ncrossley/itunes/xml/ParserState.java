package ncrossley.itunes.xml;

/**
 * A ParserState represents the state of the XML parser;
 * this is the State object pattern.  Each state is represented by
 * a singleton object of a subclass of this abstract class;
 * the methods on each state subclass provide the possible state transitions.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public abstract class ParserState
{
	/** The parser starting state. */
	public static final ParserState ITUNES_START = new ParserStateStart();

	/** The parser state while reading the library information. */
	public static final ParserState ITUNES_LIBRARY = new ParserStateLibrary();

	/** The parser state while reading the library track records. */
	public static final ParserState ITUNES_TRACKS = new ParserStateTracks();

	/** The parser state while reading the details for a single track. */
	public static final ParserState ITUNES_INTRACK = new ParserStateInTrack();

	/** The parser state between reading each library playlist record. */
	public static final ParserState ITUNES_PLAYLISTS = new ParserStatePlaylists();

	/** The parser state while reading the information about a playlist. */
	public static final ParserState ITUNES_INPLAYLIST = new ParserStateInPlaylist();

	/** The parser state while reading the tracks to be added to a playlist. */
	public static final ParserState ITUNES_PLAYLIST_TRACKS = new ParserStatePlaylistTracks();

	/** The parser state while skipping a folder. */
	public static final ParserState ITUNES_INSKIP = new ParserStateSkip();

	/** The parser state while skipping the tracks in a folder. */
	public static final ParserState ITUNES_INSKIP_TRACKS = new ParserStateSkipTracks();


	/**
	 * Construct a new ParserState; this is invoked by the concrete subclasses,
	 * once per class, to represent each possible state.
	 */
	protected ParserState()
	{
		// No action needed
	}


	/**
	 * Abstract state transition method called when a &lt;dict&gt; starting tag is found.
	 * @param parser the object driving the parsing;
	 * action methods on this object may be called.
	 * @return the next state, if a state transition is required,
	 * or <code>this</code>, if no transition is required.
	 */
	public abstract ParserState handleDictStart(ITunesXmlParser parser);

	/**
	 * Abstract state transition method called when a &lt;/dict&gt; closing tag is found.
	 * @param parser the object driving the parsing;
	 * action methods on this object may be called.
	 * @return the next state, if a state transition is required,
	 * or <code>this</code>, if no transition is required.
	 */
	public abstract ParserState handleDictEnd(ITunesXmlParser parser);

	/**
	 * Abstract state transition method called when a &lt;/key&gt; closing tag is found.
	 * @param parser the object driving the parsing;
	 * action methods on this object may be called.
	 * @param key the key name from the XML parser.
	 * @return the next state, if a state transition is required,
	 * or <code>this</code>, if no transition is required.
	 */
	public abstract ParserState handleKey(ITunesXmlParser parser, String key);

	/**
	 * Abstract state transition method called when a boolean closing tag is found
	 * (one of the forms &lt;true/&gt;, &lt;false/&gt;, &lt;/true&gt;, or &lt;/false&gt;).
	 * @param parser the object driving the parsing;
	 * action methods on this object may be called.
	 * @param flagTrue true if the closing tag is true, and false if the tag is false.
	 * @return the next state, if a state transition is required,
	 * or <code>this</code>, if no transition is required.
	 */
	public abstract ParserState handleFlag(ITunesXmlParser parser, boolean flagTrue);

	/**
	 * Abstract state transition method called when any other closing tag is found.
	 * @param parser the object driving the parsing;
	 * action methods on this object may be called.
	 * @param qName a tag name from the XML parser.
	 * @param xmlChars the text content from the closed tag.
	 * @return the next state, if a state transition is required,
	 * or <code>this</code>, if no transition is required.
	 */
	public abstract ParserState handleObject(ITunesXmlParser parser, String qName, String xmlChars);
}
