package zz.utils.ui.propertyeditors;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;
import zz.utils.properties.IRWProperty;
import zz.utils.ui.StackLayout;

public class BooleanPropertyEditor {
	public static class CheckBox extends SimplePropertyEditor<Boolean>
	implements IPropertyListener<Boolean>, ChangeListener
	{
		private JCheckBox itsCheckBox = new JCheckBox();
		
		public CheckBox(IRWProperty<Boolean> aProperty)
		{
			super(aProperty);
			setLayout(new StackLayout());
			itsCheckBox.setOpaque(false);
			add(itsCheckBox);
			itsCheckBox.setSelected(getProperty().get());
			itsCheckBox.addChangeListener(this);
		}
		
		@Override
		public void addNotify()
		{
			super.addNotify();
			getProperty().addHardListener(this);
		}
		
		@Override
		public void removeNotify()
		{
			super.removeNotify();
			getProperty().removeListener(this);
		}
	
		public void propertyChanged(IProperty<Boolean> aProperty, Boolean aOldValue, Boolean aNewValue)
		{
			itsCheckBox.setSelected(aNewValue);
		}
	
		public void propertyValueChanged(IProperty<Boolean> aProperty)
		{
		}
	
		public void stateChanged(ChangeEvent aE)
		{
			getProperty().set(itsCheckBox.isSelected());
		}
	}
}
