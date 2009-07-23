package ncrossley.itunes;

import ncrossley.utils.WordUtils.MessageGetter;


/**
 * This implementation of {@link ncrossley.utils.WordUtils.MessageGetter}
 * is used to generate error messages for tracks, playlists, albums, etc. that
 * start with deprecated words, or have leading or trailing white space.
 * <p>
 * For example, a call to getMessage("The Beatles","the") might produce a message
 * of the form "The artist 'The Beatles' starts with the deprecated word 'the'".
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class BadNameMessageGetter implements MessageGetter
{
	private String whiteSpaceMsg;
	private String badWordMessage;
	private String param;


	/**
	 * Construct a new FirstWordMessageGetter with the given messages and no extra parameter.
	 * @param whiteSpaceMsg the resource key for a formattable message about a name with leading or trailing white space
	 * @param badWordMessage the resource key for a formattable message about a name starting with a deprecated word
	 */
	public BadNameMessageGetter(String whiteSpaceMsg, String badWordMessage)
	{
		this(whiteSpaceMsg, badWordMessage, ""); //$NON-NLS-1$
	}


	/**
	 * Construct a new FirstWordMessageGetter with the given messages and one extra parameter.
	 * @param whiteSpaceMsg the resource key for a formattable message about a name with leading or trailing white space
	 * @param badWordMessage the resource key for a formattable message about a name starting with a deprecated word
	 * @param param the message parameter
	 */
	public BadNameMessageGetter(String whiteSpaceMsg, String badWordMessage, String param)
	{
		this.whiteSpaceMsg = whiteSpaceMsg;
		this.badWordMessage = badWordMessage;
		this.param = param;
	}


	public String getWhiteSpaceMessage(String phrase)
	{
		return Resources.getString(whiteSpaceMsg,phrase,param);
	}


	public String getBadWordMessage(String phrase, String firstWord)
	{
		return Resources.getString(badWordMessage,phrase,firstWord,param);
	}
}
