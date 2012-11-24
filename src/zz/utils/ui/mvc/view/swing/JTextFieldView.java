package zz.utils.ui.mvc.view.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;
import zz.utils.ui.mvc.model.PropertyModel;

public class JTextFieldView extends JTextField
{
	private PropertyModel<String> model;
	
	private final IPropertyListener<String> valueListener = new IPropertyListener<String>() {
		@Override
		public void propertyChanged(IProperty<String> aProperty, String aOldValue, String aNewValue) {
			setText(aNewValue);
		}
	};
	
	private final IPropertyListener<Boolean> enabledListener = new IPropertyListener<Boolean>() {
		@Override
		public void propertyChanged(IProperty<Boolean> aProperty, Boolean aOldValue, Boolean aNewValue) {
			setEnabled(aNewValue);
		}
	};
	
	private final FocusListener focusListener = new FocusListener()
	{
		@Override
		public void focusLost(FocusEvent aE)
		{
			model.pValue.set(getText());
		}
		
		@Override
		public void focusGained(FocusEvent aE)
		{
		}
	};
	
	private final ActionListener actionListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent aE)
		{
			model.pValue.set(getText());
		}
	};
	
	public JTextFieldView(PropertyModel<String> model) {
		super(model.pValue.get());
		this.model = model;
		setEnabled(model.pEnabled.get());
		model.pEnabled.addListener(enabledListener);
		model.pValue.addListener(valueListener);
		addActionListener(actionListener);
		addFocusListener(focusListener);
	}

}
