/*
 * Created on Mar 25, 2005
 */
package zz.utils.ui.thumbnail;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import zz.utils.ui.thumbnail.ThumbnailCache.Key;

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

	private BlockingQueue<Key<T>> itsLoadQueue = new LinkedBlockingQueue<Key<T>>();

	private Set<Key<T>> itsQueuedKeys = new HashSet<Key<T>>();

	/**
	 * We keep a set of ignored keys so that we don't try to indefinitely reload
	 * keys that can't be loaded.
	 */
	private Set<Key<T>> itsIgnoredKeys = new HashSet<Key<T>>();

	private List<IAsyncThumbnailCacheListener> itsListeners = new ArrayList<IAsyncThumbnailCacheListener>();

	public AsyncThumbnailCache()
	{
		new Thread(this).start();
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

	/**
	 * Enqueues q request for loading the image described by the given key.
	 */
	private void queueLoading(Key<T> aKey)
	{
        if (itsIgnoredKeys.contains(aKey)) return;
		if (itsQueuedKeys.add(aKey)) itsLoadQueue.add(aKey);
	}

	public void run()
	{
		try
		{
			while (true)
			{
				Key<T> theKey = itsLoadQueue.take();
				itsQueuedKeys.remove(theKey);

				try
				{
					BufferedImage theImage = createThumbnail(theKey);
                    if (theImage == null) itsIgnoredKeys.add(theKey);
                    else
                    {
						cache(theKey, theImage);
						fireThumbnailCreated();
                    }
				}
				catch (Exception e)
				{
					e.printStackTrace();
                    itsIgnoredKeys.add(theKey);
				}
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
