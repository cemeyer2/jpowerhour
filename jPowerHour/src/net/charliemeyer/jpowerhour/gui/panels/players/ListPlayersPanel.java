package net.charliemeyer.jpowerhour.gui.panels.players;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import net.charliemeyer.jpowerhour.gui.util.JButtonIconizer;
import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;
import net.charliemeyer.jpowerhour.player.JPowerHourPlayer;

public class ListPlayersPanel extends JPanel implements ActionListener, WindowListener, MouseListener
{
	JList list;
	JButton add,remove, close;
	DefaultListModel listModel;
	JPowerHourFrame frame;
	
	public ListPlayersPanel()
	{
		super();
		setLayout(new BorderLayout());
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(this);
		
		add = new JButton();
		remove = new JButton();
		close = new JButton();
		
		JButtonIconizer.iconize(add, "add.png", "Add Player", "Add Player", true);
		JButtonIconizer.iconize(remove, "remove.png", "Remove", "Remove Player", true);
		JButtonIconizer.iconize(close, "cancel.png", "Close", "Close", true);
		
		add.addActionListener(this);
		remove.addActionListener(this);
		close.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		panel.add(add);
		panel.add(remove);
		panel.add(close);
		
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
	
	public void addPlayer(JPowerHourPlayer player)
	{
		listModel.addElement(player);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		if(add.equals(source))
		{
			handleAddAction();
		}
		else if(remove.equals(source))
		{
			handleRemoveAction();
		}
		else if(close.equals(source))
		{
			handleCloseAction();
		}
	}

	private void handleCloseAction() 
	{
		frame.setVisible(false);
	}

	private void handleRemoveAction() 
	{
		this.removePlayer();
	}

	private void handleAddAction() 
	{
		new AddEditPlayerPanel(this).show();
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

	@Override
	public void mouseClicked(MouseEvent event) 
	{
		int count = event.getClickCount();
		if(count > 1)
		{
			int index = list.locationToIndex(event.getPoint());
			if(index != -1)
			{
				new AddEditPlayerPanel(this, (JPowerHourPlayer)listModel.get(index)).show();
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
