package ncrossley.itunes.controls;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ncrossley.itunes.ITunesLogger;
import ncrossley.itunes.Resources;
import ncrossley.utils.Messages;

/**
 * A DirControl handles the DIR control file entry.
 * The DIR entry is optional; there may be any number of DIR entries.
 * For each DIR entry, the contents of the specified playlist are cross-checked against
 * the contents of the specified folder hierarchy.
 * <p>
 * The correspondence between an individual playlist and folder hierarchy is
 * maintained by the inner class PlaylistControl.
 * <p>
 * @see Controls
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */
public class DirControl extends AbstractControl
{
	private final Controls controls;
	private Set<PlaylistControl> dirControls = new HashSet<PlaylistControl>();

	/**
	 * Construct a new DirControl.
	 * @param controls the owning control file processor.
	 */
	public DirControl(Controls controls)
	{
		this.controls = controls;
	}

	@Override
	public boolean action()
	{
		String playlistName = getParam();
		String path = getParam(2);
		File dir = new File(path);
		if (controls.getMusicRoot() != null && !dir.isAbsolute())
		{
			dir = new File(controls.getMusicRoot(),path);
		}
		if (dir.exists())
		{
			try
			{
				dir = dir.getCanonicalFile();
			}
			catch (IOException e)
			{
				// Leave original non-canonical path if we cannot get canonical one
			}
			PlaylistControl playlistControl = new PlaylistControl(playlistName,dir.getPath());
			dirControls.add(playlistControl);
			ITunesLogger.getLogger().log(Level.FINE,"itunes.dbg.found_dir_control", //$NON-NLS-1$
				new String[]{playlistControl.getPlaylistName(),playlistControl.getMusicDir()});
			return true;
		}
		else
		{
			Messages.stream().println(Resources.getString(
					"itunes.msgs.playlist_dir_does_not_exist", //$NON-NLS-1$
					path,controls.getControlFile().getPath()));
			return false;
		}
	}

	/**
	 * Get the set of playlists and directories to be matched.
	 * @return A set of PlaylistControl structures,
	 * each one defining a playlist name
	 * and a directory to be cross-checked.
	 */
	public Set<PlaylistControl> getData()
	{
		return Collections.unmodifiableSet(dirControls);
	}

	/**
	 * A PlaylistControl (a static inner class of PlaylistControl) is a simple pair
	 * of a playlist name and a directory.  These pairings are used to check that a playlist
	 * contains all the music in some directory hierarchy, and vice versa.
	 * @author Copyright (c) 2005-2008 Nick Crossley
	 */
	public static class PlaylistControl
	{
		private String playlistName;
		private String musicDir;

		/**
		 * Construct a new PlaylistControl with the given name and dir.
		 * @param playlistName the name of the playlist.
		 * @param musicDir the path to the corresponding music directory.
		 */
		public PlaylistControl(String playlistName,String musicDir)
		{
			this.playlistName = playlistName;
			this.musicDir = musicDir;
		}

		/**
		 * Returns the name of a playlist.
		 * @return the name of a playlist.
		 */
		public String getPlaylistName()
		{
			return playlistName;
		}

		/**
		 * Returns the path of a playlist directory.
		 * @return the path of a playlist directory.
		 */
		public String getMusicDir()
		{
			return musicDir;
		}
	}
}
