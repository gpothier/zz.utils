/*
 * Created on Aug 21, 2004
 */
package zz.utils.ui.text;

import java.awt.Font;

/**
 * This class represents a font. It aggregates various information
 * to the standard awt {@link java.awt.Font} object.
 * @author gpothier
 */
public class XFont
{
	private Font itsAWTFont;
	private boolean itsUnderline;

	public XFont()
	{
		super();
	}
	
	public XFont(Font aAWTFont, boolean aUnderline)
	{
		itsAWTFont = aAWTFont;
		itsUnderline = aUnderline;
	}
	
	public Font getAWTFont()
	{
		return itsAWTFont;
	}
	
	public void setAWTFont(Font aAWTFont)
	{
		itsAWTFont = aAWTFont;
	}
	
	public boolean isUnderline()
	{
		return itsUnderline;
	}

	public void setUnderline(boolean aUnderline)
	{
		itsUnderline = aUnderline;
	}
	
}
