/**
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Apr 8, 2003
 * Time: 12:17:37 PM
 */
package zz.utils.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import zz.utils.ui.OptionListener;

/**
 * This panel presents a Font chooser along with ok and cancel buttons.
 * Use {@link #addOptionListener} to add a listener for these buttons.
 */
public class FontChooserPanel extends AbstractOptionPanel
{
	private JFontChooser itsFontChooser;

	public FontChooserPanel ()
	{
	}

	protected JComponent createComponent ()
	{
		itsFontChooser = new JFontChooser();
		return itsFontChooser;
	}

	/**
	 * Sets up the panel with the specified Font.
	 */
	public void setSelectedFont (Font aFont)
	{
		Font theInitialFont = null; //TODO: see if we put a default value
		if (aFont != null) theInitialFont = aFont;
		itsFontChooser.setSelectedFont(theInitialFont);
	}

	public Font getSelectedFont ()
	{
		return itsFontChooser.getSelectedFont();
	}


	private static List itsAvailableChoosers = new LinkedList ();

	public static FontChooserPanel allocateFontChooserPanel ()
	{
		if (itsAvailableChoosers.size() > 0) return (FontChooserPanel) itsAvailableChoosers.remove(0);
		else return new FontChooserPanel();
	}

	public static void releaseFontChooserPanel (FontChooserPanel aFontChooserPanel)
	{
		assert aFontChooserPanel != null && ! itsAvailableChoosers.contains(aFontChooserPanel);
		itsAvailableChoosers.add (aFontChooserPanel);
	}

}
