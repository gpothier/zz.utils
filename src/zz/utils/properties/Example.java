/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * @author gpothier
 */
public class Example implements IPropertyContainer
{
	private PropertyManager itsManager = new PropertyManager(this);
	
	public static final PropertyId NAME = new PropertyId("name");
	public RWProperty<String> pName = itsManager.createRWProperty(NAME);
	
	
	public IPropertyManager getPropertyManager()
	{
		return itsManager;
	}
}
