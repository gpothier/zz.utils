/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * @author gpothier
 */
public class Example 
{
	private PropertyManager itsManager = new PropertyManager();
	
	public static final PropertyId<String> NAME = new PropertyId<String>("name");
	public Property<String> pName = itsManager.registerProperty(
			NAME, 
			new StdRWProperty<String>(NAME));
	
	
}
