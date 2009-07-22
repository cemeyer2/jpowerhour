package net.charliemeyer.jpowerhour.gui.util;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class SongListRenderer extends JLabel implements ListCellRenderer 
{

	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);



	@Override
	public Component getListCellRendererComponent(JList list, Object obj,
			int pos, boolean isSelected, boolean cellHasFocus) 
	{

		String text = (pos+1)+". "+obj.toString();

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
