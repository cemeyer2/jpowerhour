package ncrossley.utils;

import java.util.Set;


/**
 * Various word-based utilities.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public final class WordUtils
{
	private WordUtils()
	{
		// No instances of this class should ever be created
	}

	/**
	 * A MessageGetter is used to generate appropriate error messages
	 * for deprecated words or leading/trailing white space.
	 * <p>
	 * For example, a call to getBadWordMessage("The Beatles","the") might produce a message
	 * of the form "The artist 'The Beatles' starts with the deprecated word 'the'".
	 * <p>
	 * @author Copyright (c) 2005-2008 Nick Crossley
	 */
	public interface MessageGetter
	{
		/**
		 * Get an error message indicating that leading or trailing white space was found.
		 * @param phrase the phrase that was checked.
		 * @return the message, probably including the given phrase.
		 */
		String getWhiteSpaceMessage(String phrase);

		/**
		 * Get an error message indicating that a deprecated first word was found.
		 * @param phrase the phrase that was checked.
		 * @param firstWord the deprecated word that was found to start the phrase.
		 * @return the message, probably including the given phrase and deprecated word.
		 */
		String getBadWordMessage(String phrase, String firstWord);
	}

	/**
	 * Check that the given phrase does not start with any of the specified words,
	 * and does not have leading or trailing white space.
	 * @param phrase the phrase to check.  Words are assumed to be space separated.
	 * @param noStartWords a Set of deprecated starting words.
	 * @param msgGetter a object that produces an appropriate error message if
	 * the phrase does in fact start with a deprecated word.
	 * @param exempt a set of names that are valid even if they do start with bad words
	 * @return true if the phrase is OK - that is, does not start with a deprecated word,
	 * and false if the phrase does start with a deprecated word.
	 */
	public static boolean checkPhrase(String phrase, Set<String> noStartWords, Set<String> exempt,
			MessageGetter msgGetter)
	{
		if (phrase == null || phrase.length() == 0) return true;
		if (exempt.contains(phrase)) return true;

		if (phrase.matches("^\\s.*|.*\\s$")) //$NON-NLS-1$
		{
			Messages.stream().println(msgGetter.getWhiteSpaceMessage(phrase));
			return false;
		}

		String[] words;
		if ((words = phrase.split(" ")).length == 0) return true; //$NON-NLS-1$
		String firstWord = words[0];
		for (String noStart : noStartWords)
		{
			if (firstWord.equalsIgnoreCase(noStart))
			{
				Messages.stream().println(msgGetter.getBadWordMessage(phrase,firstWord));
				return false;
			}
		}
		return true;
	}
}
