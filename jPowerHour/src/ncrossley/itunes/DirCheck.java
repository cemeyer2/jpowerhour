package ncrossley.itunes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import ncrossley.utils.DirInfo;
import ncrossley.utils.Messages;

/**
 * Scan a directory on disk, and check consistency with a playlist.
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class DirCheck
{
	/**
	 * Padding to be printed before each file path not present or not in this directory hierarchy.
	 */
	protected static final String	SPACE_INDENT	= "   "; //$NON-NLS-1$

	private DirInfo musicRoot;
	private String dirPath;

	/**
	 * Construct a directory scanner for the given directory full of music files.
	 * @param dirPath a path to the root directory under which you store the music files for a playlist.
	 */
	public DirCheck(String dirPath)
	{
		musicRoot = new DirInfo(dirPath);
		this.dirPath = musicRoot.getPath();
	}

	/**
	 * Gets the root directory with which this DirCheck was constructed.
	 * @return the root path for this directory hierarchy.
	 */
	public String getDirPath()
	{
		return dirPath;
	}

	/**
	 * Check to see if each music file in this hierarchy is used in a specified iTunes playlist.
	 * @param playlist The iTunes playlist to be checked.
	 * @param ignore a set of file patterns to be ignored; may be null.
	 * @param hdrMessage The ID of a message to be shown above the list of files not in the playlist.
	 * @return returns true iff each music file appears in the playlist.
	 */
	public boolean checkAllFilesInPlaylist(Playlist playlist, Collection<String> ignore, String hdrMessage)
	{
		ITunesLogger.getLogger().log(Level.FINER,"itunes.msgs.checking_files_in_list", //$NON-NLS-1$
				new String[] {dirPath, playlist.getName()});

		// get list of all music files under musicroot
		Collection<File> allFiles = getFiles();
		filter(allFiles);

		// get list of all music files in iTunes playlist
		Collection<File> allTracks = playlist.getFiles();

		allFiles.removeAll(allTracks);
		removeMatching(allFiles,ignore);

		// Try again, resolving symbolic links, etc.
		if (allFiles.size() != 0)
		{
			Collection<File> canonicalFiles = new ArrayList<File>();
			for (File file : allTracks)
			{
				try
				{
					canonicalFiles.add(file.getCanonicalFile());
				}
				catch (IOException e)
				{
					// ignore
				}
			}
			allFiles.removeAll(canonicalFiles);
		}

		if (allFiles.size() != 0)
		{
			Messages.stream().println(Resources.getString(hdrMessage,getDirPath(),playlist.getName()));
			for (File path : allFiles)
			{
				Messages.stream().println(SPACE_INDENT+path.getPath());
			}
			ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_files_in_list", //$NON-NLS-1$
				new Object[] {dirPath, playlist.getName(),false});
			return false;
		}

		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_files_in_list", //$NON-NLS-1$
			new Object[] {dirPath, playlist.getName(),true});
		return true;
	}


	/**
	 * Check to see if each music file in the specified iTunes playlist is in this directory hierarchy.
	 * @param playlist The iTunes playlist to be checked.
	 * @param ignore a set of files (not patterns!) to be ignored; may be null.
	 * @param hdrMessage The ID of a message to be shown above the list of files not in the hierarchy.
	 * @return returns true iff each track's music file appears in this hierarchy.
	 */
	public boolean checkAllMusicInFiles(Playlist playlist, Collection<File> ignore, String hdrMessage)
	{
		ITunesLogger.getLogger().log(Level.FINER,"itunes.msgs.checking_music_in_files", //$NON-NLS-1$
			new String[] {playlist.getName(), dirPath});

		// get list of all music files under musicroot
		Collection<File> allFiles = getFiles();
		filter(allFiles);

		// get list of all music files in iTunes playlist
		Collection<File> allTracks = playlist.getFiles();

		allTracks.removeAll(allFiles);
		if (ignore != null)
		{
			allTracks.removeAll(ignore);
		}

		if (allTracks.size() != 0)
		{
			Messages.stream().println(Resources.getString(hdrMessage,getDirPath(),playlist.getName()));
			for (File path : allTracks)
			{
				Messages.stream().println(SPACE_INDENT+path.getPath());
			}
			ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_music_in_files", //$NON-NLS-1$
				new Object[] {playlist.getName(), dirPath, false});
			return false;
		}

		ITunesLogger.getLogger().log(Level.FINE,"itunes.msgs.checked_music_in_files", //$NON-NLS-1$
			new Object[] {playlist.getName(), dirPath, true});
		return true;
	}


	private void removeMatching(Collection<File> files, Collection<String> ignore)
	{
		// remove files matching the given ignore patterns
		ITunesLogger.getLogger().finest("itunes.dbg.remove_matching_files"); //$NON-NLS-1$
		if (ignore == null)  return;
		for (Iterator<File> fileIter = files.iterator(); fileIter.hasNext();)
		{
			File file = fileIter.next();
			for (String pattern : ignore)
			{
				if (file.getPath().matches(pattern))
				{
					fileIter.remove();
					break;
				}
			}
		}
	}


	private void filter(Collection<File> allFiles)
	{
		// remove Icon and .DS_Store files from all files collection
		for (Iterator<File> iter = allFiles.iterator(); iter.hasNext();)
		{
			File file = iter.next();
			if (file.getName().equals("Icon\r") ||  //$NON-NLS-1$
				file.getName().equals(".DS_Store")) //$NON-NLS-1$
			{
				iter.remove();
			}
		}
	}


	private Collection<File> getFiles()
	{
		try
		{
			return musicRoot.allFiles();
		}
		catch (IOException e)
		{
			throw new ITunesException(Resources.getString(
					"itunes.msgs.unexpected_file_type",e.getMessage())); //$NON-NLS-1$
		}
	}
}
