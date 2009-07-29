package zz.utils.ui.propertyeditors;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zz.utils.properties.IRWProperty;
import zz.utils.ui.StackLayout;

public class BooleanPropertyEditor {
	@SuppressWarnings("serial")
	public static class CheckBox extends SimplePropertyEditor<Boolean>
	implements ChangeListener
	{
		private JCheckBox itsCheckBox = new JCheckBox();
		
		public CheckBox(IRWProperty<Boolean> aProperty)
		{
			super(aProperty);
			setLayout(new StackLayout());
			itsCheckBox.setOpaque(false);
			add(itsCheckBox);
			itsCheckBox.addChangeListener(this);
		}
		
		public void stateChanged(ChangeEvent aE)
		{
			uiToProperty();
		}
		
		@Override
		protected void propertyToUi(Boolean aValue)
		{
			itsCheckBox.setSelected(aValue);
		}
	
		protected void uiToProperty()
		{
			getProperty().set(itsCheckBox.isSelected());
		}
	}
}
