/*
 * Created on Apr 4, 2007
 */
package zz.utils.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * A layout manager similar to {@link FlowLayout} but that wraps components
 * to a given width. The width must be explicitly specified by the client.
 * @author gpothier
 */
public class WrappedFlowLayout 
implements LayoutManager, ComponentListener
{
	private int itsWidth;
	private Container itsContainer = null;

	public int getWidth()
	{
		return itsWidth;
	}

	public void setWidth(int aWidth)
	{
		itsWidth = aWidth;
	}

	public void addLayoutComponent(String aName, Component aComp)
	{
	}

	public void removeLayoutComponent(Component aComp)
	{
	}
	
	private void setContainer(Container aContainer)
	{
		if (itsContainer == null) 
		{
			itsContainer = aContainer;
			itsContainer.addComponentListener(this);
		}
		else throw new RuntimeException("This layout manager is already associated to a container");
	}
	
	public void layoutContainer(Container aParent)
	{
		int theCurrentHeight = 0;
		int theX = 0;
		int theY = 0;
		
		for(int i=0;i<aParent.getComponentCount();i++)
		{
			Component theComponent = aParent.getComponent(i);
			if (! theComponent.isVisible()) continue;
			
			Dimension theSize = theComponent.getPreferredSize();
			if (theSize.width <= itsWidth-theX)
			{
				theComponent.setBounds(theX, theY, theSize.width, theSize.height);
				
				theX += theSize.width;
				theCurrentHeight = Math.max(theCurrentHeight, theSize.height);
			}
			else
			{
				theY += theCurrentHeight;
				theCurrentHeight = theSize.height;
				theX = 0;
			}
		}
	}

	public Dimension preferredLayoutSize(Container aParent)
	{
		int theTotalHeight = 0;
		
		int theCurrentHeight = 0;
		int theCurrentWidth = 0;
		
		for(int i=0;i<aParent.getComponentCount();i++)
		{
			Component theComponent = aParent.getComponent(i);
			if (! theComponent.isVisible()) continue;
			
			Dimension theSize = theComponent.getPreferredSize();
			if (theSize.width <= itsWidth-theCurrentWidth)
			{
				theCurrentWidth += theSize.width;
				theCurrentHeight = Math.max(theCurrentHeight, theSize.height);
			}
			else
			{
				theTotalHeight += theCurrentHeight;
				theCurrentHeight = theSize.height;
				theCurrentWidth = 0;
			}
		}
		theTotalHeight += theCurrentHeight;
		
		return new Dimension(itsWidth, theTotalHeight);
	}

	public Dimension minimumLayoutSize(Container aParent)
	{
		return preferredLayoutSize(aParent);
	}

	public void componentHidden(ComponentEvent aE)
	{
	}

	public void componentMoved(ComponentEvent aE)
	{
	}

	public void componentResized(ComponentEvent aE)
	{
		assert aE.getComponent() == itsContainer;
		itsWidth = itsContainer.getWidth();
	}

	public void componentShown(ComponentEvent aE)
	{
	}

	
	
}
