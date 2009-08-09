package zz.utils.ui.propertyeditors;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zz.utils.properties.IRWProperty;
import zz.utils.ui.StackLayout;
import zz.utils.undo2.UndoStack;

public class BooleanPropertyEditor {
	@SuppressWarnings("serial")
	public static class CheckBox extends SimplePropertyEditor<Boolean>
	implements ChangeListener
	{
		private JCheckBox itsCheckBox = new JCheckBox();
		
		public CheckBox(UndoStack aUndoStack, IRWProperty<Boolean> aProperty)
		{
			super(aUndoStack, aProperty);
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
			boolean theNewValue = itsCheckBox.isSelected();
			if (theNewValue != getProperty().get())
			{
				startOperation();
				getProperty().set(theNewValue);
				commitOperation();
			}
		}
	}
}
