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
 * This panel presents a color chooser along with ok and cancel buttons.
 * Use {@link #addOptionListener} to add a listener for these buttons.
 */
public class ColorChooserPanel extends AbstractOptionPanel
{
	private JColorChooser itsColorChooser;
	private boolean itsTransparent = false;

	public ColorChooserPanel ()
	{
	}

	protected JComponent createComponent ()
	{
		JButton theTransparentButton = new JButton("Transparent");
		theTransparentButton.addActionListener(new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				setSelectedColor (null);
				ok ();
			}
		});
		addToButtonsPanel(theTransparentButton);

		itsColorChooser = new JColorChooser();
		return itsColorChooser;
	}

	public void addNotify ()
	{
		super.addNotify ();
		itsTransparent = false;
	}

	/**
	 * Sets up the panel with the specified color.
	 */
	public void setSelectedColor (Color aColor)
	{
		if (aColor == null) itsTransparent = true;
		else
		{
			itsTransparent = false;
			Color theInitialColor = Color.white;
			if (aColor != null) theInitialColor = aColor;
			itsColorChooser.setColor(theInitialColor);
		}
	}

	private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	public Color getSelectedColor ()
	{
		if (itsTransparent) return TRANSPARENT;
		else return itsColorChooser.getColor();
	}

	private static List itsAvailableChoosers = new LinkedList ();

	public static ColorChooserPanel allocateColorChooserPanel ()
	{
		if (itsAvailableChoosers.size() > 0) return (ColorChooserPanel) itsAvailableChoosers.remove(0);
		else return new ColorChooserPanel();
	}

	public static void releaseColorChooserPanel (ColorChooserPanel aColorChooserPanel)
	{
		assert aColorChooserPanel != null && ! itsAvailableChoosers.contains(aColorChooserPanel);
		itsAvailableChoosers.add (aColorChooserPanel);
	}

}
