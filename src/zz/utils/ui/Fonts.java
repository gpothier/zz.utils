/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 26 févr. 2003
 * Time: 15:13:09
 */
package zz.utils.ui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class permits to retrieve font metrics and to compute text sizes.
 */
public class Fonts
{
	public static final Font DEFAULT_FONT_PLAIN = new Font("SansSerif", Font.PLAIN, 12);
	public static final Font DEFAULT_FONT_BOLD = new Font("SansSerif", Font.BOLD, 12);
	public static final Font DEFAULT_FONT_ITALIC = new Font("SansSerif", Font.ITALIC, 12);

	private static Map itsMetricsMap = new HashMap ();

	private static Graphics2D DEFAULT_GRAPHIC = createDefaultGraphics();

	private static Graphics2D createDefaultGraphics ()
	{
		GraphicsDevice theScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		GraphicsConfiguration theConfiguration = theScreenDevice.getDefaultConfiguration();
		return theConfiguration.createCompatibleImage(1, 1).createGraphics();
	}

	/**
	 * Retrieves font metrics for the given font.
	 * Note that the metrics are cached in a hashmap.
	 */
	public static FontMetrics getFontMetrics (Font aFont)
	{
		assert aFont != null;
		FontMetrics theMetrics = (FontMetrics) itsMetricsMap.get (aFont);
		if (theMetrics == null)
		{
			theMetrics = DEFAULT_GRAPHIC.getFontMetrics(aFont);
			itsMetricsMap.put (aFont, theMetrics);
		}
		return theMetrics;
	}

	public static Dimension getTextSize (String aText, FontMetrics aMetrics)
	{
		if (aText == null) return new Dimension();
		return new Dimension(aMetrics.stringWidth(aText), aMetrics.getAscent () + aMetrics.getDescent());
	}

	public static Dimension getTextSize (String aText, Font aFont)
	{
		FontMetrics theMetrics = getFontMetrics(aFont);
		return getTextSize(aText, theMetrics);
	}
}
