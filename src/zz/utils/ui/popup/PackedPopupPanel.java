/*
 * Created on 10-nov-2004
 */
package zz.utils.ui.popup;

import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JPanel;

import zz.utils.ui.StackLayout;
import zz.utils.ui.popup.PopupUtils;

/**
 * A panel that can be used as popup content that resizes the popup
 * as its contents change. 
 * <p>
 * This class doesn't create or show the popup; it is the reponsibility of the user
 * to create the popup.
 * @author gpothier
 */
public class PackedPopupPanel extends JPanel
{
	public PackedPopupPanel()
	{
		setLayout(new StackLayout());
	}
	
	/**
	 * Closes the popup that contains this panel.
	 */
	protected void close()
	{
		PopupUtils.hidePopup(this);
	}

	/**
	 * Changes the current content and resizes the popup.
	 */
	public void setContent (JComponent aComponent)
	{
		removeAll();
		add (aComponent);
		PopupUtils.revalidatePopup(this);
	}
}
