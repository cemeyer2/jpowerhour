package ncrossley.itunes.controls;

import java.util.EnumSet;
import java.util.logging.Level;

import ncrossley.itunes.Check;
import ncrossley.itunes.ITunesLogger;
import ncrossley.itunes.Resources;
import ncrossley.utils.Messages;

/**
 * A ChecksControl handles the CHECKS control file entry.
 * The CHECKS entry is optional; there may be only one such entry.
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class ChecksControl extends AbstractControl
{
	private final Controls controls;
	private EnumSet<Check> checkSet;


	/**
	 * Construct a new ChecksControl.
	 * @param controls the owning control file processor.
	 */
	ChecksControl(Controls controls)
	{
		this.controls = controls;
	}


	@Override
	public boolean action()
	{
		String checkList = getParam();
		if (checkSet != null)
		{
			Messages.stream().println(Resources.getString("itunes.msgs.dupl_checks_control", //$NON-NLS-1$
				this.controls.getControlFile().getPath()));
			return false;
		}
		else if (checkList.equalsIgnoreCase("none")) //$NON-NLS-1$
		{
			checkSet = EnumSet.noneOf(Check.class);
			ITunesLogger.getLogger().log(Level.FINE,
				"itunes.dbg.found_checks_control",checkList); //$NON-NLS-1$
			return true;
		}
		else
		{
			try
			{
				checkSet = EnumSet.noneOf(Check.class);
				for (String checkName : checkList.split(
					Resources.getString("itunes.int.comma_sep"))) //$NON-NLS-1$
				{
					checkSet.add(Check.checkValue(checkName));
				}
				ITunesLogger.getLogger().log(Level.FINE,
						"itunes.dbg.found_checks_control",checkList); //$NON-NLS-1$
				return true;
			}
			catch (IllegalArgumentException e)
			{
				Messages.stream().println(Resources.getString(
					"itunes.msgs.bad_checks_control",checkList, //$NON-NLS-1$
					EnumSet.allOf(Check.class)));
				return false;
			}
		}
	}

	/**
	 * Test if a specified check is to be performed on the playlists.
	 * @param check the Check we are enquiring about
	 * @return true if the given Check is to be performed, false if it is not
	 */
	public boolean shouldCheck(Check check)
	{
		return checkSet == null ? true : checkSet.contains(check);
	}
}
