package ncrossley.itunes;

import java.io.File;

import ncrossley.annotations.SuppressWarnings;
import ncrossley.itunes.controls.Controls;
import ncrossley.utils.Messages;


/**
 * Main class for iTunes library analyser CLI entry point.
 * <p>
 * This class analyzes the command line flags, then creates an appropriate
 * ITunesChecks instance to run the checks.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class ITunes
{
	private boolean verbose;
	private Controls controls;


	/**
	 * Construct a new ITunes.
	 * @param controls an as-yet unread Controls structure;
	 * the {@link #runChecks()} method will read these controls,
	 * then run the appropriate checks on the iTunes library.
	 * @param verbose true iff the checks should print progress messages.
	 */
	public ITunes(Controls controls, boolean verbose)
	{
		this.controls = controls;
		this.verbose = verbose;
	}


	/**
	 * Run the iTunes library analyser main program.
	 * @param args There is one optional flag: -v or -verbose, which turns on printing of
	 * messages for successful checks.  After the optional flag, there is one optional argument.
	 * This argument should be the path to a control file;
	 * if no control file is specified, the default is in the properties file.
	 */
	public static void main(String[] args)
	{
		ITunes iTunes = checkUsage(args);
		if (iTunes == null)
		{
			// Failed to parse arguments successfully - message already printed
			exit(1);
		}
		else if (iTunes.runChecks())
		{
			Messages.stream().println(Resources.getString("itunes.msgs.passed_all_checks", //$NON-NLS-1$
				iTunes.controls.getMusicRoot()));
		}
		System.out.print(Messages.messages());
	}

	private static ITunes checkUsage(String[] args)
	{
		Controls	controls;
		boolean	verboseFlag	= false;
		int		index		= 0;

		if (args.length >= 1 && (args[0].equals("-v") ||  //$NON-NLS-1$
			args[0].equals("-verbose")))  //$NON-NLS-1$
		{
			verboseFlag = true;
			index = 1;
		}

		if (args.length > index+1)
		{
			Messages.stream().println(Resources.getString("itunes.msgs.usage")); //$NON-NLS-1$
			return null;
		}
		else
		{
			String controlFilePath = (args.length == index+1 ? args[index]
				: Resources.getString("itunes.dflt.default_control_file")); //$NON-NLS-1$
			controls = new Controls(new File(controlFilePath));
			return new ITunes(controls,verboseFlag);
		}
	}


	/**
	 * Tidy up and exit.
	 * @param exitStatus the system exit status.
	 */
	@SuppressWarnings("Dm")
	public static void exit(int exitStatus)
	{
		System.exit(exitStatus);
	}


	/**
	 * Read the control file given to the Controls for this ITunes,
	 * then run all the tests specified in those Controls.
	 * @return true iff all tests pass.
	 */
	public boolean runChecks()
	{
		if (!controls.read())
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.cannot_read_control_file", //$NON-NLS-1$
					controls.getControlFile().getPath()));
			return false;
		}
		else if (verbose)
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.read_control_file", //$NON-NLS-1$
					controls.getControlFile().getPath()));
		}

		ITunesChecks checks = new ITunesChecks(controls, verbose);
		return checks.runAllChecks();
	}
}
