/*
 * Created on Nov 19, 2007
 */
package zz.utils.primitive;

/**
 * A primitive int array that grows as needed
 * @author gpothier
 */
public class IntArray
{
	private int[] itsData;

	public IntArray()
	{
		this(16);
	}
	
	public IntArray(int aInitialSize)
	{
		itsData = new int[aInitialSize];
	}
	
	public int get(int aIndex)
	{
		return aIndex < itsData.length ? itsData[aIndex] : 0;
	}
	
	public void set(int aIndex, int aValue)
	{
		ensureSize(aIndex+1);
		itsData[aIndex] = aValue;
	}
	
	private void ensureSize(int aSize)
	{
		if (itsData.length >= aSize) return;
		
		int theNewSize = Math.max(aSize, itsData.length*2);
		int[] theNewData = new int[theNewSize];
		System.arraycopy(itsData, 0, theNewData, 0, itsData.length);
		itsData = theNewData;
	}
	
}
