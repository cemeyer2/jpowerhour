package ncrossley.utils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A DirInfo object holds information about a directory, including
 * list of its member files and directories.
 * It is also a proxy for many of the operations of the class java.io.File.
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 */

public class DirInfo implements Comparable<Object>
{
	private File dirFile;
	private Collection<File> subFiles;
	private Collection<DirInfo> subDirs;


	/**
	 * Construct a DirInfo for the specified directory.
	 * @param path An absolute or relative path to the directory.
	 */
	public DirInfo(String path)
	{
		dirFile = new File(path);
	}


	/**
	 * Construct a DirInfo for the specified directory.
	 * @param dir An absolute or relative path to the directory.
	 */
	public DirInfo(File dir)
	{
		dirFile = dir;
	}


	/**
	 * Returns the base name of the directory.
	 * @return The base name of the directory (the last part of the path name).
	 */
	public String getName()
	{
		return dirFile.getName();
	}


	/**
	 * Returns the absolute path name of the directory.
	 * @return The absolute path name of the directory.
	 */
	public String getPath()
	{
		return dirFile.getPath();
	}


	/**
	 * Create the directory.
	 * @return true if the directory was created successfully.
	 */
	public boolean create()
	{
		return dirFile.mkdir();
	}


	/**
	 * Delete the directory.
	 * @return true if the directory was deleted successfully.
	 */
	public boolean delete()
	{
		return dirFile.delete();
	}


	/**
	 * Create a new file in the directory.
	 * @param name the name of the file to be created.
	 * @return the newly created file.
	 */
	public File newFile(String name)
	{
		try
		{
			File newF = new File(dirFile, name);
			if (newF.createNewFile())
			{
				return newF;
			}
			else
			{
				return null;
			}
		}
		catch (IOException ioe)
		{
			return null;
		}
	}


	/**
	 * Return the list of files in this directory.
	 * @return an unmodifiable collection of files.
	 * @throws IOException if the directory cannot be read, or contains an unexpected file type.
	 */
	public Collection<? extends File> listFiles() throws IOException
	{
		if (subFiles == null)
		{
			traverse();
		}
		return Collections.unmodifiableCollection(subFiles);
	}


	/**
	 * Return the list of subdirectories in this directory.
	 * @return an unmodifiable collection of DirInfo directories.
	 * @throws IOException if the directory cannot be read, or contains an unexpected file type.
	 */
	public Collection<? extends DirInfo> listDirs() throws IOException
	{
		if (subDirs == null)
		{
			traverse();
		}
		return Collections.unmodifiableCollection(subDirs);
	}


	private void traverse() throws IOException
	{
		subFiles = new TreeSet<File>();
		subDirs = new TreeSet<DirInfo>();

		File[] files = dirFile.listFiles();
		if (files==null)
		{
			throw new IOException(dirFile.getPath());
		}
		for (File f : files)
		{
			if (f.isFile())
			{
				subFiles.add(f);
			}
			else if (f.isDirectory())
			{
				DirInfo subDir = new DirInfo(f);
				subDirs.add(subDir);
			}
			else
			{
				throw new IOException(f.getPath());
			}
		}
	}


	/**
	 * Compare one directory pathname to another, lexicographically.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param obj The other DirInfo to compare.
	 * @return <0, =0, >0, according to whether this directory comes
	 * lexicographically before, equal to, or after the directory specified
	 * by the parameter <code>obj</code>.
	 */
	public int compareTo(Object obj)
	{
		return dirFile.compareTo(((DirInfo) obj).dirFile);
	}


	/**
	 * Check if one DirInfo equals another, by comparing path names.
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj The other DirInfo to compare.
	 * @return true if and only if the two DirInfo objects refer to the same directory path.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if ((obj != null) && (obj instanceof DirInfo))
		{
			return compareTo(obj) == 0;
		}
		return false;
	}


	/**
	 * Since we overrode equals(), we must subclass hashCode() to preserve contract.
	 * @see java.lang.Object#hashCode()
	 * @return a hash code for the directory's File object, which in turn is a hash
	 * code for the path name.
	 */
	@Override
	public int hashCode()
	{
		return dirFile.hashCode();
	}


	/**
	 * Returns a set of all the files recursively below this directory.
	 * @return the set of all files in and below this directory.
	 * @throws IOException if the directory cannot be read, or contains an unexpected file type.
	 */
	public Collection<File> allFiles() throws IOException
	{
		Collection<File> allFiles = new TreeSet<File>(listFiles());
		for (DirInfo subDir : listDirs())
		{
			allFiles.addAll(subDir.allFiles());
		}
		return allFiles;
	}
}
