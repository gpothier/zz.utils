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
{	private boolean itsUseFutures;
	
	private I itsNext;
	private B itsBuffer;
	private Future<B> itsFutureBuffer;
	
	private int itsIndex;
	private int itsRemaining;
	
	public BufferedIterator()
	{
		this(false);
	}
	
	public BufferedIterator(boolean aUseFutures)
	{
		itsUseFutures = aUseFutures;
	}

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
	
	protected void reset()
	{
		itsRemaining = 0;
		itsBuffer = null;
		itsFutureBuffer = null;
		itsNext = null;
		fetchNext();
	}
	

	private void fetchNext()
	{
		if (itsRemaining == 0) 
		{
			if (itsUseFutures)
			{
				itsFutureBuffer = new Future<B>()
				{
					@Override
					protected B fetch() throws Throwable
					{
						return fetchNextBuffer();
					}
				};
				
				return;
			}
			else
			{
				itsBuffer = fetchNextBuffer();
				itsIndex = 0;
				itsRemaining = itsBuffer != null ? getSize(itsBuffer) : 0;
			}
		}
		
		if (itsBuffer != null)
		{
			itsNext = get(itsBuffer, itsIndex++);
			itsRemaining--;
		}
		else itsNext = null;
	}
	
	private I getNext()
	{
		if (itsFutureBuffer != null)
		{
			itsBuffer = itsFutureBuffer.get();
			itsIndex = 0;
			itsRemaining = itsBuffer != null ? getSize(itsBuffer) : 0;

			if (itsBuffer != null)
			{
				itsNext = get(itsBuffer, itsIndex++);
				itsRemaining--;
			}
			else itsNext = null;

			itsFutureBuffer = null;
		}

		return itsNext;
	}

	public boolean hasNext()
	{
		return getNext() != null;
	}

	public I next()
	{
		if (! hasNext()) throw new NoSuchElementException();
		I theResult = getNext();
		fetchNext();
		return theResult;
	}

	public void remove()
	{
		throw new UnsupportedOperationException();
	}
	
	
}
