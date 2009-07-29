package zz.utils.ui.propertyeditors;

import java.lang.reflect.Constructor;

import javax.swing.JPanel;

import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;
import zz.utils.properties.IRWProperty;

@SuppressWarnings("serial")
public abstract class SimplePropertyEditor<T> extends JPanel
implements IPropertyListener<T>
{
	private IRWProperty<T> itsProperty;

	public SimplePropertyEditor(IRWProperty<T> aProperty)
	{
		itsProperty = aProperty;
	}
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		propertyToUi(itsProperty.get());
		getProperty().addHardListener(this);
	}
	
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		getProperty().removeListener(this);
	}

	public IRWProperty<T> getProperty()
	{
		return itsProperty;
	}
	
	
	
	public void setProperty(IRWProperty<T> aProperty)
	{
		uiToProperty();
		itsProperty = aProperty;
		propertyToUi(aProperty.get());
	}

	public void propertyChanged(IProperty<T> aProperty, T aOldValue, T aNewValue)
	{
		propertyToUi(aNewValue);
	}

	protected abstract void propertyToUi(T aValue);
	protected abstract void uiToProperty();
	
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
