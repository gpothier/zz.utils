package zz.utils.ui.propertyeditors;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import zz.utils.properties.IRWProperty;
import zz.utils.properties.SimpleRWProperty;
import zz.utils.properties.PropertyUtils.Connector;
import zz.utils.properties.PropertyUtils.SimpleValueConnector;
import zz.utils.ui.GridStackLayout;
import zz.utils.ui.propertyeditors.SimplePropertyEditor.EditorNotFoundException;

/**
 * A property editor that is able to edit a set of properties of the same kind
 * @author gpothier
 *
 * @param <T>
 */
public class MultiPropertyEditor<T> extends JPanel
{
	private Field itsField;
	private List<IRWProperty<T>> itsProperties = new ArrayList<IRWProperty<T>>();
	private IRWProperty<T> itsMasterProperty;
	
	private SimplePropertyEditor<T> itsMasterEditor;
	private List<Connector<T>> itsConnectors = new ArrayList<Connector<T>>();
	private boolean itsConnected = false;
	
	private IRWProperty<Boolean> pEnabled = new SimpleRWProperty<Boolean>()
	{
		@Override
		protected void changed(Boolean aOldValue, Boolean aNewValue)
		{
			if (aNewValue)
			{
				if (itsMasterEditor != null) 
				{
					itsMasterEditor.setEnabled(true);
					connect();
				}
			}
			else
			{
				if (itsMasterEditor != null) 
				{
					itsMasterEditor.setEnabled(false);
					disconnect();
				}
			}
		}
	};
	
	public MultiPropertyEditor(Field aField, List<IRWProperty<T>> aProperties)
	{
		this(getPropertyClass(aField), aProperties);
		itsField = aField;
	}
	
	private static Class getPropertyClass(Field aField)
	{
		Type theType = aField.getGenericType();
		if (theType instanceof ParameterizedType)
		{
			ParameterizedType theParameterizedType = (ParameterizedType) theType;
			Type theRawType = theParameterizedType.getRawType();
			if ((theRawType instanceof Class) && (IRWProperty.class == theRawType))
			{
				Type[] theTypeArguments = theParameterizedType.getActualTypeArguments();
				return (Class) theTypeArguments[0];
			}
		}
		throw new IllegalArgumentException("Not a property");
	}
	
	public MultiPropertyEditor(Class<T> aClass, List<IRWProperty<T>> aProperties)
	{
		setLayout(new GridStackLayout(1));
		itsProperties = aProperties;

		// if all properties have the same value, enable the master editor
		T theValue = null;
		boolean theSameValue = true;
		for (IRWProperty<T> theProperty : aProperties)
		{
			if (theValue == null) theValue = theProperty.get();
			else if (! theValue.equals(theProperty.get()))
			{
				theSameValue = false;
				break;
			}
		}
		
		pEnabled.set(theSameValue);

		// add checkbox only if there is more than one property
		if (itsProperties.size() > 1)
		{
			add(new BooleanPropertyEditor(pEnabled));
		}

		// setup editor and connectors
		if (itsProperties.size() > 0) 
		{
			Iterator<IRWProperty<T>> theIterator = aProperties.iterator();
			
			itsMasterProperty = theIterator.next();
			itsMasterEditor = SimplePropertyEditor.createEditor(aClass, itsMasterProperty);
			add(itsMasterEditor);
			
			while(theIterator.hasNext())
			{
				IRWProperty<T> theProperty = theIterator.next();
				itsConnectors.add(new SimpleValueConnector<T>(itsMasterProperty, theProperty, false, true));
			}
		}

		itsMasterEditor.setEnabled(theSameValue);
		if (theSameValue) connect();
	}
	
	public Field getField()
	{
		return itsField;
	}
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		if (pEnabled.get()) connect();
	}
	
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		disconnect();
	}
	
	private void connect()
	{
		if (itsConnected) return;
		for (Connector<T> theConnector : itsConnectors) theConnector.connect();
		itsConnected = true;
	}

	private void disconnect()
	{
		if (! itsConnected) return;
		for (Connector<T> theConnector : itsConnectors) theConnector.disconnect();
		itsConnected = false;
	}
	
	/**
	 * Returns all the fields that correspond to public properties of the given object
	 */
	public static List<Field> getAvailableProperties(Object aObject)
	{
		List<Field> theResult = new ArrayList<Field>();
		Field[] theFields = aObject.getClass().getFields();
		for (Field theField : theFields)
		{
			if (! theField.getName().startsWith("p")) continue;
			Type theType = theField.getGenericType();
			if (theType instanceof ParameterizedType)
			{
				ParameterizedType theParameterizedType = (ParameterizedType) theType;
				Type theRawType = theParameterizedType.getRawType();
				if (! (theRawType instanceof Class)) continue;
				if (! (IRWProperty.class == theRawType)) continue;
				
				theResult.add(theField);
			}
		}
		
		return theResult;
	}
	
	/**
	 * Returns the available properties in the given collection of objects.
	 */
	public static Set<Field> getAvailableProperties(Collection<?> aObjects)
	{
		Set<Field> theResult = new HashSet<Field>();
		for (Object o : aObjects) theResult.addAll(getAvailableProperties(o));
		return theResult;
	}
	
	/**
	 * Returns the properties corresponding to the given property field in all the given
	 * objects, if available.
	 */
	@SuppressWarnings("unchecked")
	public static List<IRWProperty> getProperties(Field aProperty, Collection<?> aObjects)
	{
		List<IRWProperty> theResult = new ArrayList<IRWProperty>();
		for (Object o : aObjects)
		{
			IRWProperty theProperty = null;
			
			try { theProperty = (IRWProperty) aProperty.get(o); }
			catch (IllegalArgumentException e) { continue; }
			catch (IllegalAccessException e) { throw new RuntimeException(e); }
			
			if (theProperty != null) theResult.add(theProperty);
		}
		return theResult;
	}
	
	/**
	 * Creates a {@link MultiPropertyEditor} for each available property in the provided collection
	 * of objects.
	 */
	public static List<MultiPropertyEditor> createEditors(Collection<?> aObjects)
	{
		List<MultiPropertyEditor> theResult = new ArrayList<MultiPropertyEditor>();
		Set<Field> theProperties = getAvailableProperties(aObjects);
		for (Field theField : theProperties)
		{
			try
			{
				theResult.add(new MultiPropertyEditor(theField, getProperties(theField, aObjects)));
			}
			catch (EditorNotFoundException e)
			{
				continue;
			}
		}
		return theResult;
	}
}
