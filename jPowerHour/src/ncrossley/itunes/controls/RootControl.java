package ncrossley.itunes.controls;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import ncrossley.itunes.ITunesLogger;
import ncrossley.itunes.Resources;
import ncrossley.utils.Messages;

/**
 * A RootControl handles the MUSICROOT control file entry.
 * There must be exactly one MUSICROOT control;
 * it provides the path to the root of MP3 folder hierarchy.
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class RootControl extends AbstractControl
{
	private final Controls	controls;
	private String musicRoot;

	/**
	 * Construct a new RootControl.
	 * @param controls the owning control file processor.
	 */
	RootControl(Controls controls)
	{
		this.controls = controls;
	}

	@Override
	public boolean action()
	{
		String newRoot = getParam();
		File rootDir;
		if (musicRoot != null)
		{
			Messages.stream().println(Resources.getString("itunes.msgs.dupl_root_control", //$NON-NLS-1$
				this.controls.getControlFile().getPath()));
			return false;
		}
		else if ((rootDir = new File(newRoot)).exists())
		{
			ITunesLogger.getLogger().log(Level.CONFIG,
					"itunes.dbg.found_root_control",newRoot); //$NON-NLS-1$
			try
			{
				musicRoot = rootDir.getCanonicalPath();
			}
			catch (IOException e)
			{
				// If we cannot get canonical path, just continue with non-canonical one
				musicRoot = newRoot;
			}
			return true;
		}
		else
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.music_root_does_not_exist", //$NON-NLS-1$
					newRoot,this.controls.getControlFile().getPath()));
			return false;
		}
	}

	/**
	 * Check if a valid MUSICROOT control was found.
	 * @return true iff a MUSICROOT control was found, and the named directory exists.
	 */
	public boolean isSupplied()
	{
		return musicRoot != null;
	}

	/**
	 * Gets the path of an overall iTunes music hierarchy root.
	 * @return the path of an overall iTunes music hierarchy root.
	 */
	public String getMusicRoot()
	{
		return musicRoot;
	}
}
