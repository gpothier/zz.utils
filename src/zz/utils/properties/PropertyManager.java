/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a relationship between {@link zz.utils.properties.PropertyId property ids}
 * and {@link zz.utils.properties.Property properties}.
 * Classes that need reified properties (ie., ability to retrieve a property given a property id)
 * can use this class as shown in {@link zz.utils.properties.Example}.
 * @author gpothier
 */
public class PropertyManager implements IPropertyManager
{
	private IPropertyContainer itsPropertyContainer;
	
	private Map<PropertyId, Property> itsProperties =
		new HashMap<PropertyId, Property>();
	
	/**
	 * Creates a new property manager for the specified property container.
	 */
	public PropertyManager(IPropertyContainer aPropertyContainer)
	{
		itsPropertyContainer = aPropertyContainer;
	}

	/**
	 * Retrieve the property that corresponds to the given id.
	 */
	public Property getProperty(PropertyId aPropertyId)
	{
		return itsProperties.get(aPropertyId);
	}
	
	/**
	 * Creates a new read-write property. This is intended for the property container,
	 * not for external clients.
	 * @param aId Id of the new property.
	 */
	public <T> RWProperty<T> createRWProperty (PropertyId aId)
	{
		RWProperty<T> theProperty = new RWProperty<T>(itsPropertyContainer, aId);
		itsProperties.put (aId, theProperty);
		return theProperty;
	}
	
	/**
	 * Creates a new read-only property. This is intended for the property container,
	 * not for external clients.
	 * @param aId Id of the new property.
	 */
	public <T> ROProperty<T> createROProperty (PropertyId aId)
	{
		ROProperty<T> theProperty = new ROProperty<T>(itsPropertyContainer, aId);
		itsProperties.put (aId, theProperty);
		return theProperty;
	}
}
