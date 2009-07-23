package ncrossley.itunes;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import ncrossley.itunes.xml.ITunesXmlHandler;
import ncrossley.itunes.xml.ITunesXmlParser;

/**
 * Read an XML file that contains an exported iTunes library.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 *
 */
public class LibraryReader
{
	private SAXParserFactory factory;
	private SAXParser parser;

	LibraryReader()
	{
		try
		{
			factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
		}
		catch (FactoryConfigurationError fce)
		{
			throw new ITunesException(Resources.getString(
					"itunes.msgs.no_sax_parser_factory"),fce); //$NON-NLS-1$
		}
		try
		{
			parser = factory.newSAXParser();
		}
		catch (ParserConfigurationException pce)
		{
			throw new ITunesException(Resources.getString(
					"itunes.msgs.no_sax_parser"),pce); //$NON-NLS-1$
		}
		catch (SAXException se)
		{
			throw new ITunesException(Resources.getString(
					"itunes.msgs.cannot_setup_sax"),se); //$NON-NLS-1$
		}
	}

	/**
	 * Read from an XML file, adding tracks and playlists to an iTunes library.
	 * @param library The iTunes Library to which the tracks and playlists are added.
	 * @param file The file to read.  This file was probably produced by exporting
	 * a library from iTunes.
	 * @return The number of tracks added to the library.
	 */
	public int read(Library library, File file)
	{
		int oldTracks = library.getTrackCount();
		try
		{
			parser.parse(file, new ITunesXmlParser(new ITunesXmlHandler(library)));
		}
		catch (IOException ioe)
		{
			throw new ITunesException(MessageFormat.format(
				Resources.getString("itunes.msgs.cannot_read_xml"), //$NON-NLS-1$
				file.getAbsolutePath()),ioe);
		}
		catch (SAXException se)
		{
			throw new ITunesException(MessageFormat.format(
				Resources.getString("itunes.msgs.cannot_parse_xml"), //$NON-NLS-1$
				file.getAbsolutePath()),se);
		}
		return library.getTrackCount() - oldTracks;
	}
}
