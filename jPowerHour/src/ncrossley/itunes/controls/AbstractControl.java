package ncrossley.itunes.controls;

import java.util.regex.Matcher;


/**
 * An AbstractControl represents a control - that is, a specification of how a check is to
 * be performed, or control/configuration of such a check.  An iTunes control file (as
 * read by the {@link Controls} class) holds a number of such controls.  Each such
 * control is represented by a concrete subclass of this class.
 * <p>
 * Every control must provide an action that gathers information from the parsed control
 * when that control is found by the parser.
 * <p>
 * Most controls will also provide an operation to return some appropriate object
 * representing that information.
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public abstract class AbstractControl
{
	private Matcher matcher;

	/**
	 * Sets the matcher.
	 * @param matcher the matcher value to set.
	 */
	public void setMatcher(Matcher matcher)
	{
		this.matcher = matcher;
	}

	/**
	 * Get the first submatch to the control file matcher.
	 * @return the first submatched string, if any.
	 */
	public String getParam()
	{
		return getParam(1);
	}

	/**
	 * Get the numbered submatch to the control file matcher.
	 * @param param the substring pattern number for the
	 * matcher that was created by {@link #setMatcher(Matcher)}.
	 * @return the string matching the given subpattern, if any.
	 */
	public String getParam(int param)
	{
		String paramMatch = matcher.group(param);
		return paramMatch == null ? null : paramMatch.trim();
	}

	/**
	 * This method is called when the parser finds a control of the
	 * appropriate type in the control file.  Each subclass must implement
	 * this method, extracting and storing the data in the matcher that
	 * was created by {@link #setMatcher(Matcher)}.
	 * @return true iff the control was processed successfully.
	 */
	public abstract boolean action();
}
