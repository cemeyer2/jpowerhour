package ncrossley.itunes.xml;

import static ncrossley.itunes.xml.ITunesXmlConstants.*;

/**
 * The ParserStatePlaylistTracks type represents the state of the XML parser while
 * reading the tracks in one playlist from the iTunes XML data.
 * Only one instance of this type is constructed, by the parent abstract class.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
class ParserStatePlaylistTracks extends ParserStateAdapter
{
	@Override
	public ParserState handleDictEnd(ITunesXmlParser parser)
	{
		return ITUNES_INPLAYLIST;
	}

	@Override
	public ParserState handleObject(ITunesXmlParser parser, String qName, String xmlChars)
	{
		if (qName.equals(XML_TAG_INTEGER))
		{
			parser.getListener().addTracktoPlaylist(xmlChars);
		}
		return this;
	}
}
