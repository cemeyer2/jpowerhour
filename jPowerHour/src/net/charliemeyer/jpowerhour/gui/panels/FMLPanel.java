package net.charliemeyer.jpowerhour.gui.panels;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.BreakIterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.charliemeyer.jpowerhour.gui.util.JPowerHourFrame;
import net.charliemeyer.jpowerhour.util.FMLRandomPost;

public class FMLPanel extends JPanel implements WindowListener
{
	JLabel label,authorlabel;
	JPowerHourFrame frame;
	FMLThread thread;
	boolean showing = false;
	
	public FMLPanel()
	{
		super();
		setLayout(new BorderLayout());
		label = new JLabel();
		authorlabel = new JLabel();
		add(label,BorderLayout.CENTER);
		add(authorlabel,BorderLayout.NORTH);
		
		frame = new JPowerHourFrame("FML");
		
		Dimension d = new Dimension(400,200);
		setSize(d);
		frame.setSize(d);
		frame.setContentPane(this);
		frame.addWindowListener(this);
	}
	
	public class FMLThread implements Runnable
	{
		boolean shouldrun = true;
		
		public void run() 
		{
			while(shouldrun){
				FMLRandomPost post = new FMLRandomPost();
				String text = post.getText();
				wrapLabelText(label, text);
				authorlabel.setText(post.getAuthor()+" says FML:");
				try
				{
					Thread.sleep(20000);
				}
				catch(InterruptedException ie)
				{
					ie.printStackTrace();
				}
			}
		}
		
		public void setShouldRun(boolean run)
		{
			shouldrun = run;
		}
	}
	
	private void wrapLabelText(JLabel label, String text) {
		FontMetrics fm = label.getFontMetrics(label.getFont());
		Container container = label.getParent();
		int containerWidth = container.getWidth();

		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);

		StringBuffer trial = new StringBuffer();
		StringBuffer real = new StringBuffer("<html>");

		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE;
			start = end, end = boundary.next()) {
			String word = text.substring(start,end);
			trial.append(word);
			int trialWidth = SwingUtilities.computeStringWidth(fm,
				trial.toString());
			if (trialWidth > containerWidth) {
				trial = new StringBuffer(word);
				real.append("<br>");
			}
			real.append(word);
		}

		real.append("</html>");

		label.setText(real.toString());
	}
	
	public void show()
	{
		thread = new FMLThread();
		Thread runner = new Thread(thread,"FML Post Getter");
		runner.start();
		frame.setVisible(true);
		showing = true;
	}
	
	public void hide()
	{
		thread.setShouldRun(false);
		frame.setVisible(false);
		showing = false;
	}
	
	public boolean isShowing()
	{
		return showing;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		new FMLPanel().show();
	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent arg0) {
		hide();		
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
