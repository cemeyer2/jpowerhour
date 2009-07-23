package ncrossley.itunes.xml;

import java.io.InputStream;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import ncrossley.itunes.ITunesLogger;
import ncrossley.itunes.Library;
import ncrossley.itunes.Resources;

import static ncrossley.itunes.xml.ITunesXmlConstants.XML_TAG_DICT;
import static ncrossley.itunes.xml.ITunesXmlConstants.XML_TAG_FALSE;
import static ncrossley.itunes.xml.ITunesXmlConstants.XML_TAG_KEY;
import static ncrossley.itunes.xml.ITunesXmlConstants.XML_TAG_TRUE;

/**
 * A SAX2 handler for the iTunes XML export format.
 * <p>
 * The callback actions on this handler invoke transitions in the state machine
 * implemented in the various ParserState classes, which in turn invoke
 * actions in the ITunesXmlListener, which creates new Track and Playlist objects,
 * and adds these new objects to the Library.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 *
 */
public class ITunesXmlParser extends DefaultHandler
{
	private ParserState			state;		// iTunes library parser state
	private StringBuffer		chBuffer;	// the content of an XML element we just visited
	private String				lastKey;	// the 'name' of the last <key> we saw
	private ITunesXmlListener	listener;	// the class handling the high-level parsing events

	/**
	 * Create a new parser that delegates actions to the specified handler.
	 * @param listener a handler for the callbacks for the higher-level pasing events generated
	 * by the state machine transitions; this handler must insert the tracks and playlists being read
	 * into a {@link Library}.
	 */
	public ITunesXmlParser(ITunesXmlListener listener)
	{
		super();
		this.listener = listener;
		chBuffer = new StringBuffer(200);
		state = ParserState.ITUNES_START;
	}

	/*
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
	{
		chBuffer.append(ch, start, length);
	}

	/*
	 * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
	{
		ITunesLogger.getLogger().log(Level.FINEST,"itunes.dbg.log_entity_resolver", //$NON-NLS-1$
				new String[] {publicId,systemId});
		if (systemId.equals("http://www.apple.com/DTDs/PropertyList-1.0.dtd")) //$NON-NLS-1$
		{
			// return a local copy of the DTD string, since
			// the Apple one appears not to be available half the time

			ITunesLogger.getLogger().finest("itunes.dbg.log_local_resolution"); //$NON-NLS-1$
			InputStream dtd = ITunesXmlParser.class.getResourceAsStream(
				Resources.getString("itunes.int.xml_property_file")); //$NON-NLS-1$
			if (dtd == null)
			{
				ITunesLogger.getLogger().warning("itunes.dbg.log_local_entity_missing"); //$NON-NLS-1$
				return null;
			}
			else
			{
				return new InputSource(dtd);
			}
		}
		else
		{
			// use the default behaviour
			return null;
		}
	}

	/*
	 * @inheritDoc
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String,
	 * java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
	{
		chBuffer.setLength(0);
		if (qName.equals(XML_TAG_DICT))
		{
			state = state.handleDictStart(this);
		}
	}

	/*
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
	{
		String xmlChars = chBuffer.toString();
		if (qName.equals(XML_TAG_DICT))
		{
			state = state.handleDictEnd(this);
		}
		else if (qName.equals(XML_TAG_KEY))
		{
			state = state.handleKey(this, xmlChars);
			lastKey = xmlChars;
		}
		else if (qName.equalsIgnoreCase(XML_TAG_TRUE) || qName.equalsIgnoreCase(XML_TAG_FALSE))
		{
			state = state.handleFlag(this, Boolean.parseBoolean(qName));
		}
		else
		{
			state = state.handleObject(this, qName, xmlChars);
		}
	}


	/**
	 * Gets the value of the last &lt;key&gt; tag that we saw.
	 * @return the value of the most-recently encountered &lt;key&gt; element.
	 */
	public String getLastKey()
	{
		return lastKey;
	}


	/**
	 * Gets the ITunesXmlListener being used by this parser.
	 * @return the current listener.
	 */public ITunesXmlListener getListener()
	{
		return listener;
	}
}
