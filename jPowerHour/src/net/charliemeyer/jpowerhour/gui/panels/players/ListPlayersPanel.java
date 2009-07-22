package net.charliemeyer.jpowerhour.gui.panels.players;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;
import net.charliemeyer.jpowerhour.player.JPowerHourPlayer;

public class ListPlayersPanel extends JPanel implements ActionListener, WindowListener
{
	JList list;
	JButton add,remove;
	DefaultListModel listModel;
	JPowerHourFrame frame;
	
	public ListPlayersPanel()
	{
		super();
		setLayout(new BorderLayout());
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		add = new JButton();
		remove = new JButton();
		
		try
		{
			add.setIcon(new ImageIcon(ImageIO.read(new File("images/add.png"))));
			remove.setIcon(new ImageIcon(ImageIO.read(new File("images/remove.png"))));
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			add.setText("Add Player");
			remove.setText("Remove Player");
		}
		add.addActionListener(this);
		remove.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(add);
		panel.add(remove);
		
		add(list, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
		
		frame = new JPowerHourFrame("Edit Players");
		frame.addWindowListener(this);
		
		Dimension dim = new Dimension(300,600);
		setSize(dim);
		frame.setSize(dim);
		frame.setContentPane(this);
	}
	
	public void show()
	{
		frame.setVisible(true);
	}
	
	public void hide()
	{
		frame.setVisible(false);
	}
	
	public int getPlayerCount()
	{
		return listModel.size();
	}
	
	public JPowerHourPlayer getPlayer(int index)
	{
		return (JPowerHourPlayer)listModel.get(index);
	}
	
	public void removePlayer()
	{
		int selected = list.getSelectedIndex();
		if(selected != -1)
		{
			listModel.remove(selected);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.hide();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
