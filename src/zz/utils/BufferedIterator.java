/*
 * Created on Sep 12, 2006
 */
package zz.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that fetches data by blocks.
 * Subclasses should call {@link #reset()} in their constructor.
 * @param <B> The type of the buffer (might be a list, an array, or
 * anything that provides random access).
 * @param <I> The type of the items
 * @author gpothier
 */
public abstract class BufferedIterator<B, I> implements Iterator<I>
{
	private I itsNext;
	private B itsBuffer;
	private int itsIndex;
	private int itsRemaining;
	
	/**
	 * Fetches the next available buffer.
	 * @return A buffer, or null if no more elements are available.
	 */
	protected abstract B fetchNextBuffer();
	
	/**
	 * Returns an item of the given buffer.
	 */
	protected abstract I get(B aBuffer, int aIndex);
	
	/**
	 * Returns the size of the given buffer.
	 */
	protected abstract int getSize(B aBuffer);

	private void fetchNextBuffer0()
	{
		itsBuffer = fetchNextBuffer();
		itsIndex = 0;
		itsRemaining = itsBuffer != null ? getSize(itsBuffer) : 0;
	}
	
	protected void reset()
	{
		itsRemaining = 0;
		itsBuffer = null;
		itsNext = null;
		fetchNext();
	}
	

	private void fetchNext()
	{
		if (itsRemaining == 0) fetchNextBuffer0();
		if (itsBuffer != null)
		{
			itsNext = get(itsBuffer, itsIndex++);
			itsRemaining--;
		}
		else itsNext = null;
	}

	public boolean hasNext()
	{
		return itsNext != null;
	}

	public I next()
	{
		if (! hasNext()) throw new NoSuchElementException();
		I theResult = itsNext;
		fetchNext();
		return theResult;
	}

	public void remove()
	{
		throw new UnsupportedOperationException();
	}
	
	
}
