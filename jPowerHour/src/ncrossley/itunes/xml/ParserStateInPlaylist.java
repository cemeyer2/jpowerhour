package ncrossley.itunes.xml;

import static ncrossley.itunes.xml.ITunesXmlConstants.*;

/**
 * The ParserStateInPlaylist type represents the state of the XML parser while
 * reading the information about a playlist from the iTunes XML data.
 * Only one instance of this type is constructed, by the parent abstract class.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
class ParserStateInPlaylist extends ParserStateAdapter
{
	@Override
	public ParserState handleDictStart(ITunesXmlParser parser)
	{
		return ITUNES_PLAYLIST_TRACKS;
	}

	@Override
	public ParserState handleDictEnd(ITunesXmlParser parser)
	{
		parser.getListener().completePlaylist();
		return ITUNES_PLAYLISTS;
	}

	@Override
	public ParserState handleKey(ITunesXmlParser parser, String key)
	{
		if (key.equals(XML_TAG_SMART))
		{
			parser.getListener().setSmartList();
		}
		return this;
	}

	@Override
	public ParserState handleFlag(ITunesXmlParser parser, boolean flagTrue)
	{
		final String key = parser.getLastKey();
		if (flagTrue && (
			   key.equals(XML_TAG_FOLDER)
			|| key.equals(XML_TAG_MUSIC)
			|| key.equals(XML_TAG_SHUFFLE)))
		{
			// Skip folders - we process only the playlists inside folders
			// Skip the built-in Music and Party Shuffle playlists
			return ParserState.ITUNES_INSKIP;
		}
		else if (key.equals(XML_TAG_MASTER))
		{
			parser.getListener().setMaster(flagTrue);
			return this;
		}
		else if (key.equals(XML_TAG_PODCASTS))
		{
			parser.getListener().setPodcastList(flagTrue);
			return this;
		}
		else if (key.equals(XML_TAG_VIDEOS)
				|| key.equals(XML_TAG_AUDIOBOOKS)
				|| key.equals(XML_TAG_MOVIES)
				|| key.equals(XML_TAG_TVSHOWS))
		{
			parser.getListener().setBuiltIn(flagTrue);
			return this;
		}
		else
		{
			return this;
		}
	}

	@Override
	public ParserState handleObject(ITunesXmlParser parser, String qName, String xmlChars)
	{
		if (qName.equals(XML_TAG_STRING))
		{
			parser.getListener().handlePlaylist(parser.getLastKey(),xmlChars);
		}
		return this;
	}
}
