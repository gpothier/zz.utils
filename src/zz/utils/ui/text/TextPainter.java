/*
 * Created on Jun 2, 2004
 */
package zz.utils.ui.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * Provides utilities to paint text.
 * 
 * @author gpothier
 */
public class TextPainter
{
	/**
	 * Paints a text in the specified shape.
	 * 
	 * @param aGraphics The graphics where to paint the text
	 * @param aText The text to paint
	 * @param aShape Shape in which the text should fit.
	 */
	public static void paint(
			Graphics2D aGraphics, 
			Font aFont,
			boolean aUnderline,
			Paint aPaint,
			String aText, 
			Shape aShape)
	{
		Rectangle2D theBounds;
		
		if (aShape instanceof Rectangle2D)
		{
			theBounds = (Rectangle2D) aShape;
		}
		else if (aShape instanceof Ellipse2D)
		{
			Ellipse2D theEllipse = (Ellipse2D) aShape;
			double theX = theEllipse.getMinX();
			double theY = theEllipse.getMinY();
			double theW = theEllipse.getWidth();
			double theH = theEllipse.getHeight();
			
			theBounds = new Rectangle2D.Double (
					theX + theW/4, theY + theH/4,
					theW/2, theH/2);
		}
		else theBounds = aShape.getBounds2D();

		paint(aGraphics, aFont, aUnderline, aPaint, aText, theBounds);
	}

	public static void paint(
			Graphics2D aGraphics, 
			Font aFont, 
			boolean aUnderline,
			Paint aPaint,
			String aText, 
			Rectangle2D aBounds)
	{
		if (aText.length() == 0) return;
		
		//Save previous font & set requested font
		Font thePreviousFont = aGraphics.getFont();
		if (aFont != null) aGraphics.setFont(aFont);
		if (aPaint != null) aGraphics.setPaint(aPaint);
		
		FontRenderContext theContext = aGraphics.getFontRenderContext();

		float theX = (float) aBounds.getX();
		float theY = (float) aBounds.getY();
		float theW = (float) aBounds.getWidth();
		float theMaxY = (float) aBounds.getMaxY();

		AttributedString theString = new AttributedString(aText);
		if (aFont != null) 
		{
			theString.addAttribute(TextAttribute.FONT, aFont);
			theString.addAttribute(
					TextAttribute.UNDERLINE, 
					aUnderline ? TextAttribute.UNDERLINE_ON : null);
		}
		
		AttributedCharacterIterator theIterator = theString.getIterator();

		LineBreakMeasurer measurer = new LineBreakMeasurer(theIterator, theContext);

		while (measurer.getPosition() < aText.length() && theY < theMaxY)
		{
			TextLayout layout = measurer.nextLayout(theW);

			theY += layout.getAscent();
			float theDX = layout.isLeftToRight() ? 0 : (theW - layout.getAdvance());

			layout.draw(aGraphics, theX + theDX, theY);
			theY += layout.getDescent() + layout.getLeading();
		}
		
		// restore previous font
		aGraphics.setFont(thePreviousFont);
	}
}