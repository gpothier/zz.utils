/*
 * Created on Sep 29, 2005
 */
package zz.utils.ui;

import java.awt.event.MouseEvent;

/**
 * Utility class to healp dealing with key modifiers
 * used with mouse events. It is helpful if one doesn't 
 * want to deal with each modifier independently but rather
 * consider their combinations.
 * <br/>
 * There are also utility methods for determining the state of 
 * a particular modifier.
 * @author gpothier
 */
public enum MouseModifiers
{
	NONE,
	CTRL, SHIFT, ALT,
	CTRL_SHIFT, CTRL_ALT, SHIFT_ALT,
	CTRL_SHIFT_ALT;
	
	/**
	 * Returns the modifier corresponding to the given event.
	 */
	public static MouseModifiers getModifiers (MouseEvent aEvent)
	{
		int theModifiers = aEvent.getModifiersEx() 
			& (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK | MouseEvent.ALT_DOWN_MASK);
		
		if (theModifiers == 0) return NONE;
		else if (theModifiers == MouseEvent.CTRL_DOWN_MASK) return CTRL;
		else if (theModifiers == MouseEvent.SHIFT_DOWN_MASK) return SHIFT;
		else if (theModifiers == MouseEvent.ALT_DOWN_MASK) return ALT;
		else if (theModifiers == (MouseEvent.CTRL_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK)) return CTRL_SHIFT;
		else if (theModifiers == (MouseEvent.CTRL_DOWN_MASK | MouseEvent.ALT_DOWN_MASK)) return CTRL_ALT;
		else if (theModifiers == (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.ALT_DOWN_MASK)) return SHIFT_ALT;
		else if (theModifiers == (MouseEvent.CTRL_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK | MouseEvent.ALT_DOWN_MASK)) return CTRL_SHIFT_ALT;
		else throw new RuntimeException("Not handled: "+theModifiers);
	}
	
	public static boolean hasShift (MouseEvent aEvent)
	{
		return (aEvent.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != 0;
	}
	
	public static boolean hasCtrl (MouseEvent aEvent)
	{
		return (aEvent.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;		
	}
	
	public static boolean hasAlt (MouseEvent aEvent)
	{
		return (aEvent.getModifiersEx() & MouseEvent.ALT_DOWN_MASK) != 0;
	}
}
