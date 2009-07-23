package ncrossley.itunes.xml;

import static ncrossley.itunes.xml.ITunesXmlConstants.*;

/**
 * The ParserStateInTrack type represents the state of the XML parser while reading
 * all the details for a single track from the iTunes XML data.
 * Only one instance of this type is constructed, by the parent abstract class.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
class ParserStateInTrack extends ParserStateAdapter
{
	@Override
	public ParserState handleDictEnd(ITunesXmlParser parser)
	{
		parser.getListener().completeTrack();
		return ParserState.ITUNES_TRACKS;
	}


	@Override
	public ParserState handleFlag(ITunesXmlParser parser, boolean flagTrue)
	{
		final String key = parser.getLastKey();
		if (key.equals(XML_TAG_PODCAST))
		{
			parser.getListener().setPodcastTrack(flagTrue);
		}
		return this;
	}


	@Override
	public ParserState handleObject(ITunesXmlParser parser, String qName, String xmlChars)
	{
		if (qName.equals(XML_TAG_STRING))
		{
			parser.getListener().handleTrack(parser.getLastKey(),xmlChars);
		}
		return this;
	}
}
