/*
 * Created on Apr 16, 2006
 */
package zz.utils.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class UIUtils
{
	public static final Insets NULL_INSETS = new Insets (0, 0, 0, 0);

	/**
	 * Returns the frame that contains this component
	 */
	public static Frame getFrame (Component aComponent)
	{
		Container theCurrentContainer = aComponent.getParent ();
		while (theCurrentContainer != null)
		{
			if (theCurrentContainer instanceof Frame) return (Frame) theCurrentContainer;
			theCurrentContainer = theCurrentContainer.getParent ();
		}
		return null;
	}

	/**
	 * Returns the swing frame that contains this component
	 */
	public static JFrame getJFrame (JComponent aComponent)
	{
		Container theCurrentContainer = aComponent.getParent ();
		while (theCurrentContainer != null)
		{
			if (theCurrentContainer instanceof JFrame) return (JFrame) theCurrentContainer;
			theCurrentContainer = theCurrentContainer.getParent ();
		}
		return null;
	}

	/**
	 * Returns the JViewPort that contains this component
	 */
	public static JViewport getViewport (Component aComponent)
	{
		Container theCurrentContainer = aComponent.getParent ();
		while (theCurrentContainer != null)
		{
			if (theCurrentContainer instanceof JViewport) return (JViewport) theCurrentContainer;
			theCurrentContainer = theCurrentContainer.getParent ();
		}
		return null;
	}


	/**
	 * Resizes the specified icon.
	 * @return If rescaling is needed, returns a new resized icon. If the requested size is
	 * the size of the icon, anIcon is returned.
	 */
	public static ImageIcon resizeIcon (ImageIcon anIcon, int aWidth, int anHeight)
	{
		int theWidth = anIcon.getIconWidth ();
		int theHeight = anIcon.getIconHeight ();

		if (theWidth == aWidth && theHeight == anHeight) return anIcon;

		Image theImage = anIcon.getImage ();
		return new ImageIcon (theImage.getScaledInstance (aWidth, anHeight, Image.SCALE_SMOOTH));
	}

	private static Cursor itsEmptyCursor;

	static
	{
		BufferedImage theImage = new BufferedImage (1, 1, BufferedImage.TYPE_INT_ARGB);
		itsEmptyCursor = Toolkit.getDefaultToolkit ().createCustomCursor (theImage, new Point (0, 0), "empty");
	}

	public static Cursor getEmptyCursor ()
	{
		return itsEmptyCursor;
	}

	/**
	 * Returns the bounds of the default screen
	 */
	public static Rectangle getScreenBounds ()
	{
		GraphicsEnvironment theGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment ();
		GraphicsDevice theScreenDevice = theGraphicsEnvironment.getDefaultScreenDevice ();
		GraphicsConfiguration theDefaultConfiguration = theScreenDevice.getDefaultConfiguration ();
		return theDefaultConfiguration.getBounds ();
	}

	/**
	 * Returns the maximum window bounds (taking taskbar etc into account)
	 */
	public static Rectangle getMaximumWindowBounds ()
	{
		GraphicsEnvironment theGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment ();
		return theGraphicsEnvironment.getMaximumWindowBounds ();
	}

	public static final int MINIMUM_SIZE = 1;
	public static final int PREFERRED_SIZE = 2;
	public static final int MAXIMUM_SIZE = 3;

	/**
	 * Returns one of getPreferredSize, getMaximumSize or getMinimumSize of the specified
	 * component according to the value of aType.
	 * @param aComponent The component to get the size from
	 * @param aType Which size to get (must be one of MINIMUM_SIZE, PREFERRED_SIZE and MAXIMUM_SIZE)
	 */
	public static Dimension getASize (Component aComponent, int aType)
	{
		switch (aType)
		{
			case MINIMUM_SIZE:
				return aComponent.getMinimumSize ();

			case PREFERRED_SIZE:
				return aComponent.getPreferredSize ();

			case MAXIMUM_SIZE:
				return aComponent.getMaximumSize ();
		}

		throw new IllegalArgumentException ("aType must be one of MINIMUM_SIZE, PREFERRED_SIZE and MAXIMUM_SIZE");
	}

	/**
	 * Returns a color similar but lighter than the specified color.
	 * @param aCoefficient If 0, the returned color will be totally white.
	 * If 1, the returned color will be the same as the specified color.
	 */
	public static Color getLighterColor (Color aColor, float aCoefficient)
	{
		Color theLighterColor = null;
		if (aColor != null)
		{
			int r = (int) (255 - (255 - aColor.getRed ()) * aCoefficient);
			int g = (int) (255 - (255 - aColor.getGreen ()) * aCoefficient);
			int b = (int) (255 - (255 - aColor.getBlue ()) * aCoefficient);
			theLighterColor = new Color (r, g, b);
		}
		return theLighterColor;
	}

	public static Color getLighterColor (Color aColor)
	{
		return getLighterColor (aColor, 0.3f);
	}

	public static void simulateKeyTyped (Component target, char c)
	{
		EventQueue q = target.getToolkit ().getSystemEventQueue ();
		q.postEvent (new KeyEvent (target, KeyEvent.KEY_TYPED,
		        System.currentTimeMillis (), 0, KeyEvent.VK_UNDEFINED, c));
	}

	/**
	 * I'd rather not say what this method is for...
	 * See EasyJTable
	 */
	public static void simulateDeadKey (final Component target)
	{
		SwingUtilities.invokeLater (new Runnable ()
		{
			public void run ()
			{
				EventQueue q = target.getToolkit ().getSystemEventQueue ();

				q.postEvent (new KeyEvent (target, KeyEvent.KEY_PRESSED,
				        System.currentTimeMillis (), 0, KeyEvent.VK_ALT, '\u0000'));

				q.postEvent (new KeyEvent (target, KeyEvent.KEY_RELEASED,
				        System.currentTimeMillis (), 0, KeyEvent.VK_ALT, '\u0000'));
			}
		});
	}

	public static final Composite ALPHA_04 = AlphaComposite.getInstance (AlphaComposite.SRC_OVER, 0.4f);
	public static final Composite ALPHA_06 = AlphaComposite.getInstance (AlphaComposite.SRC_OVER, 0.6f);
	public static final Composite ALPHA_02 = AlphaComposite.getInstance (AlphaComposite.SRC_OVER, 0.2f);
	public static final Composite ALPHA_OPAQUE = AlphaComposite.getInstance (AlphaComposite.SRC);

	/**
	 * Returns the position of aRelative relative to aOrigin.
	 */
	public static Point getRelativePosition (Component aRelative, Component aOrigin)
	{
		Point theRelativePoint = aRelative.getLocationOnScreen ();
		Point theOriginPoint = aOrigin.getLocationOnScreen ();

		return new Point (theRelativePoint.x - theOriginPoint.x, theRelativePoint.y - theOriginPoint.y);
	}

	/**
	 * Mixes two colors
	 */
	public static Color averageColor (Color aColor1, float aWeight1, Color aColor2, float aWeight2)
	{
		if (aColor1 == null) return aColor2;
		if (aColor2 == null) return aColor1;

		int r = (int) ((aColor1.getRed () * aWeight1 + aColor2.getRed () * aWeight2) / (aWeight1 + aWeight2));
		int g = (int) ((aColor1.getGreen () * aWeight1 + aColor2.getGreen () * aWeight2) / (aWeight1 + aWeight2));
		int b = (int) ((aColor1.getBlue () * aWeight1 + aColor2.getBlue () * aWeight2) / (aWeight1 + aWeight2));

		return new Color (r, g, b);
	}


	private static final Rectangle AUTOSCROLL_RECTANGLE = new Rectangle(0, 0, 1, 1);
	/**
	 * Listener to add to a component which was activated for autoscrolling.
	 */
	public static final MouseMotionListener AUTOSCROLL_DRAG_LISTENER = new MouseMotionAdapter ()
	{
		public void mouseDragged (MouseEvent e)
		{
			AUTOSCROLL_RECTANGLE.setLocation(e.getX (), e.getY ());
			((JComponent) e.getSource ()).scrollRectToVisible (AUTOSCROLL_RECTANGLE);
		}
	};

	/**
	 * Returns whether the container or one of its children contains a component.
	 */ 
	public static boolean containsComponent (Container aContainer, Component aComponent)
	{
		Component[] theArray = aContainer.getComponents();
		for (int i = 0; i < theArray.length; i++)
		{
			Component theComponent = theArray[i];
			if (theComponent == aComponent) return true;
			else if (theComponent instanceof Container)
			{
				Container theContainer = (Container) theComponent;
				if (containsComponent(theContainer, aComponent)) return true;
			}
		}
		return false;
	}

	public static final Cursor BUSY_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

	/**
	 * Shows a busy cursor, and restores the previous cursor only when the event dispatchin
	 * thread finishes its work.
	 * @param aComponent We need a component to perform this operation. Can be any component of the frame.
	 */
	public static void showBusyCursor (final Component aComponent)
	{
		final Cursor thePreviousCursor = aComponent.getCursor();
		aComponent.setCursor(BUSY_CURSOR);
		SwingUtilities.invokeLater(new Runnable ()
		{
			public void run ()
			{
				aComponent.setCursor(thePreviousCursor);
			}
		});
	}


}
