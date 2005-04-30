/*
 * Created on Apr 6, 2005
 */
package zz.utils.list;

import javax.swing.AbstractListModel;

/**
 * Wraps an {@link zz.utils.list.IList} into an {@link javax.swing.ListModel}
 * @author gpothier
 */
public class SwingListModel extends AbstractListModel
{
	private IList<Object> itsList;
	
	public SwingListModel()
	{
	}

	public SwingListModel(IList<Object> itsList)
	{
		this.itsList = itsList;
	}

	protected IList<Object> getList()
	{
		return itsList;
	}
	

	protected void setList(IList<Object> aList)
	{
		itsList = aList;
		fireContentsChanged();
	}
	

	public Object getElementAt(int aIndex)
	{
		return itsList.get(aIndex);
	}

	public int getSize()
	{
		return itsList != null ? itsList.size() : 0;
	}

	public void fireContentsChanged()
	{
		fireContentsChanged(this, 0, getSize());
	}
}
