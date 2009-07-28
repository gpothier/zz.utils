package zz.utils.ui.propertyeditors;

import java.lang.reflect.Constructor;

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
	
	public static <T> SimplePropertyEditor<T> createEditor(Class<? extends SimplePropertyEditor<T>> aEditorClass, IRWProperty<T> aProperty)
	{
		try
		{
			Constructor<? extends SimplePropertyEditor<T>> theConstructor = aEditorClass.getConstructor(IRWProperty.class);
			return theConstructor.newInstance(aProperty);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static <T> Class<? extends SimplePropertyEditor<T>> getDefaultEditorClass(Class<T> aClass)
	{
		if (aClass == Float.class) return (Class) FloatPropertyEditor.LogSlider.class;
		else if (aClass == Boolean.class) return (Class) BooleanPropertyEditor.CheckBox.class;
		else if (aClass == String.class) return (Class) StringPropertyEditor.TextField.class;
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
