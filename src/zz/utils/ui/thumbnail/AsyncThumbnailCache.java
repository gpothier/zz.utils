/*
 * Created on Mar 25, 2005
 */
package zz.utils.ui.thumbnail;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	private LinkedList<Key<T>> itsQueuedRequests = new LinkedList<Key<T>>();
	
	/**
	 * The maximum number of requests to keep in the queue.
	 */
	private int itsMaxRequests;
	
	/**
	 * The order for processing requests.
	 */
	private ProcessingOrder itsProcessingOrder;

	/**
	 * We keep a set of ignored keys so that we don't try to indefinitely reload
	 * keys that can't be loaded.
	 */
	private Set<Key<T>> itsIgnoredKeys = new HashSet<Key<T>>();

	private List<IAsyncThumbnailCacheListener> itsListeners = new ArrayList<IAsyncThumbnailCacheListener>();


	public AsyncThumbnailCache(int aMaxPermanentThumbnails, int aMaxRequests, ProcessingOrder aOrder)
	{
		super(aMaxPermanentThumbnails);
		
		itsMaxRequests = aMaxRequests;
		itsProcessingOrder = aOrder;
		
		Thread theThread = new Thread(this);
		theThread.setPriority(Thread.MIN_PRIORITY);
		theThread.start();
	}

	/**
	 * Clears the requests queue. 
	 */
	public synchronized void clearQueue()
	{
		itsQueuedRequests.clear();
	}
	
	/**
	 * Enqueues a request for loading the image described by the given key.
	 */
	private synchronized void queueRequest(Key<T> aKey)
	{
        if (itsIgnoredKeys.contains(aKey)) return;
		if (!itsQueuedRequests.contains(aKey)) 
		{
			itsQueuedRequests.add(aKey);
			if (itsQueuedRequests.size() > itsMaxRequests) itsQueuedRequests.removeFirst();
		}
	}

	/**
	 * Returns a copy of the current requests. 
	 */
	private synchronized Key<T> popRequest()
	{
		if (itsQueuedRequests.isEmpty()) return null;
		else 
		{
			switch (itsProcessingOrder)
			{
				case FIFO: return itsQueuedRequests.removeFirst();
				case LIFO: return itsQueuedRequests.removeLast();
				default: return null;
			}
		}
	}
	
	protected BufferedImage getThumbnail(Key<T> aKey)
	{
		BufferedImage theImage = getCached(aKey);

		if (theImage == null)
		{
			queueRequest(aKey);
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
