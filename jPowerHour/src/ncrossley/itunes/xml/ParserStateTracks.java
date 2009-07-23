package ncrossley.itunes.xml;


/**
 * The ParserStateTracks type represents the state of the XML parser while reading
 * the track number from the iTunes XML data, and before reading any other track data.
 * Only one instance of this type is constructed, by the parent abstract class.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
class ParserStateTracks extends ParserStateAdapter
{
	@Override
	public ParserState handleDictStart(ITunesXmlParser parser)
	{
		return ParserState.ITUNES_INTRACK;
	}

	@Override
	public ParserState handleDictEnd(ITunesXmlParser parser)
	{
		return ParserState.ITUNES_LIBRARY;
	}

	@Override
	public ParserState handleKey(ITunesXmlParser parser, String key)
	{
		parser.getListener().startTrack(key);
		return this;
	}
}
