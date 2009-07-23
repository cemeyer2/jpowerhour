package ncrossley.utils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 * A DirTreeNode is a tree node representing a directory or file in a directory
 * hierarchy.
 * <p>
 * DirTreeNode extends DefaultMutableTreeNode, and so may be used as part of the
 * model behind a JTree.
 *
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 *
 */
public class DirTreeNode extends DefaultMutableTreeNode
{
	private boolean				loadedChildren;
	private transient DirInfo	dirInfo;
	private boolean				isDir;

	/**
	 * Create a DirTreeNode from a DirInfo directory definition.
	 * @param dir the DirInfo for the directory to be made into a new tree node.
	 */
	public DirTreeNode(DirInfo dir)
	{
		super(dir.getName());
		isDir = true;
		loadedChildren = false;
		dirInfo = dir;
		setAllowsChildren(true);
	}

	/**
	 * Create a DirTreeNode from a File (which must not be a directory).
	 * @param file the File for the file to be made into a new tree node.
	 */
	public DirTreeNode(File file)
	{
		super(file.getName());
		assert !file.isDirectory();
		isDir = false;
		loadedChildren = true;
		dirInfo = null;
		setAllowsChildren(false);
	}

	/**
	 * Returns true if this node allows children. Whether the node allows
	 * children depends on how it was created.
	 * @return true if this node allows children, false otherwise
	 */
	@Override
	public boolean isLeaf()
	{
		return !getAllowsChildren();
	}

	/**
	 * Returns the number of child nodes.
	 * @return the number of child nodes.
	 */
	@Override
	public int getChildCount()
	{
		try
		{
			if (!loadedChildren)
			{
				loadChildren();
			}
			return super.getChildCount();
		}
		catch (IOException ioe)
		{
			return 0;
		}
	}

	/**
	 * Loads the directories contents.
	 * @throws IOException if the directory cannot be read, or contains an
	 * unexpected file type.
	 */
	private void loadChildren() throws IOException
	{
		loadedChildren = true;
		if (!isDir)
		{
			throw new IOException(dirInfo.getPath());
		}
		for (DirInfo subdir : dirInfo.listDirs())
		{
			DirTreeNode dtn = new DirTreeNode(subdir);
			add(dtn);
		}
		for (File file : dirInfo.listFiles())
		{
			DirTreeNode dtn = new DirTreeNode(file);
			add(dtn);
		}
	}

	/**
	 * Retrieve a member of this directory, by integer index.
	 * Subclassed to load the children, if necessary.
	 * @param index the index of the directory member to return, counting from 0.
	 * @return a tree node (file or directory) that is the index'th member of
	 * the directory.
	 */
	@Override
	public TreeNode getChildAt(int index)
	{
		try
		{
			if (!loadedChildren)
			{
				loadChildren();
			}
			return super.getChildAt(index);
		}
		catch (IOException ioe)
		{
			ArrayIndexOutOfBoundsException e = new ArrayIndexOutOfBoundsException(index);
			e.initCause(ioe);
			throw e;
		}
	}

	/**
	 * Return an enumeration of all the members of this directory.
	 * Subclassed to load the children, if necessary.
	 * @return an enumeration of the directory members.
	 */
	@Override
	public Enumeration<?> children()
	{
		try
		{
			if (!loadedChildren)
			{
				loadChildren();
			}
		}
		catch (IOException ioe)
		{
			// Do nothing - allow superclass to return empty enumeration
		}
		return super.children();
	}
}
