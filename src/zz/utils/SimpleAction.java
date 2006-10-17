/*
 * Created on Oct 15, 2006
 */
package zz.utils;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * An action whose attributes can be conveniently specified in the constructor.
 * @author gpothier
 */
public abstract class SimpleAction extends AbstractAction
{
	public SimpleAction(String aTitle, String aDescription, Icon aIcon)
	{
		putValue(Action.NAME, aTitle);
		putValue(Action.SHORT_DESCRIPTION, aDescription);
		putValue(Action.SMALL_ICON, aIcon);
	}
	
	public SimpleAction(String aTitle, String aDescription)
	{
		this(aTitle, aDescription, null);
	}
	
	public SimpleAction(String aTitle)
	{
		this(aTitle, null);
	}
}
