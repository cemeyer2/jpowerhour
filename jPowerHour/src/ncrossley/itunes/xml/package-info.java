/**
 * This sub-package of the iTunes consistency checker deals with the iTunes
 * library and its parsing.
 * <p>
 * The control file is XML. The parser is in
 * {@link ncrossley.itunes.xml.ITunesXmlParser ITunesXmlParser}; it uses
 * SAX. Parsing is performed using a finite state machine, driven from SAX
 * callbacks. For each state, the parser contructs an instance of an appropriate
 * subclass of {@link ncrossley.itunes.xml.ParserState ParserState} to handle
 * the actions and state transitions.  The state machine then generates higher
 * level events, such as 'start of a playlist', 'add this track to this playlist',
 * etc., by making calls to the
 * {@link ncrossley.itunes.xml.ITunesXmlListener ITunesXmlListener}
 * associated with the parser.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 * @since 1.0
 */
package ncrossley.itunes.xml;
