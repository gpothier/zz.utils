/*
 * Created on Oct 7, 2007
 */
package zz.utils.ui;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;
import zz.utils.properties.IRWProperty;

/**
 * Provides various static methods to create simple property editors.
 * @author gpothier
 */
public class PropertyEditor
{
	/**
	 * Creates a checkbox that edits the value of the given property.
	 */
	public static JCheckBox createCheckBox(IRWProperty<Boolean> aProperty)
	{
		return createCheckBox(aProperty, null, null);
	}

	/**
	 * Creates a checkbox that edits the value of the given property.
	 * @param aLabel The text of the checkbox.
	 */
	public static JCheckBox createCheckBox(IRWProperty<Boolean> aProperty, String aLabel)
	{
		return createCheckBox(aProperty, aLabel, null);
	}
	
	public static JCheckBox createCheckBox(IRWProperty<Boolean> aProperty, String aLabel, String aTooltip)
	{
		JCheckBox theCheckBox = new CheckBoxPropertyEditor(aProperty);
		if (aLabel != null) theCheckBox.setText(aLabel);
		if (aTooltip != null) theCheckBox.setToolTipText(aTooltip);
		return theCheckBox;
	}
	
	private static class CheckBoxPropertyEditor extends JCheckBox
	implements IPropertyListener<Boolean>, ChangeListener
	{
		private final IRWProperty<Boolean> itsProperty;

		public CheckBoxPropertyEditor(IRWProperty<Boolean> aProperty)
		{
			itsProperty = aProperty;
			setSelected(itsProperty.get());
			addChangeListener(this);
		}
		
		@Override
		public void addNotify()
		{
			super.addNotify();
			itsProperty.addHardListener(this);
		}
		
		@Override
		public void removeNotify()
		{
			super.removeNotify();
			itsProperty.removeListener(this);
		}

		public void propertyChanged(IProperty<Boolean> aProperty, Boolean aOldValue, Boolean aNewValue)
		{
			setSelected(aNewValue);
		}

		public void propertyValueChanged(IProperty<Boolean> aProperty)
		{
		}

		public void stateChanged(ChangeEvent aE)
		{
			itsProperty.set(isSelected());
		}
	}
}
