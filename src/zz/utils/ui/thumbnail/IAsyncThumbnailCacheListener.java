/*
 * Created on Mar 25, 2005
 */
package zz.utils.ui.thumbnail;

/**
 * A listsner that is notified whenever an {@link zz.utils.ui.thumbnail.AsyncThumbnailCache}
 * created a new thumbnail in the background.
 * @author gpothier
 */
public interface IAsyncThumbnailCacheListener
{
	public void thumbnailCreated();
}
