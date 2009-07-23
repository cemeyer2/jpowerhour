package net.charliemeyer.jpowerhour.itunes;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import ncrossley.itunes.Library;
import ncrossley.itunes.Playlist;
import ncrossley.itunes.Track;

public class ITunesPlayListParser 
{
	private static Library getITunesLibrary()
	{
		File libraryFile = ITunesUtils.getItunesLibrary();
		if(libraryFile != null)
		{
			Library library = new Library(libraryFile);
			return library;
		}
		return null;
	}
	
	public static Collection<Playlist> getITunesPlaylists()
	{
		Library library = getITunesLibrary();
		Collection<Playlist> playlists = library.getPlaylists();
		return playlists;
	}
}
