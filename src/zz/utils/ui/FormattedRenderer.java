/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 30 oct. 2002
 * Time: 18:08:31
 */
package zz.utils.ui;

import javax.swing.Icon;

import zz.utils.Formatter;

/**
 * Renderer that uses formatters to determine the text of list elements/cell tables
 * and their tooltips.
 */
public class FormattedRenderer extends UniversalRenderer
{
	private Formatter itsNameFormatter;
	private Formatter itsToolTipTextFormatter;

	public FormattedRenderer ()
	{
		this (null, null);
	}

	public FormattedRenderer (Formatter aNameFormatter)
	{
		this (aNameFormatter, null);
	}

	public FormattedRenderer (Formatter aNameFormatter, Formatter aToolTipTextFormatter)
	{
		itsNameFormatter = aNameFormatter;
		itsToolTipTextFormatter = aToolTipTextFormatter;
	}

	public void setNameFormatter (Formatter aNameFormatter)
	{
		itsNameFormatter = aNameFormatter;
	}

	public void setToolTipTextFormatter (Formatter aToolTipTextFormatter)
	{
		itsToolTipTextFormatter = aToolTipTextFormatter;
	}

	protected String getName (Object aObject)
	{
		if (itsNameFormatter != null) return itsNameFormatter.getText(aObject);
		else if (aObject != null) return aObject.toString();
		else return "";
	}

	protected String getToolTipText (Object aObject)
	{
		if (itsToolTipTextFormatter != null) return itsToolTipTextFormatter.getText(aObject);
		else if (aObject != null) return aObject.toString();
		else return "";
	}
}
