/*
 * Created on Mar 25, 2005
 */
package zz.utils.ui.thumbnail;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import zz.utils.ArrayStack;

/**
 * A thumbnail cache that always responds immediately to requests. A separate
 * thread creates the thumbnails in the background. Listeners can regigister
 * themselves to be notified when new thumbnails have been created.
 * 
 * @author gpothier
 */
public abstract class AsyncThumbnailCache<T> extends ThumbnailCache<T> implements Runnable
{
	/**
	 * Cache for "loading in progress" images.
	 */
	private ThumbnailCache<Integer> itsLIPCache = new SyncThumbnailCache<Integer>()
	{
		protected BufferedImage createThumbnail(Integer aId, int aMaxSize)
		{
			return AsyncThumbnailCache.this.createThumbnail(null, aMaxSize);
		}
	};

	/**
	 * The set of currently pending thumbnail creation requests. 
	 */
	private LinkedList<Key<T>> itsQueuedKeys = new LinkedList<Key<T>>();

	/**
	 * We keep a set of ignored keys so that we don't try to indefinitely reload
	 * keys that can't be loaded.
	 */
	private Set<Key<T>> itsIgnoredKeys = new HashSet<Key<T>>();

	private List<IAsyncThumbnailCacheListener> itsListeners = new ArrayList<IAsyncThumbnailCacheListener>();

	public AsyncThumbnailCache()
	{
		this(5);
	}

	public AsyncThumbnailCache(int aMaxPermanentThumbnails)
	{
		super (aMaxPermanentThumbnails);
		
		Thread theThread = new Thread(this);
		theThread.setPriority(Thread.NORM_PRIORITY-1);
		theThread.start();
	}
	
	/**
	 * Enqueues a request for loading the image described by the given key.
	 */
	private synchronized void queueLoading(Key<T> aKey)
	{
        if (itsIgnoredKeys.contains(aKey)) return;
		if (!itsQueuedKeys.contains(aKey)) itsQueuedKeys.add(aKey);
	}

	/**
	 * Clears the requests queue. 
	 */
	public synchronized void clearQueue()
	{
		itsQueuedKeys.clear();
	}
	
	/**
	 * Returns a copy of the current requests. 
	 */
	private synchronized Key<T> popRequest()
	{
		if (itsQueuedKeys.isEmpty()) return null;
		else return itsQueuedKeys.removeFirst();
	}
	
	protected BufferedImage getThumbnail(Key<T> aKey)
	{
		BufferedImage theImage = getCached(aKey);

		if (theImage == null)
		{
			queueLoading(aKey);
			return getLIPImage(aKey.getSize());
		}
		else return theImage;
	}

	public void run()
	{
		try
		{
			while (true)
			{
				Key<T> theRequest;

				while ((theRequest = popRequest()) != null)
				{
					try
					{
						BufferedImage theImage = createThumbnail(theRequest);
		                if (theImage == null) itsIgnoredKeys.add(theRequest);
		                else
		                {
							cache(theRequest, theImage);
							fireThumbnailCreated();
		                }
					}
					catch (Exception e)
					{
						e.printStackTrace();
		                itsIgnoredKeys.add(theRequest);
					}
				}
				
				Thread.sleep(100);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private BufferedImage getLIPImage(int aSize)
	{
		return itsLIPCache.getThumbnail(aSize, aSize);
	}

	public void addListsner(IAsyncThumbnailCacheListener aListener)
	{
		itsListeners.add(aListener);
	}

	public void removeListsner(IAsyncThumbnailCacheListener aListener)
	{
		itsListeners.remove(aListener);
	}

	protected void fireThumbnailCreated()
	{
		for (IAsyncThumbnailCacheListener theListener : itsListeners)
		{
			theListener.thumbnailCreated();
		}
	}

}
