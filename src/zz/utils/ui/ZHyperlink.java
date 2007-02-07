/*
 * Created on Feb 6, 2007
 */
package zz.utils.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import zz.utils.ui.text.XFont;

public abstract class ZHyperlink extends ZLabel
implements MouseListener, MouseMotionListener
{
	private static final boolean WITH_CTRL = false;
	private boolean itsMouseOver = false;

	public ZHyperlink(String aText, XFont aFont, Color aColor)
	{
		super(aText, aFont, aColor);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * This method is called when the link is clicked.
	 */
	protected abstract void traverse();
	
	private static boolean hasCtrl(MouseEvent aEvent)
	{
		return (aEvent.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;
	}
	
	public void mouseEntered(MouseEvent aE)
	{
		setMouseOver(! WITH_CTRL || hasCtrl(aE));
	}
	
	public void mouseExited(MouseEvent aE)
	{
		setMouseOver(false);
	}

	public void mouseMoved(MouseEvent aE)
	{
		boolean theCtrl = ! WITH_CTRL || hasCtrl(aE); 
		setMouseOver(theCtrl);
	}
	
	private void setMouseOver(boolean aMouseOver)
	{
		if (itsMouseOver != aMouseOver)
		{
			setXFont(new XFont(getXFont().getAWTFont(), aMouseOver));
			itsMouseOver = aMouseOver;
			repaint();
		}
	}
	
	public void mousePressed(MouseEvent aE)
	{
		if (! WITH_CTRL || MouseModifiers.hasCtrl(aE))
		{
			traverse();
			aE.consume();
		}
	}

	public void mouseClicked(MouseEvent aE)
	{
	}

	public void mouseReleased(MouseEvent aE)
	{
	}

	public void mouseDragged(MouseEvent aE)
	{
	}
}
