package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.gui.JPowerHourGUI;
import net.charliemeyer.jpowerhour.gui.util.SongListRenderer;

public class SongListPanel extends JPanel implements MouseListener
{
	JList list;
	DefaultListModel listModel;
	
	public SongListPanel(JPowerHourGUI parent)
	{
		super();
		setLayout(new BorderLayout());
		
		listModel = new DefaultListModel();
		

		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new SongListRenderer());
		list.addMouseListener(this);
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void addPowerHourSong(JPowerHourSong song)
	{
		listModel.addElement(song);
	}
	
	public int getPowerHourSongCount()
	{
		return listModel.size();
	}
	
	public JPowerHourSong getPowerHourSong(int index)
	{
		int size = listModel.size();
		if(index >= 0 && index < size)
			return (JPowerHourSong)listModel.get(index);
		else
			return null;
	}
	
	public void removePowerHourSong()
	{
		if(list.getSelectedIndex() != -1)
			listModel.remove(list.getSelectedIndex());
	}
	
	public void up()
	{
		int index = list.getSelectedIndex();
		if(index > 0)
		{
			Object a = listModel.get(index-1);
			Object b = listModel.get(index);
			listModel.set(index-1, b);
			listModel.set(index, a);
			list.setSelectedIndex(index-1);
		}
	}
	
	public void down()
	{
		int index = list.getSelectedIndex();
		if(index >= 0 && index < listModel.size()-1)
		{
			Object a = listModel.get(index+1);
			Object b = listModel.get(index);
			listModel.set(index, a);
			listModel.set(index+1, b);
			list.setSelectedIndex(index+1);
		}
	}
	
	public void clearSongs()
	{
		listModel.clear();
	}

	@Override
	public void mouseClicked(MouseEvent event) 
	{
		int count = event.getClickCount();
		if(count >= 2)
		{
			int index = list.locationToIndex(event.getPoint());
			if(index != -1)
			{
				JPowerHourSong song = (JPowerHourSong) listModel.get(index);
				EditSongPanel panel = new EditSongPanel(song);
				panel.show();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
