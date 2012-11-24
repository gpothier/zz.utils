package zz.utils.properties;

import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

/**
 * A property whose value depends on the value of other properties.
 * This class automatically gathers and listens to dependent properties.
 * @author gpothier
 *
 * @param <T>
 */
public abstract class ComputedProperty<T> extends AbstractProperty<T>
{
	// Volatile is needed for double-checked locking to work 
	public volatile static ThreadLocal<Set<IProperty>> TMP_DEPS;
	private Set<IProperty> itsDependencies = new HashSet<IProperty>();
	
	private boolean itsDirty = true;
	private T itsValue;
	
	private IPropertyListener itsListener = new IPropertyListener() 
	{
		public void propertyChanged(IProperty aProperty, Object aOldValue, Object aNewValue)
		{
			synchronized(ComputedProperty.this) 
			{
				if (! itsDirty) scheduleFire();
				itsDirty = true;
			}
		}
	};
	
	protected abstract T compute0();
	
	protected void compute() 
	{
		if (TMP_DEPS == null) 
		{
			synchronized(ComputedProperty.class) 
			{
				if (TMP_DEPS == null) TMP_DEPS = new ThreadLocal<Set<IProperty>>();
			}
		}
		for(IProperty<Object> p : itsDependencies) p.removeListener(itsListener);
		itsDependencies.clear();
		TMP_DEPS.set(itsDependencies);
		itsValue = compute0();
		itsDirty = false;
		TMP_DEPS.set(null);
		for(IProperty<Object> p : itsDependencies) p.addListener(itsListener);
	}
	
	private void scheduleFire() 
	{
		if (SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					T oldValue = itsValue;
					compute();
					if (oldValue != itsValue) firePropertyChanged(oldValue, itsValue);
				}
			});
		}
		else
		{
			T oldValue = itsValue;
			compute();
			if (oldValue != itsValue) firePropertyChanged(oldValue, itsValue);
		}
	}

	public T get()
	{
		if (itsDirty) compute();
		return itsValue;
	}
}
