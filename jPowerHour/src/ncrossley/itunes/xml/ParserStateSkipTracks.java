package ncrossley.itunes.xml;


/**
 * The ParserStateSkipTracks type represents the state of the XML parser while
 * skipping the tracks in a folder from the iTunes XML data.
 * Only one instance of this type is constructed, by the parent abstract class.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
class ParserStateSkipTracks extends ParserStateAdapter
{
	@Override
	public ParserState handleDictEnd(ITunesXmlParser parser)
	{
		return ITUNES_INSKIP;
	}
}
