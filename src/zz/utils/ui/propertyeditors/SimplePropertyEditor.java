package zz.utils.ui.propertyeditors;

import javax.swing.JPanel;

import zz.utils.properties.IRWProperty;

public class SimplePropertyEditor<T> extends JPanel
{
	private IRWProperty<T> mProperty;

	public SimplePropertyEditor(IRWProperty<T> aProperty)
	{
		mProperty = aProperty;
	}
	
	public IRWProperty<T> getProperty()
	{
		return mProperty;
	}
	
	public static <T> SimplePropertyEditor<T> createEditor(Class<T> aClass, IRWProperty<T> aProperty)
	{
		if (aClass == Float.class) return (SimplePropertyEditor<T>) new FloatPropertyEditor((IRWProperty<Float>) aProperty);
		else if (aClass == Boolean.class) return (SimplePropertyEditor<T>) new BooleanPropertyEditor((IRWProperty<Boolean>) aProperty);
		else throw new EditorNotFoundException(aClass);
	}
	
	public static class EditorNotFoundException extends RuntimeException
	{
		public EditorNotFoundException(Class aClass)
		{
			super(aClass.getName());
		}
	}
}
