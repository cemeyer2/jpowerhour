package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import net.charliemeyer.jpowerhour.JPowerHourInterlude;
import net.charliemeyer.jpowerhour.JPowerHourListener;
import net.charliemeyer.jpowerhour.JPowerHourSong;
import net.charliemeyer.jpowerhour.gui.util.JButtonIconizer;
import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;
import net.charliemeyer.jpowerhour.gui.util.MusicFilter;

public class ManageInterludesPanel extends JPanel implements WindowListener, ActionListener, JPowerHourListener
{
	DefaultListModel model;
	JList list;
	JButton add, remove, play, close, reload;
	JPowerHourFrame frame;

	public ManageInterludesPanel()
	{
		super();
		setLayout(new BorderLayout());
		
		model = new DefaultListModel();
		list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		add = new JButton();
		remove = new JButton();
		close = new JButton();
		play = new JButton();
		reload = new JButton();
		
		JButtonIconizer.iconize(add, "add.png", "Add Interlude", "Add Interlude", true);
		JButtonIconizer.iconize(remove, "remove.png", "Remove Interlude", "Remove Interlude", true);
		JButtonIconizer.iconize(close, "cancel.png", "Close", "Close", true);
		JButtonIconizer.iconize(play, "play.png", "Play", "Play", true);
		JButtonIconizer.iconize(reload, "reload.png", "Reload", "Reload Default Interludes", true);
		
		add.addActionListener(this);
		remove.addActionListener(this);
		close.addActionListener(this);
		play.addActionListener(this);
		reload.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,5));
		buttonPanel.add(add);
		buttonPanel.add(remove);
		buttonPanel.add(play);
		buttonPanel.add(reload);
		buttonPanel.add(close);
		
		add(list, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		Dimension dim = new Dimension(250,500);
		setSize(dim);
		
		frame = new JPowerHourFrame("Manage Interludes");
		
		frame.setContentPane(this);
		frame.setSize(dim);
		frame.addWindowListener(this);
		
		addDefaultInterludes();
	}
	
	public void show()
	{
		frame.setVisible(true);
	}
	
	public void hide()
	{
		frame.setVisible(false);
	}
	
	private void addDefaultInterludes()
	{
		try
		{
			for(int i = 0; i < JPowerHourInterlude.getDefaultInterludeCount(); i++)
			{
				model.addElement(new JPowerHourInterlude(i));
			}
		}
		catch(BasicPlayerException bpe)
		{
			bpe.printStackTrace();
		}
	}
	
	public int getInterludeCount()
	{
		return model.size();
	}
	
	public JPowerHourInterlude getInterlude(int index)
	{
		return (JPowerHourInterlude) model.get(index);
	}
	
	public void clearInterludes()
	{
		model.clear();
	}
	
	public void addInterlude(JPowerHourInterlude interlude)
	{
		model.addElement(interlude);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		new ManageInterludesPanel().show();
	}


	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void windowClosing(WindowEvent event) 
	{
		hide();
	}


	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

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
		else if(play.equals(source))
		{
			handlePlayAction();
		}
		else if(close.equals(source))
		{
			handleCloseAction();
		}
		else if(reload.equals(source))
		{
			handleReloadAction();
		}
	}

	private void handleReloadAction() 
	{
		addDefaultInterludes();
	}

	private void handleAddAction() 
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new MusicFilter());
		
		int retval = chooser.showOpenDialog(this);
		if(retval == JFileChooser.APPROVE_OPTION)
		{
			File f = chooser.getSelectedFile();
			try 
			{
				JPowerHourInterlude i = new JPowerHourInterlude(f);
				if(i.getArtist().equals("") && i.getTitle().equals(""))
				{
					i.setArtist(i.getSongFile().getName());
				}
				model.addElement(i);
			}
			catch (BasicPlayerException e) 
			{
				e.printStackTrace();
			}
			
		}
	}

	private void handleRemoveAction() {
		int selected = list.getSelectedIndex();
		if(selected != -1)
		{
			model.remove(selected);
		}
	}

	private void handlePlayAction() {
		int selected = list.getSelectedIndex();
		if(selected != -1)
		{
			JPowerHourInterlude i = (JPowerHourInterlude) model.get(selected);
			try 
			{
				i.playSong();
			} 
			catch (BasicPlayerException e) 
			{
				String message = "Could not get the audio device. Please try closing all other programs that may be using the audio device";
				JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	private void handleCloseAction() {
		this.hide();
	}

	public void powerHourFinished() 
	{
		play.setEnabled(true);
		add.setEnabled(true);
		remove.setEnabled(true);
		reload.setEnabled(true);
	}

	public void powerHourPaused() 
	{
		
	}

	public void powerHourResumed()
	{
		
	}

	public void powerHourStarted() 
	{
		play.setEnabled(false);
		add.setEnabled(false);
		remove.setEnabled(false);
		reload.setEnabled(false);
	}

	public void songChange(JPowerHourSong currentlyPlaying,
			int currentlyPlayingNumber) {
		// TODO Auto-generated method stub
		
	}
}
