package zz.utils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import zz.utils.ui.NamedObject;

/**
 * Contains useful methods. <p>
 *
 * @version 30-10-2001
 */
public final class Utils
{

	public static final AffineTransform IDENTITY = new AffineTransform();

	/**
	 * Do not use this. <p>
	 */
	private Utils ()
	{
		throw new NoSuchMethodError ();
	}

	/**
	 * Reads the whole specified reader into a string an returns it.
	 */
	public static String readReader (Reader aReader) throws IOException
	{
		StringWriter sw = new StringWriter ();

		char[] cbuf = new char[1024];
		int n;
		while ((n = aReader.read (cbuf)) >= 0)
			sw.write (cbuf, 0, n);

		return sw.toString ();
	}
	
	public static void readFully(InputStream aStream, byte[] aBuffer) throws IOException
	{
		int n = 0;
		int len = aBuffer.length;
		while (n < len)
		{
			int count = aStream.read(aBuffer, n, len - n);
			if (count < 0) throw new EOFException();
			n += count;
		}
	}

	/**
	 * Reads the whole specified InputStream into a string and returns it.
	 */
	public static String readInputStream (InputStream anInputStream) throws IOException
	{
		return readReader (new InputStreamReader (anInputStream));
	}

	/**
	 * Returns the greatest of the three specified integers.
	 */
	public static int max (int i1, int i2, int i3)
	{
		return Math.max (i1, Math.max (i2, i3));
	}

	/**
	 * Returns the greatest of the three specified doubles.
	 */
	public static double max (double d1, double d2, double d3)
	{
		return Math.max (d1, Math.max (d2, d3));
	}

	/**
	 * Returns the smallest of the three specified doubles.
	 */
	public static double min (double d1, double d2, double d3)
	{
		return Math.min (d1, Math.min (d2, d3));
	}


	/**
	 * Tests if objects are both null or equal
	 */
	public static boolean equalOrBothNull (Object o1, Object o2)
	{
		if (o1 == null || o2 == null) return o1 == o2;
		else return o1 == o2 || o1.equals (o2);
	}

	/**
	 * Tests if objects are different
	 */
	public static boolean different (Object o1, Object o2)
	{
		return ! equalOrBothNull(o1, o2);
	}


	/**
	 * Appends to the specified StringBuffer:
	 * a comma (", ") if the buffer was not empty
	 * the specified number followed by a space
	 * either of the two strings, according to the value of the number (if 1, single is used)
	 */
	protected static void writePortion (StringBuffer aBuffer, int aNumber, String aSingleString, String aPluralString)
	{
		if (aBuffer.length () > 0) aBuffer.append (", ");
		aBuffer.append (aNumber);
		aBuffer.append (" ");
		aBuffer.append (aNumber == 1 ? aSingleString : aPluralString);
	}


	/**
	 * Returns the smallest rectangle that contains the specified rectangle after it is transformed
	 * by the specified transform
	 */
	public static Rectangle2D transformRect (Rectangle2D aRectangle, AffineTransform aTransform)
	{
		if (aTransform == null || aTransform.isIdentity ()) return aRectangle;

		double[] coords = new double[]{aRectangle.getX (), aRectangle.getY (),
		                               aRectangle.getX () + aRectangle.getWidth (), aRectangle.getY (),
		                               aRectangle.getX () + aRectangle.getWidth (), aRectangle.getY () + aRectangle.getHeight (),
		                               aRectangle.getX (), aRectangle.getY () + aRectangle.getHeight ()};

		aTransform.transform (coords, 0, coords, 0, 4);

		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;
		for (int i = 0; i < 4; i++)
		{
			double x = coords[2 * i];
			double y = coords[2 * i + 1];
			if (x < minX) minX = x;
			if (x > maxX) maxX = x;
			if (y < minY) minY = y;
			if (y > maxY) maxY = y;
		}

		return new Rectangle2D.Double (minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 *  Returns a non-null string, replacing a null value by an empty string.
	 * @return If aString is not null, returns aString, otherwise returns "".
	 */
	public static String ensureStringNotNull (String aString)
	{
		if (aString != null)
			return aString;
		else
			return "";
	}

    /**
     * Adds to the collections all the items returned by the iterator.
	 * @return The given collection.
     */
    public static final Collection fillCollection (Collection aCollection, Iterator aIterator)
    {
        for (Iterator theIterator = aIterator; theIterator.hasNext ();) aCollection.add (theIterator.next ());
		return aCollection;
    }

    /**
     * Adds to the collections all the items returned by the iterable.
	 * @return The given collection.
     */
    public static final Collection fillCollection (Collection aCollection, Iterable aIterable)
    {
    	return fillCollection(aCollection, aIterable.iterator());
    }
    
    /**
     * Creates a list out of a collection.
     */
    public static <T> List<T> createList (Collection<T> aCollection)
    {
    	List<T> theList = new ArrayList<T>();
    	fillCollection(theList, aCollection);
    	return theList;
    }

	public static final String getObjectName (Object aObject)
	{
		if (aObject == null) return "null";
		else if (aObject instanceof NamedObject)
		{
			NamedObject theNamedObject = (NamedObject) aObject;
			return theNamedObject.getName();
		}
		else return aObject.toString();
	}

	public static final ImageIcon getObjectIcon (Object aObject)
	{
		if (aObject == null) return null;
		else if (aObject instanceof NamedObject)
		{
			NamedObject theNamedObject = (NamedObject) aObject;
			return theNamedObject.getIcon();
		}
		else return null;
	}

	/**
	 * Similar to {@link java.util.List#indexOf}, except that it invokes
	 * the equals method of the objects in the list rather than that of the specified
	 * object.
	 * @param aObject An object to look for.
	 * @return The index of the first object whose equals method returns true.
	 */
	public static int indexOf (Object aObject, java.util.List aList)
	{
		int theIndex = 0;
		for (Iterator theIterator = aList.iterator (); theIterator.hasNext ();)
		{
			Object o = theIterator.next ();
			if (o.equals(aObject)) return theIndex;
			theIndex ++;
		}
		return -1;
	}

	/**
	 * Searches and returns the first object in the list whose equals method returns
	 * true when passed the specified object.
	 */
	public static Object find (Object aObject, java.util.List aList)
	{
		int theIndex = indexOf(aObject, aList);
		if (theIndex == -1) return null;
		else return aList.get (theIndex);
	}
	
	/**
	 * Removes and returns the first object in the list whose equals method
	 * returns true when passed the specified object.
	 */
	public static Object remove (Object aObject, java.util.List aList)
	{
		int theIndex = indexOf(aObject, aList);
		if (theIndex == -1) return null;
		else return aList.remove (theIndex);
	}

	/**
	 * Rounds the specified double with the given number of decimals.
	 */
	public static double round (double aValue, int aNDecimals)
	{
		double theK = Math.pow (10, aNDecimals);

		return Math.round(aValue *= theK) / theK;
	}

	public static Collection cloneCollection (Collection aCollection)
	{
		if (aCollection == null) return null;
		try
		{
			Collection theCloneCollection = (Collection) aCollection.getClass().newInstance();
			for (Iterator theIterator = aCollection.iterator (); theIterator.hasNext ();)
			{
				Object theObject = (Object) theIterator.next ();
				if (theObject instanceof IPublicCloneable)
				{
					IPublicCloneable theCloneable = (IPublicCloneable) theObject;
					Object theClone = theCloneable.clone();
					theCloneCollection.add (theClone);
				}
				else theCloneCollection.add (theObject);
			}
			return theCloneCollection;
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			return null;
		}
	}

	public static Map cloneMap (Map aMap)
	{
		if (aMap == null) return null;
		try
		{
			Map theClone = (Map) aMap.getClass().newInstance();
			for (Iterator theIterator = aMap.entrySet ().iterator (); theIterator.hasNext ();)
			{
				Map.Entry theEntry = (Map.Entry) theIterator.next ();

				Object theKey = theEntry.getKey();
				Object theValue = theEntry.getValue();

				Object theKeyClone;
				Object theValueClone;

				if (theKey instanceof IPublicCloneable)
				{
					IPublicCloneable theCloneable = (IPublicCloneable) theKey;
					theKeyClone = theCloneable.clone();
				}
				else theKeyClone = theKey;

				if (theValue instanceof IPublicCloneable)
				{
					IPublicCloneable theCloneable = (IPublicCloneable) theValue;
					theValueClone = theCloneable.clone();
				}
				else theValueClone = theValue;

				theClone.put (theKeyClone, theValueClone);
			}
			return theClone;
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			return null;
		}
	}

	public static boolean isWithin(int aInf,int aSup,int aValue)
	{
		return (aValue>aInf && aValue<aSup);
	}
	
	/**
	 * Writes a newline and a number of spaces into the specified string builder.
	 */
	public static void indentln (StringBuilder aBuilder, int aIndent)
	{
		aBuilder.append('\n');
		indent(aBuilder, aIndent);
	}

	/**
	 * Writes a number of spaces into the specified string builder.
	 */
	public static void indent (StringBuilder aBuilder, int aIndent)
	{
		for (int i=0;i<aIndent;i++) aBuilder.append(' ');
	}
	
	/**
	 * Merges two arrays in one. 
	 */
	public static <T> T[] merge (T[] aArray1, T... aArray2)
	{
		Class< ? extends Object> theComponentType;
		if (aArray1.length > 0) theComponentType = aArray1[0].getClass();
		else if (aArray2.length > 0) theComponentType = aArray2[0].getClass();
		else return aArray1;
		
		T[] theResult = (T[]) Array.newInstance(theComponentType, aArray1.length + aArray2.length);
        System.arraycopy(aArray1, 0, theResult, 0, aArray1.length);
        System.arraycopy(aArray2, 0, theResult, aArray1.length, aArray2.length);
        return theResult;

	}
	
	/**
	 * Writes all the bytes obtained from the specified input stream into
	 * the specified output stream.
	 */
	public static void pipe(InputStream aInputStream, OutputStream aOutputStream) throws IOException
	{
		pipe(aInputStream, aOutputStream, -1);
	}
	
	/**
	 * Writes all the bytes obtained from the specified input stream into
	 * the specified output stream, until the input stream reaches
	 * end of file, or the specified number of bytes have been
	 * transfered.
	 * @param aByteCount Maximum number of bytes to transfer, or -1
	 * for no limit.
	 */
	public static void pipe(
			InputStream aInputStream, 
			OutputStream aOutputStream,
			int aByteCount) throws IOException
	{
		pipe(new byte[1024], aInputStream, aOutputStream, aByteCount);
	}
			
	/**
	 * Writes all the bytes obtained from the specified input stream into
	 * the specified output stream, until the input stream reaches
	 * end of file, or the specified number of bytes have been
	 * transfered.
	 * @param aBuffer User-specified buffer, so that the buffer
	 * does not have to be instantiated in this method.
	 * @param aByteCount Maximum number of bytes to transfer, or -1
	 * for no limit.
	 */
	public static void pipe(
			byte[] aBuffer,
			InputStream aInputStream, 
			OutputStream aOutputStream,
			int aByteCount) throws IOException
	{
		int theRemaining = aByteCount != -1 ? aByteCount : Integer.MAX_VALUE;
		
		do
		{
			int theCount = aInputStream.read(
				aBuffer, 
				0, 
				Math.min(aBuffer.length, theRemaining));
			
			if (theCount < 0) break;
			
			aOutputStream.write(aBuffer, 0, theCount);
			
			if (aByteCount != -1) theRemaining -= theCount;
			
		} while(theRemaining > 0);
	}

	public static void memset(byte[] aArray, byte aValue)
	{
		memset(aArray, aValue, 16);
	}
	
	public static void memset(byte[] aArray, byte aValue, int k)
	{
		for (int i=0;i<Math.min(aArray.length, k);i++) aArray[i] = aValue;
		if (aArray.length <= k) return;
		
		int thePos = k;
		int theLength = k;
		while (theLength < aArray.length/2)
		{
			System.arraycopy(aArray, 0, aArray, thePos, theLength);
			thePos += theLength;
			theLength *=2;
		}
		
		System.arraycopy(aArray, 0, aArray, thePos, aArray.length-thePos);
	}

	public static void memset(int[] aArray, int aValue)
	{
		memset(aArray, aValue, 16);
	}
	
	public static void memset(int[] aArray, int aValue, int k)
	{
		for (int i=0;i<Math.min(aArray.length, k);i++) aArray[i] = aValue;
		if (aArray.length <= k) return;
		
		int thePos = k;
		int theLength = k;
		while (theLength < aArray.length/2)
		{
			System.arraycopy(aArray, 0, aArray, thePos, theLength);
			thePos += theLength;
			theLength *=2;
		}
		
		System.arraycopy(aArray, 0, aArray, thePos, aArray.length-thePos);
	}
	
	/**
	 * Lexicographic comparison of byte arrays
	 */
	public static int compare(byte[] aBytes1, byte[] aBytes2)
	{
		int len1 = aBytes1.length;
		int len2 = aBytes2.length;
		int n = Math.min(len1, len2);

		int k = 0;
		while (k < n)
		{
			byte b1 = aBytes1[k];
			byte b2 = aBytes2[k];
			if (b1 != b2) return b1 - b2; 
			k++;
		}

		return len1 - len2;
	}

	/**
	 * Sets a list element at the specified position, padding the list with
	 * null values if it is too small.
	 */
	public static <T> void listSet(List<T> aList, int aIndex, T aValue)
	{
		if (aList.size() > aIndex) 
		{
			aList.set(aIndex, aValue);
			return;
		}
		
		while (aList.size() < aIndex) aList.add(null);
		aList.add(aValue);
	}
}