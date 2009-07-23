package net.charliemeyer.jpowerhour.gui.util;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class AlcoholPercentageListRenderer extends JLabel implements ListCellRenderer 
{

	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);



	@Override
	public Component getListCellRendererComponent(JList list, Object obj,
			int pos, boolean isSelected, boolean cellHasFocus) 
	{

		double val = ((Double)obj).doubleValue();
		
		
		String text = "2.7% - Light Beer";
		if(val == 0.035)
		{
			text = "3.5% - Regular Beer";
		}
		else if(val == 0.049)
		{
			text = "4.9% - Heavy Beer";
		}
		

		setText(text);

		if (isSelected) 
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} 
		else 
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		Border border = null;
		if (cellHasFocus) {
			if (isSelected) {
				border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
			}
			if (border == null) {
				border = UIManager.getBorder("List.focusCellHighlightBorder");
			}
		} else {
			border = getNoFocusBorder();
		}
		setBorder(border);

		return this;
	}

	private static Border getNoFocusBorder() 
	{
		if (System.getSecurityManager() != null) 
		{
			return SAFE_NO_FOCUS_BORDER;
		} 
		else 
		{
			return noFocusBorder;
		}
	}



}
