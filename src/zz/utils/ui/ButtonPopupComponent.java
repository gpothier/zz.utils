/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 26 mars 2003
 * Time: 02:36:54
 */
package zz.utils.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A popup componenet that automatically handles click on a button to toggle the popup.
 */
public class ButtonPopupComponent extends PopupComponent implements ActionListener
{
	public ButtonPopupComponent (JComponent popup, JButton aButton)
	{
		super (popup, aButton);
		aButton.addActionListener(this);
	}

	public ButtonPopupComponent (JButton aButton)
	{
		super (null, aButton);
		aButton.addActionListener (this);
	}

	public void actionPerformed (ActionEvent e)
	{
		togglePopup();
	}
}
