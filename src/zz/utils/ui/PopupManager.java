/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 27 févr. 2003
 * Time: 15:02:03
 */
package zz.utils.ui;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zz.utils.Utils;

/**
 * Permits to display popup windows.
 * The window will be displayed next to a specified trigger component,
 * respecting when possible a specified preferred direction (top, bottom, left, right).
 * <p>
 */
public class PopupManager
{
	/**
	 * Convenience method, defaults orientation to Bottom and auto hide to true.
	 */
	public static Popup createPopup (JFrame aOwner,
									 JComponent aPopupTriggerComponent, JComponent aPopupComponent)
	{
		return createPopup(aOwner, aPopupTriggerComponent, aPopupComponent, JLabel.BOTTOM, true);
	}


	/**
	 * Convenience method, calls {@link #createPopup(javax.swing.JFrame, javax.swing.JRootPane, javax.swing.JComponent, javax.swing.JComponent, int, boolean)
	 * with the root pane set to the frame's root pane.
	 */
	public static Popup createPopup (JFrame aOwner,
								   JComponent aPopupTriggerComponent, JComponent aPopupComponent,
								   int aPreferredDirection, boolean aAutoHide)
	{

		JRootPane theRootPane = aOwner != null ? aOwner.getRootPane() : null;
		return createPopup(aOwner, theRootPane, aPopupTriggerComponent, aPopupComponent, aPreferredDirection, aAutoHide);
	}

	/**
	 * Creates a popup object.
	 *
	 * @param aPopupTriggerComponent The component that triggered the popup.
	 * The returned bounds are calculated so that the popup component appears next to
	 * the popup trigger component
	 * @param aPreferredDirection Specifies in which direction the popup should open if
	 * there is no space constraint.
	 * One of JLabel.TOP, JLabel.BOTTOM, JLabel.LEFT, JLabel.RIGHT
	 *
	 * @param aOwner The frame that will own the window containing the popup. If it
	 * is null, the {@link Popup} will try to determine it at show time, using the trigger component.
	 * @param aRootPane The owner root pane. This cannot be null.
	 * @param aPopupComponent The content of the popup window.
	 * @param aAutoHide Whether to automatically hide the popup when the user clicks out of the popup
	 * window
	 * @return A {@link Popup} object which can be shown {@link Popup#show} or hidden {@link Popup#hide}.
	 */
	public static Popup createPopup (JFrame aOwner, JRootPane aRootPane,
								   JComponent aPopupTriggerComponent, JComponent aPopupComponent,
								   int aPreferredDirection, boolean aAutoHide)
	{
		Popup thePopup = new Popup (aOwner, aRootPane, aPopupComponent, aPopupTriggerComponent);
		thePopup.setPreferredDirection(aPreferredDirection);
		thePopup.setAutoHide(aAutoHide);

		return thePopup;
	}

	/**
	 * A method that computes the bounds (in screen coordinates) of a popup component
	 * according to the size & position of a popup trigger component.
	 * @param aPopupTriggerComponent The component that triggered the popup.
	 * The returned bounds are calculated so that the popup component appears next to
	 * the popup trigger component
	 * @param aPreferredDirection Specifies in which direction the popup should open if
	 * there is no space constraint.
	 * One of JLabel.TOP, JLabel.BOTTOM, JLabel.LEFT, JLabel.RIGHT
	 * @return Suggested screen coordinates for the popup
	 */
	public static Rectangle computePopupBounds (JComponent aPopupTriggerComponent, int aPreferredDirection, Dimension aPopupDimension)
	{
		return computePopupBounds(aPopupTriggerComponent.getRootPane(), aPopupTriggerComponent, aPreferredDirection, aPopupDimension);
	}

	public static Rectangle computePopupBounds (JFrame aOwner, JComponent aPopupTriggerComponent, int aPreferredDirection, Dimension aPopupDimension)
	{
		return computePopupBounds(aOwner.getRootPane(), aPopupTriggerComponent, aPreferredDirection, aPopupDimension);
	}

	public static Rectangle computePopupBounds (JRootPane aRootPane, JComponent aPopupTriggerComponent, int aPreferredDirection, Dimension aPopupDimension)
	{
		JRootPane rp = aRootPane;
		JLayeredPane lp = rp.getLayeredPane ();

		Point lpls = lp.getLocationOnScreen();
		Point ls = aPopupTriggerComponent.getLocationOnScreen();
		int myW = aPopupTriggerComponent.getWidth ();
		int myH = aPopupTriggerComponent.getHeight ();
		Rectangle theScreenBounds = Utils.getMaximumWindowBounds();
		int availableWidth = (int) (theScreenBounds.width);
		int availableHeight = (int) (theScreenBounds.height);

		int spaceAbove = ls.y - lpls.y;
		int spaceUnder = (lpls.y + availableHeight) - (ls.y + myH);
		int spaceBefore = ls.x - lpls.x;
		int spaceAfter = (lpls.x + availableWidth) - (ls.x + myW);

		int x;
		int y;
		int w = aPopupDimension.width;
		int h = aPopupDimension.height;

		if (w > availableWidth) w = availableWidth;
		if (h > availableHeight) h = availableHeight;

		x = ls.x;
		y = ls.y;

		switch (aPreferredDirection)
		{
			case JLabel.TOP:
				if (h <= spaceAbove) y -= h;
				else if (h <= spaceUnder) y += myH;
				else
				{
					y = 0;
					if (w <= spaceAfter) x += myW;
					else if (w <= spaceBefore) x -= w;
					else x = 0;
				}
				break;

			case JLabel.BOTTOM:
				if (h <= spaceUnder) y += myH;
				else if (h <= spaceAbove) y -= h;
				else
				{
					y = 0;
					if (w <= spaceAfter) x += myW;
					else if (w <= spaceBefore) x -= w;
					else x = 0;
				}
				break;

			case JLabel.LEFT:
				if (w <= spaceBefore) x -= w;
				else if (w <= spaceAfter) x += myW;
				else
				{
					x = 0;
					if (h <= spaceUnder) y += myH;
					else if (h <= spaceAbove) y -= h;
					else y = 0;
				}
				break;

			case JLabel.RIGHT:
				if (w <= spaceAfter) x += myW;
				else if (w <= spaceBefore) x -= w;
				else
				{
					x = 0;
					if (h <= spaceUnder) y += myH;
					else if (h <= spaceAbove) y -= h;
					else y = 0;
				}
				break;
		}

		if (h < aPopupDimension.height) w += 30; // To compensate for a scrollbar
		if (w < aPopupDimension.width) h += 30;

		if (x+w > availableWidth) x = availableWidth - w;
		if (y+h > availableHeight) y = availableHeight - h;

		return new Rectangle (x, y, w, h);
	}

	/**
	 * Displays the sepcified popup menu next to the specified popup trigger component.
	 */
	public static void showPopupMenu (JComponent aPopupTriggerComponent, int aPreferredDirection, JPopupMenu aPopupMenu)
	{
		Rectangle theScreenBounds = computePopupBounds(aPopupTriggerComponent, aPreferredDirection, aPopupMenu.getPreferredSize());

		Point theLocationOnScreen = aPopupTriggerComponent.getLocationOnScreen();
		int x = theScreenBounds.x - theLocationOnScreen.x;
		int y = theScreenBounds.y - theLocationOnScreen.y;
		aPopupMenu.show(aPopupTriggerComponent, x, y);
	}

	/**
	 * This class is capable to display a popup window.
	 * @see #show
	 * @see #hide
	 */
	public static class Popup implements AWTEventListener
	{
		private JComponent itsPopup;
		private JComponent itsTriggerComponent;

		private boolean itsShown = false;

		/**
		 * Set this to true to cause the popup to be hidden when the mouse is clicked
		 * on any point of the frame not in the popup
		 */
		private boolean itsAutoHide = true;

		private JWindow itsPopupWindow;

		private JComponent itsScreen;

		/**
		 * The direction where to place the popup. Must be one of JLabel.TOP, JLabel.BOTTOM, JLabel.LEFT,
		 * JLabel.RIGHT
		 */
		private int itsPreferredDirection = JLabel.BOTTOM;

//		private List itsChangeListeners = new ArrayList ();
		private List itsPopupListeners = new ArrayList ();

		private JFrame itsOwner;
		private JRootPane itsRootPane;

		public Popup (JFrame aOwner, JRootPane aRootPane, JComponent aPopup, JComponent aTriggerComponent)
		{
			itsOwner = aOwner;
			itsRootPane = aRootPane;
			itsTriggerComponent = aTriggerComponent;
			setPopup (aPopup);
//			prepare();
		}



		public void addPopupListener (PopupListener aListener)
		{
			itsPopupListeners.add (aListener);
		}

		public void removePopupListener (PopupListener aListener)
		{
			itsPopupListeners.remove (aListener);
		}

		void firePopupShown ()
		{
			if (itsPopup instanceof PopupListener)
			{
				PopupListener thePopupListener = (PopupListener) itsPopup;
				thePopupListener.popupShown ();
			}

			for (Iterator theIterator = itsPopupListeners.iterator (); theIterator.hasNext ();)
			{
				PopupListener theListener = (PopupListener) theIterator.next ();
				theListener.popupShown();
			}
		}

		void firePopupHidden ()
		{
			if (itsPopup instanceof PopupListener)
			{
				PopupListener thePopupListener = (PopupListener) itsPopup;
				thePopupListener.popupHidden ();
			}

			for (Iterator theIterator = itsPopupListeners.iterator (); theIterator.hasNext ();)
			{
				PopupListener theListener = (PopupListener) theIterator.next ();
				theListener.popupHidden();
			}
		}

		private void prepare ()
		{
			if (itsOwner != null)
			{
				itsOwner.addComponentListener(new ComponentAdapter ()
				{
					public void componentHidden (ComponentEvent e)
					{
						hide();
					}

					public void componentResized (ComponentEvent e)
					{
						repositionPopup();
					}

					public void componentMoved (ComponentEvent e)
					{
						repositionPopup();
					}
				});
				itsOwner.addWindowListener(new WindowAdapter ()
				{
					public void windowClosed (WindowEvent e)
					{
						hide();
					}
				});
			}

			itsPopupWindow = new JWindow (itsOwner);
//		itsPopupWindow.setFocusable(false);
		}

		public void setPreferredDirection (int aPreferredDirection)
		{
			itsPreferredDirection = aPreferredDirection;
		}

		/**
		 * If set to true, the popup will be hidden when the mouse is clicked outside
		 * the popup.
		 */
		public void setAutoHide (boolean aAutoDisappear)
		{
			itsAutoHide = aAutoDisappear;
		}

		public void setPopup (JComponent aPopup)
		{
			itsPopup = aPopup;
			if (aPopup instanceof PopupInterface)
			{
				PopupInterface thePopupInterface = (PopupInterface) aPopup;
				thePopupInterface.setContext (this, itsOwner);
			}

		}

		public JComponent getPopup ()
		{
			return itsPopup;
		}

		public JComponent getTriggerComponent ()
		{
			return itsTriggerComponent;
		}

		public void setTriggerComponent (JComponent aTriggerComponent)
		{
			itsTriggerComponent = aTriggerComponent;
		}

		public boolean isPopupShown ()
		{
			return itsShown;
		}

		public void togglePopup ()
		{
			if (itsShown) hide ();
			else show ();
		}

		private Rectangle getPopupBounds ()
		{
			return PopupManager.computePopupBounds(itsOwner, itsTriggerComponent, itsPreferredDirection, itsPopupWindow.getPreferredSize());
		}

		private void repositionPopup ()
		{
			if (! itsShown) return;
			if (getPopup () == null) return;

			itsPopupWindow.setBounds (getPopupBounds());
			if (itsScreen != null)
			{
				JLayeredPane lp = itsRootPane.getLayeredPane ();
				itsScreen.setBounds(0, 0, lp.getWidth(), lp.getHeight());
			}
			itsPopupWindow.toFront();
		}

		/**
		 * Displays the popup window on the screen, next to the trigger component.
		 * Tries to respect the preferred direction.
		 */
		public void show ()
		{
			show (true);
		}

		/**
		 * Displays the popup window on the screen, next to the trigger component.
		 * Tries to respect the preferred direction.
		 */
		public void show (boolean aNotify)
		{
			if (itsShown) return;
			if (getPopup () == null) return;

			if (itsOwner == null) itsOwner = (JFrame) Utils.getFrame (itsTriggerComponent);
			if (itsRootPane == null) itsRootPane = itsOwner.getRootPane();

			if (itsPopupWindow == null) prepare();

			JLayeredPane lp = itsRootPane.getLayeredPane ();

			itsPopupWindow.setContentPane (getPopup());
			itsPopupWindow.setBounds(getPopupBounds());
			itsPopupWindow.show ();

			if (itsAutoHide)
			{
				itsScreen = new TransparentPanel (null);
				lp.add (itsScreen, JLayeredPane.POPUP_LAYER);
				lp.moveToFront(itsScreen);
				itsScreen.setBounds(0, 0, lp.getWidth(), lp.getHeight());
				itsRootPane.getToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK);
			}
			else
			{
				itsScreen = null;
			}

			itsShown = true;

			if (aNotify) firePopupShown();
		}

		/**
		 * Hides the popup.
		 */
		public void hide ()
		{
			hide (true);
		}

		/**
		 * Hides the popup.
		 */

		public void hide (boolean aNotify)
		{
			if (! itsShown) return;
			if (itsAutoHide)
			{
				itsRootPane.getLayeredPane().remove (itsScreen);
				itsOwner.getToolkit().removeAWTEventListener(this);
			}
			itsPopupWindow.hide();
			itsShown = false;

			if (aNotify) firePopupHidden();
		}

		public void revalidatePopup ()
		{
			itsPopup.revalidate();
			repositionPopup();
			itsPopupWindow.validate();
			itsPopup.repaint();
		}

		/**
		 * Under windows, popup windows tend to go to the 
		 * background when clicked.
		 */
		private static boolean itsFrameWorkaround;
		static {
			String theOSName = System.getProperty("os.name");
			itsFrameWorkaround = theOSName.toUpperCase().indexOf("WINDOWS") >= 0;
		}
		
		public void eventDispatched (AWTEvent aEvent)
		{
			if (aEvent.getID() == MouseEvent.MOUSE_PRESSED)
			{
				MouseEvent theEvent = (MouseEvent) aEvent;
				Component theSource = (Component) theEvent.getSource();

				if (Utils.containsComponent(itsTriggerComponent, theSource)) return;
				else if (isInsidePopup(theSource))
				{
					if (itsFrameWorkaround) itsPopupWindow.show ();
					return;
				}
				else hide ();
//
//				Point theSourceSL = theSource.getLocationOnScreen();
//
//				int theSX = theEvent.getX() + theSourceSL.x;
//				int theSY = theEvent.getY() + theSourceSL.y;
//
//				Point thePopupSL = itsPopupWindow.getLocationOnScreen();
//				int theWidth = itsPopupWindow.getWidth();
//				int theHeight = itsPopupWindow.getHeight();
//
//				if (theSX < thePopupSL.x || theSX > thePopupSL.x + theWidth
//						|| theSY < thePopupSL.y ||theSY > thePopupSL.y + theHeight)
//					hide();
			}
		}
		/**
		 * @return
		 */
		public JFrame getOwner() {
			return itsOwner;
		}

		/**
		 * @param aFrame
		 */
		public void setOwner(JFrame aFrame) 
		{
			itsOwner = aFrame;
			itsRootPane = itsOwner.getRootPane();
		}

	}

	private static boolean isInsidePopup (Component aComponent)
	{
		while (aComponent != null)
		{
			if (aComponent instanceof Window)
			{
				return ! (aComponent instanceof TopLevel);
			}
			aComponent = aComponent.getParent();
		}
		return false;
	}

}
