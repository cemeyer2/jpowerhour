package ncrossley.itunes;

import java.io.File;
import java.util.Collection;


/**
 * Scan a directory on disk, and check consistency with a playlist.
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class ConsistentPlaylist extends DirCheck
{
	/**
	 * Construct a directory scanner for the given directory full of music files.
	 * @param dirPath a path to the root directory under which you store the music files for a playlist.
	 */
	public ConsistentPlaylist(String dirPath)
	{
		super(dirPath);
	}

	/**
	 * Check to see if each music file in this hierarchy is used in a specified iTunes playlist.
	 * @param playlist The iTunes playlist to be checked.
	 * @param ignore A set of files to be ignored; may be null.
	 * @return returns true iff each music file appears in the playlist.
	 */
	public boolean checkAllFilesInPlaylist(Playlist playlist, Collection<String> ignore)
	{
		return checkAllFilesInPlaylist(playlist,ignore,"itunes.msgs.files_not_in_playlist"); //$NON-NLS-1$
	}

	/**
	 * Check to see if each music file in the iTunes playlist is in this directory hierarchy.
	 * @param playlist The iTunes playlist to be checked.
	 * @param ignore A set of files to be ignored; may be null.
	 * @return returns true iff each track's music file appears in this hierarchy.
	 */
	public boolean checkAllMusicInFiles(Playlist playlist, Collection<File> ignore)
	{
		return checkAllMusicInFiles(playlist,ignore,"itunes.msgs.music_not_in_dir"); //$NON-NLS-1$
	}
}
