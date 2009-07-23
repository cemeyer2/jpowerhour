package ncrossley.itunes.xml;

/**
 * The ParserStateStart type represents the initial start state of the XML parser.
 * Only one instance of this type is constructed, by the parent abstract class.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
class ParserStateStart extends ParserStateAdapter
{
	@Override
	public ParserState handleDictStart(ITunesXmlParser parser)
	{
		return ParserState.ITUNES_LIBRARY;
	}
}
