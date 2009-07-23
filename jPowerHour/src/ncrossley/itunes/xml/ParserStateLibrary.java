package ncrossley.itunes.xml;

import static ncrossley.itunes.xml.ITunesXmlConstants.*;

/**
 * The ParserStateLibrary type represents the state of the XML parser
 * while reading the outer level of data for the library.
 * Only one instance of this type is constructed, by the parent abstract class.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
class ParserStateLibrary extends ParserStateAdapter
{
	@Override
	public ParserState handleDictStart(ITunesXmlParser parser)
	{
		return ParserState.ITUNES_TRACKS;
	}

	@Override
	public ParserState handleDictEnd(ITunesXmlParser parser)
	{
		return ParserState.ITUNES_START;
	}

	@Override
	public ParserState handleKey(ITunesXmlParser parser, String key)
	{
		if (key.equals(XML_TAG_PLAYLISTS))
		{
			return ParserState.ITUNES_PLAYLISTS;
		}
		else
		{
			return this;
		}
	}
}
