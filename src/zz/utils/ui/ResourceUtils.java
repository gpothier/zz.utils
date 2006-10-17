/*
 * Created on Oct 15, 2006
 */
package zz.utils.ui;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceUtils
{
	private static final GraphicsConfiguration CONFIG = getConfig();
	
	private static GraphicsConfiguration getConfig()
	{
		GraphicsEnvironment theGraphicsEnvironment = 
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		GraphicsConfiguration theConfig = 
			theGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
		
		return theConfig;
	}
	
	/**
	 * Creates an image with the same data as the specified image, but in
	 * a format that is more efficient.
	 */
	public static BufferedImage createCompatibleImage (BufferedImage aImage)
	{
		int theW = aImage.getWidth();
		int theH = aImage.getHeight();
		
		BufferedImage theImage = CONFIG.createCompatibleImage(
				theW, 
				theH, 
				Transparency.TRANSLUCENT);
		
		Graphics2D theImgGraphics = theImage.createGraphics();
		theImgGraphics.drawImage(aImage, 0, 0, null);
		
		return theImage;
	}
	
	
	/**
	 * Loads an image stored as a resource of the given class.
	 */
	public static BufferedImage loadImageResource (Class aReferenceClass, String aName)
	{
		InputStream theStream = aReferenceClass.getResourceAsStream (aName);
		if (theStream == null) throw new RuntimeException("Cannot find resource: "+aName);
		try
		{
			BufferedImage theImage = ImageIO.read(theStream);
			theImage = createCompatibleImage(theImage);
			
			return theImage;
		}
		catch (IOException e)
		{
			System.err.println("Could not load image: "+aName);
			e.printStackTrace();
			return null;
		}
	}
	
	public static ImageIcon loadIconResource (Class aReferenceClass, String aName)
	{
		return new ImageIcon (loadImageResource(aReferenceClass, aName));
	}



}
