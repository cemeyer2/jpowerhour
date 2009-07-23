package ncrossley.itunes.xml;

import ncrossley.itunes.ITunesException;
import ncrossley.itunes.Resources;

/**
 * The ParserStateAdapter provides empty implementations for all the abstract
 * methods in the ParserState class, except for the dict start and end methods.
 * For the dict start and end methods, an exception is thrown, since all
 * subclasses must handle these events if it is possible to see then in that
 * state.
 * <p>
 * No instances of this class should ever be constructed;
 * it is intended purely for subclassing.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
abstract class ParserStateAdapter extends ParserState
{
	@Override
	public ParserState handleDictStart(ITunesXmlParser parser)
	{
		throw new ITunesException(Resources.getString("itunes.msgs.unexpected_dict_start", //$NON-NLS-1$
				getClass().getSimpleName()));
	}

	@Override
	public ParserState handleDictEnd(ITunesXmlParser parser)
	{
		throw new ITunesException(Resources.getString("itunes.msgs.unexpected_dict_end", //$NON-NLS-1$
				getClass().getSimpleName()));
	}

	@Override
	public ParserState handleKey(ITunesXmlParser parser, String key)
	{
		return this;
	}

	@Override
	public ParserState handleFlag(ITunesXmlParser parser, boolean flagTrue)
	{
		return this;
	}

	@Override
	public ParserState handleObject(ITunesXmlParser parser, String qName, String xmlChars)
	{
		return this;
	}
}
