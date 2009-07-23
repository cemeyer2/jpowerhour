package ncrossley.itunes;

/**
 * An ITunesException is an unchecked exception created somewhere in the
 * ITunes package.  The underlying problem is usually described in a
 * nested exception.
 * <p>
 * It is expected that the main iTunes program will catch this exception,
 * and print some appropriate messages.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 *
 */
public class ITunesException extends RuntimeException
{
	/**
	 * Create a new ITunesException with the given message, but no underlying cause.
	 * @param message A message explaining the exception.
	 */
	public ITunesException(String message)
	{
		super(message);
	}

	/**
	 * Create a new ITunesException with the given message and underlying cause.
	 * @param message A message explaining the exception.
	 * @param cause The underlying exception.
	 */
	public ITunesException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
