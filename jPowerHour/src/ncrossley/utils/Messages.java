package ncrossley.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * The Messages class provides an output stream buffer for messages,
 * and a way to retrieve and clear those messages.
 * Messages may be added to the buffer using the standard stream print and println methods.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public final class Messages
{
	private static final Messages DEFAULT = new Messages();

	private ByteArrayOutputStream buffer;
	private PrintStream stream;

	/**
	 * Construct a new Messages object,
	 * and set the embedded PrintStream to print to the embedded buffer.
	 */
	public Messages()
	{
		buffer = new ByteArrayOutputStream(1024);
		stream = new PrintStream(buffer, true);
	}

	/**
	 * Return the message stream for the given Messages class.
	 * @return the message stream.
	 */
	public PrintStream getStream()
	{
		return stream;
	}

	/**
	 * Return the default (global) message stream.
	 * @return the default message stream.
	 */
	public static PrintStream stream()
	{
		return DEFAULT.stream;
	}

	/**
	 * Return the messages for the given Messages class.
	 * @return the contents of the message buffer.
	 */
	public String getMessages()
	{
		return buffer.toString();
	}

	/**
	 * Return the default (global) messages.
	 * @return the contents of the default message buffer.
	 */
	public static String messages()
	{
		return DEFAULT.buffer.toString();
	}

	/**
	 * Clear the message buffer for the given Messages class.
	 */
	public void clearMessages()
	{
		buffer.reset();
	}

	/**
	 * Clear the default (global) message buffer.
	 */
	public static void clear()
	{
		DEFAULT.buffer.reset();
	}
}
