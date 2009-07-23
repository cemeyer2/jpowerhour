package ncrossley.itunes.controls;

import java.io.File;
import java.util.logging.Level;

import ncrossley.itunes.ITunesLogger;
import ncrossley.itunes.Resources;
import ncrossley.utils.Messages;

/**
 * An XML handles the XML control file entry.  There must be exactly one XML control;
 * it provides the path to an iTunes XML library (exported, or the master iTunes library itself).
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class XMLControl extends AbstractControl
{
	private final Controls controls;
	private String iTunesXML;

	/**
	 * Construct a new XMLControl.
	 * @param controls the owning control file processor.
	 */
	XMLControl(Controls controls)
	{
		this.controls = controls;
	}

	@Override
	public boolean action()
	{
		String newXML = getParam();
		if (iTunesXML != null)
		{
			Messages.stream().println(Resources.getString("itunes.msgs.dupl_xml_control", //$NON-NLS-1$
				controls.getControlFile().getPath()));
			return false;
		}
		else if (new File(newXML).exists())
		{
			ITunesLogger.getLogger().log(Level.CONFIG,"itunes.dbg.found_xml_control",newXML); //$NON-NLS-1$
			iTunesXML = newXML;
			return true;
		}
		else
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.xml_export_does_not_exist", //$NON-NLS-1$
					newXML,controls.getControlFile().getPath()));
			return false;
		}
	}

	/**
	 * Check if a valid XML control was found.
	 * @return true iff a XML control was found, and the named file exists.
	 */
	public boolean isSupplied()
	{
		return iTunesXML != null;
	}

	/**
	 * Get the path of an iTunes XML library.
	 * @return the path of an iTunes XML library.
	 */
	public String getITunesXML()
	{
		return iTunesXML;
	}
}
