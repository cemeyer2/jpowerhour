package ncrossley.itunes.xml;

/**
 * The ParserStatePlaylists type represents the state of the XML parser between
 * reading each of the playlist details from the iTunes XML data.
 * Only one instance of this type is constructed, by the parent abstract class.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
class ParserStatePlaylists extends ParserStateAdapter
{
	@Override
	public ParserState handleDictStart(ITunesXmlParser parser)
	{
		parser.getListener().startPlaylist();
		return ITUNES_INPLAYLIST;
	}

	@Override
	public ParserState handleDictEnd(ITunesXmlParser parser)
	{
		return ITUNES_START;
	}
}
