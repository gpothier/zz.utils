/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import java.util.HashMap;
import java.util.Map;

import zz.utils.PublicCloneable;

/**
 * Maintains a relationship between {@link zz.utils.properties.PropertyId property ids}
 * and {@link zz.utils.properties.Property properties}.
 * Classes that need reified properties (ie., ability to retrieve a property given a property id)
 * can use this class as shown in {@link zz.utils.properties.Example}.
 * @author gpothier
 */
public class PropertyManager extends PublicCloneable
{
	private Map<PropertyId, Property> itsProperties =
		new HashMap<PropertyId, Property>();
	
	/**
	 * Retrieve the property that corresponds to the given id.
	 */ 
	public <T> Property<T> getProperty(PropertyId<T> aPropertyId)
	{
		return itsProperties.get(aPropertyId);
	}
	
	/**
	 * Registers a property.
	 * @return The same as the specified property
	 */
	public <T> Property<T> registerProperty (PropertyId<T> aId, Property<T> aProperty)
	{
		itsProperties.put(aId, aProperty);
		return aProperty;
	}
	
	/**
	 * Clones this property manager for usage in the specified container.
	 * All properties are cloned for the given container 
	 * (see {@link Property#cloneForContainer(Object)}.
	 * @param aContainer The container for the clone and its properties.
	 */
	public PropertyManager cloneForContainer (Object aContainer)
	{
		PropertyManager theClone = (PropertyManager) super.clone();
		
		theClone.itsProperties = new HashMap<PropertyId, Property>();
		for (Map.Entry<PropertyId, Property> theEntry : itsProperties.entrySet())
		{
			PropertyId theId = theEntry.getKey();
			Property theProperty = theEntry.getValue();
			
			theClone.itsProperties.put (theId, theProperty.cloneForContainer(aContainer));
		}
		
		return theClone;
	}
	
	public String toString()
	{
		StringBuffer theBuffer = new StringBuffer(super.toString());
		theBuffer.append("\n");
		
		for (Map.Entry<PropertyId, Property> theEntry : itsProperties.entrySet())
		{
			PropertyId theId = theEntry.getKey();
			Property theProperty = theEntry.getValue();
			
			theBuffer.append(" - ");
			theBuffer.append(theId.getName());
			theBuffer.append(": ");
			theBuffer.append(theProperty.get());
			theBuffer.append("\n");
		}
		
		return theBuffer.toString();
	}
}
