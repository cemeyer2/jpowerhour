package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.gui.JPowerHourGUI;
import net.charliemeyer.jpowerhour.gui.util.SongListRenderer;

public class SongListPanel extends JPanel
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
}
