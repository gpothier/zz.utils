/*
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Dec 4, 2001
 * Time: 3:30:48 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package zz.utils.ui;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

import zz.utils.Utils;

/**
 * A LayoutManager very similar to GridLayout except that cells of different rows
 * dont' have to have the same height (but all cells within a row have the same
 * height) and columns don't necessarily have the same width. Instead, their width
 * is based on the maximum width of the cells.
 */
public class GridStackLayout implements LayoutManager2
{

	protected int itsNColumns;
	protected int itsHGap;
	protected int itsVGap;

	protected int[] itsColumnsWidths;
	protected int[] itsRowHeights;


	public GridStackLayout (int aNColumns)
	{
		this (aNColumns, 0, 0);
	}

	public GridStackLayout (int aNColumns, int aHGap, int aVGap)
	{
		itsNColumns = aNColumns;
		itsHGap = aHGap;
		itsVGap = aVGap;
	}

	public void addLayoutComponent (Component aComponent, Object aConstraints)
	{
	}

	public void addLayoutComponent (String aName, Component aComponent)
	{
	}

	public void removeLayoutComponent (Component aComponent)
	{
	}

	public float getLayoutAlignmentX (Container aTarget)
	{
		return 0.5f;
	}

	public float getLayoutAlignmentY (Container aTarget)
	{
		return 0.5f;
	}

	public void invalidateLayout (Container aTarget)
	{
	}

	public void layoutContainer (Container aTarget)
	{
		computeCellsSizes(aTarget, Utils.PREFERRED_SIZE);

		Dimension thePreferredSize = preferredLayoutSize(aTarget);
		double theScaleX = 1.0*aTarget.getWidth() / thePreferredSize.width;
		double theScaleY = 1.0*aTarget.getHeight() / thePreferredSize.height;

		Component[] theChildren = aTarget.getComponents();
		int theNChildren = theChildren.length;

		Insets theInsets = aTarget.getInsets();

		int theCurrentColumn = 0;
		int theCurrentRow = 0;

		int x = theInsets.left;
		int y = theInsets.top;

		for (int i = 0; i < theNChildren; i++)
		{
			Component theChild = theChildren[i];

			int theWidth = (int)(theScaleX * itsColumnsWidths[theCurrentColumn]);
			int theHeight = (int)(theScaleY * itsRowHeights[theCurrentRow]);
			theChild.setBounds(x, y, theWidth, theHeight);

			theCurrentColumn++;
			x += theWidth + itsHGap;
			if (theCurrentColumn >= itsNColumns)
			{
				theCurrentColumn = 0;
				x = theInsets.left;
				theCurrentRow++;
				y += theHeight + itsVGap;
			}
		}

	}

	protected void computeCellsSizes (Container aTarget, int aType)
	{
		Component[] theChildren = aTarget.getComponents();
		int theNChildren = theChildren.length;
		int theNRows = (int)Math.ceil (theNChildren/itsNColumns);

		itsColumnsWidths = new int[itsNColumns];
		for (int i = 0; i < itsNColumns; i++) itsColumnsWidths[i] = aType != Utils.MAXIMUM_SIZE ? 0 : Integer.MAX_VALUE;

		itsRowHeights = new int[theNRows];
		for (int i=0; i < theNRows; i++) itsRowHeights[i] = aType != Utils.MAXIMUM_SIZE ? 0 : Integer.MAX_VALUE;

		int theCurrentColumn = 0;
		int theCurrentRow = 0;

		for (int i = 0; i < theNChildren; i++)
		{
			Component theChild = theChildren[i];
			Dimension theChildSize = Utils.getASize (theChild, aType);

			if (aType != Utils.MAXIMUM_SIZE)
			{
				itsColumnsWidths[theCurrentColumn] = Math.max (itsColumnsWidths[theCurrentColumn], theChildSize.width);
				itsRowHeights[theCurrentRow] = Math.max (itsRowHeights[theCurrentRow], theChildSize.height);
			}
			else
			{
				itsColumnsWidths[theCurrentColumn] = Math.min (itsColumnsWidths[theCurrentColumn], theChildSize.width);
				itsRowHeights[theCurrentRow] = Math.min (itsRowHeights[theCurrentRow], theChildSize.height);
			}

			theCurrentColumn++;
			if (theCurrentColumn >= itsNColumns)
			{
				theCurrentColumn = 0;
				theCurrentRow++;
			}
		}
	}

	public Dimension layoutSize (Container aTarget, int aType)
	{
		computeCellsSizes(aTarget, aType);

		Insets theInsets = aTarget.getInsets();

		int theTotalWidth = Math.max (itsNColumns-1, 0) * itsHGap + theInsets.left + theInsets.right;
		for (int i = 0; i < itsColumnsWidths.length; i++) theTotalWidth += itsColumnsWidths[i];

		int theTotalHeight = Math.max (itsRowHeights.length-1, 0) * itsVGap + theInsets.top + theInsets.bottom;
		for (int i = 0; i < itsRowHeights.length; i++) theTotalHeight += itsRowHeights[i];

		return new Dimension(theTotalWidth, theTotalHeight);
	}

	public Dimension maximumLayoutSize (Container aTarget)
	{
		return layoutSize(aTarget, Utils.MAXIMUM_SIZE);
	}

	public Dimension minimumLayoutSize (Container aTarget)
	{
		return layoutSize(aTarget, Utils.MINIMUM_SIZE);
	}

	public Dimension preferredLayoutSize (Container aTarget)
	{
		return layoutSize(aTarget, Utils.PREFERRED_SIZE);
	}
}
