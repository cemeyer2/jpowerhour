package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.charliemeyer.jpowerhour.PowerHourSong;
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
	
	public void addPowerHourSong(PowerHourSong song)
	{
		listModel.addElement(song);
	}
	
	public int getPowerHourSongCount()
	{
		return listModel.size();
	}
	
	public PowerHourSong getPowerHourSong(int index)
	{
		return (PowerHourSong)listModel.get(index);
	}
	
	public void removePowerHourSong()
	{
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
		if(index >= 0 && listModel.size() > 1 && index < listModel.size())
		{
			Object a = listModel.get(index+1);
			Object b = listModel.get(index);
			listModel.set(index, a);
			listModel.set(index+1, b);
			list.setSelectedIndex(index+1);
		}
	}
}
