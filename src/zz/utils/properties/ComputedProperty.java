package zz.utils.properties;

import java.util.Arrays;
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
implements IRWProperty<T>
{
	// Volatile is needed for double-checked locking to work 
	public volatile static ThreadLocal<Set<IProperty>> TMP_DEPS;
	private Set<IProperty> itsDependencies = new HashSet<IProperty>();
	
	private boolean itsDirty = true;
	private boolean itsFireScheduled = false;
	private T itsValue;
	
	private IPropertyListener itsListener = new IPropertyListener() 
	{
		public void propertyChanged(IProperty aProperty, Object aOldValue, Object aNewValue)
		{
			synchronized(ComputedProperty.this) 
			{
				itsDirty = true;
				scheduleFire();
			}
		}
		
		public String toString() {
			return "Listener for computed property: "+ComputedProperty.this;
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
		try
		{
			itsValue = compute0();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		itsDirty = false;
		TMP_DEPS.set(null);
		for(IProperty<Object> p : itsDependencies) p.addListener(itsListener);
	}
	
	private void scheduleFire() 
	{
		if (SwingUtilities.isEventDispatchThread()) {
			if (itsFireScheduled) return;
			itsFireScheduled = true;
			final T oldValue = itsValue;
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					if (itsDirty) compute();
					if (oldValue != itsValue) firePropertyChanged(oldValue, itsValue);
					itsFireScheduled = false;
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
		addToDeps();
		if (itsDirty) compute();
		return itsValue;
	}
	
	@Override
	public T set(T aValue)
	{
		throw new RuntimeException("Cannot change value of computed property");
	}

	@Override
	public boolean canSet(T aValue)
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return getClass().getName() + "@" + System.identityHashCode(this);
	}
	
	/**
	 * Creates a computed property that represents the
	 * conjunction (boolean AND) of other properties.
	 */
	public static IProperty<Boolean> And(final Iterable<IProperty<Boolean>> properties) {
		return new ComputedProperty<Boolean>() {
			@Override
			protected Boolean compute0()
			{
				for(IProperty<Boolean> p : properties) if (! p.get()) return false;
				return true;
			}
		};
	}
	
	/**
	 * Creates a computed property that represents the
	 * conjunction (boolean AND) of other properties.
	 */
	public static IProperty<Boolean> And(IProperty<Boolean>... properties) 
	{
		return And(Arrays.asList(properties));
	}
	
	/**
	 * Creates a computed property that represents the
	 * disjunction (boolean OR) of other properties.
	 */
	public static IProperty<Boolean> Or(final Iterable<IProperty<Boolean>> properties) {
		return new ComputedProperty<Boolean>() {
			@Override
			protected Boolean compute0()
			{
				for(IProperty<Boolean> p : properties) if (p.get()) return true;
				return false;
			}
		};
	}
	
	/**
	 * Creates a computed property that represents the
	 * disjunction (boolean OR) of other properties.
	 */
	public static IProperty<Boolean> Or(IProperty<Boolean>... properties) 
	{
		return Or(Arrays.asList(properties));
	}

	/**
	 * Creates a computed property that represents the
	 * negation of other properties.
	 */
	public static IProperty<Boolean> Not(final IProperty<Boolean> property) {
		return new ComputedProperty<Boolean>() {
			@Override
			protected Boolean compute0()
			{
				return ! property.get();
			}
		};
	}
}
