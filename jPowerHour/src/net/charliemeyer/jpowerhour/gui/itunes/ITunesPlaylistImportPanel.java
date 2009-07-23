package net.charliemeyer.jpowerhour.gui.itunes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import ncrossley.itunes.Playlist;
import ncrossley.itunes.Track;
import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.gui.JPowerHourGUI;
import net.charliemeyer.jpowerhour.gui.util.JButtonIconizer;
import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;
import net.charliemeyer.jpowerhour.gui.util.PlaylistListRenderer;
import net.charliemeyer.jpowerhour.itunes.ITunesPlayListParser;

public class ITunesPlaylistImportPanel extends JPanel implements ActionListener
{
	JList list;
	DefaultListModel model;
	JButton importPlaylist, cancel;
	JPowerHourFrame frame;
	JPowerHourGUI parent;
	
	public ITunesPlaylistImportPanel(JPowerHourGUI parent)
	{
		this.parent = parent;
		
		setLayout(new BorderLayout());
		
		model = new DefaultListModel();
		list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new PlaylistListRenderer());
		
		importPlaylist = new JButton();
		cancel = new JButton();
		
		importPlaylist.addActionListener(this);
		cancel.addActionListener(this);
		
		JButtonIconizer.iconize(importPlaylist, "import.png", "Import Playlist", "Import Playlist", true);
		JButtonIconizer.iconize(cancel, "cancel.png", "Cancel", "Cancel", true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(importPlaylist);
		panel.add(cancel);
		
		add(list,BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
		
		frame = new JPowerHourFrame("Import iTunes Playlist");
		
		Dimension dim = new Dimension(250,500);
		setSize(dim);
		frame.setSize(dim);
		
		frame.setContentPane(this);
		
		loadPlaylists();
		
	}
	
	private void loadPlaylists()
	{
		Collection<Playlist> playlists = ITunesPlayListParser.getITunesPlaylists();
		for(Playlist playlist : playlists)
		{
			model.addElement(playlist);
		}
	}
	
	public void show()
	{
		frame.setVisible(true);
	}
	

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		if(importPlaylist.equals(source))
		{
			handleImportPlaylistOption();
		}
		else if(cancel.equals(source))
		{
			handleCancelAction();
		}
	}

	private void handleImportPlaylistOption() 
	{
		int index = list.getSelectedIndex();
		if(index != -1)
		{
			Playlist playlist = (Playlist)this.model.get(index);
			parent.getSongListPanel().clearSongs();
			for(Track track : playlist.getTracks())
			{
				try
				{
					File f = track.getFile();
					if(f.exists())
					{
						JPowerHourSong song = new JPowerHourSong(f);
						parent.getSongListPanel().addPowerHourSong(song);
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Error: file "+f.getName()+" does not exist", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error adding "+track.getArtist()+" - "+track.getName()+" to the playlist", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			frame.setVisible(false);
			frame.dispose();
		}
	}

	private void handleCancelAction() 
	{
		frame.setVisible(false);
		frame.dispose();
	}
}
