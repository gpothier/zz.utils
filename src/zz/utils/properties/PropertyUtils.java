/*
 * Created on Jun 2, 2005
 */
package zz.utils.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Various utility methods for dealing with properties
 * @author gpothier
 */
public class PropertyUtils
{
	/**
	 * Clones the properties of the given object, ie. replaces
	 * all non-static property fields by a clone. Property values are cloned according
	 * to the value of {@link PropertyId#getCloneValues()} of each property.
	 */
	public static void cloneProperties (Object aPropertyHolder)
	{
		try
		{
			// We replace all the properties of the clone by cloned properties.
			for (Field theField : aPropertyHolder.getClass().getFields())
			{
				if (IProperty.class.isAssignableFrom(theField.getType())
					&& ! Modifier.isStatic(theField.getModifiers()))
				{
					theField.setAccessible(true);
					
					IProperty theOriginalProperty = (IProperty) theField.get(aPropertyHolder);
					IProperty theClonedProperty = theOriginalProperty.cloneForContainer(
							aPropertyHolder, 
							theOriginalProperty.getId().getCloneValues());
					
					theField.set(aPropertyHolder, theClonedProperty);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error cloning module", e);
		}
		
	}}
