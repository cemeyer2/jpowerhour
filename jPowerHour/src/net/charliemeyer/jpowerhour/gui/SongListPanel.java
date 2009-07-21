package net.charliemeyer.jpowerhour.gui;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SongListPanel extends JPanel
{
	JList list;
	
	public SongListPanel()
	{
		super();
		setLayout(new BorderLayout());
		
		list = new JList();
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		add(scrollPane, BorderLayout.CENTER);
	}
}
