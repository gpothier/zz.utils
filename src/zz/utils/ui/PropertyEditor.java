/*
 * Created on Oct 7, 2007
 */
package zz.utils.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
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
	
	public static JTextField createTextField(IRWProperty<String> aProperty)
	{
		return createTextField(aProperty, null);
	}
	
	/**
	 * Creates a {@link JTextField} that edits the given property.
	 */
	public static JTextField createTextField(IRWProperty<String> aProperty, String aTooltip)
	{
		JTextField theTextField = new TextFieldPropertyEditor(aProperty);
		if (aTooltip != null) theTextField.setToolTipText(aTooltip);
		return theTextField;
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
	
	private static class TextFieldPropertyEditor extends JTextField
	implements IPropertyListener<String>, ActionListener, FocusListener
	{
		private final IRWProperty<String> itsProperty;
		
		public TextFieldPropertyEditor(IRWProperty<String> aProperty)
		{
			itsProperty = aProperty;
			setText(itsProperty.get());
			addActionListener(this);
			addFocusListener(this);
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
		
		public void propertyChanged(IProperty<String> aProperty, String aOldValue, String aNewValue)
		{
			setText(aNewValue);
		}
		
		public void propertyValueChanged(IProperty<String> aProperty)
		{
		}

		public void focusGained(FocusEvent aE)
		{
		}

		public void focusLost(FocusEvent aE)
		{
			itsProperty.set(getText());
		}

		public void actionPerformed(ActionEvent aE)
		{
			itsProperty.set(getText());
		}
	}
}
