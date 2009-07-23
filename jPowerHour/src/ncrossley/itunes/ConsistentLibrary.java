package ncrossley.itunes;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ncrossley.utils.Messages;


/**
 * Scan a directory on disk, and build up a tree of folder and file names.
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class ConsistentLibrary extends DirCheck
{
	/**
	 * Construct a directory scanner for the given directory full of music files.
	 * @param dirPath a path to the root directory under which you store all your music files.
	 */
	public ConsistentLibrary(String dirPath)
	{
		super(dirPath);
	}

	/**
	 * Check to see if each music file in the hierarchy is used in the iTunes library.
	 * @param library The iTunes library to be checked.
	 * @param ignore A set of files to be ignored; may be null.
	 * @return returns true iff each music file appears in the iTunes library.
	 */
	public boolean checkAllFilesInLibrary(Library library, Collection<String> ignore)
	{
		return checkAllFilesInPlaylist(library.findMasterPlaylist(),ignore,
				"itunes.msgs.files_not_in_library"); //$NON-NLS-1$
	}

	/**
	 * Check to see if each music file in the iTunes library is in the directory hierarchy.
	 * @param library The iTunes library to be checked.
	 * @param ignore A set of files that must exist, but need not be in the normal directory
	 * hierarchy; may be null.
	 * @return returns true iff each track's music file exists, and (if not ignored)
	 * appears in the hierarchy.
	 */
	public boolean checkAllLibraryInFiles(Library library, Collection<File> ignore)
	{
		ITunesLogger.getLogger().finer("itunes.msgs.checking_library_in_files"); //$NON-NLS-1$

		boolean success = true;
		Set<File> notInDir = new HashSet<File>();

		// Check each track's music file can be read
		for (File file : library.findMasterPlaylist().getFiles())
		{
			if (!file.canRead())
			{
				notInDir.add(file);
			}
		}

		if (notInDir.size() != 0)
		{
			Messages.stream().println(Resources.getString("itunes.msgs.music_not_found")); //$NON-NLS-1$
			for (File path : notInDir)
			{
				Messages.stream().println(SPACE_INDENT+path.getPath());
			}
			success = false;
		}

		// For files in dir check, skip both files to be ignored and files we have already noted as missing
		notInDir.addAll(ignore);
		success &= checkAllMusicInFiles(library.findMasterPlaylist(), notInDir,
				"itunes.msgs.music_not_in_hierarchy"); //$NON-NLS-1$

		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_library_in_files",success); //$NON-NLS-1$
		return success;
	}
}
