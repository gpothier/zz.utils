package zz.utils.ui.mvc.view.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;
import zz.utils.ui.mvc.model.PropertyModel;

public class JPasswordFieldView extends JPasswordField
{
	private PropertyModel<String> model;
	
	private final IPropertyListener<String> valueListener = new IPropertyListener<String>() {
		@Override
		public void propertyChanged(IProperty<String> aProperty, String aOldValue, String aNewValue) {
			if (! getText().equals(aNewValue)) setText(aNewValue);
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
	
	private final DocumentListener documentListener = new DocumentListener()
	{
		@Override
		public void removeUpdate(DocumentEvent aE)
		{
			model.pValue.set(getText());
		}
		
		@Override
		public void insertUpdate(DocumentEvent aE)
		{
			model.pValue.set(getText());
		}
		
		@Override
		public void changedUpdate(DocumentEvent aE)
		{
			model.pValue.set(getText());
		}
	};
	
	public JPasswordFieldView(PropertyModel<String> model)
	{
		this(model, false);
	}
	
	public JPasswordFieldView(PropertyModel<String> model, boolean updateOnTyping) 
	{
		super(model.pValue.get());
		this.model = model;
		setEnabled(model.pEnabled.get());
		setColumns(20);
		model.pEnabled.addListener(enabledListener);
		model.pValue.addListener(valueListener);
		addFocusListener(focusListener);
		
		if (updateOnTyping) getDocument().addDocumentListener(documentListener);
		else addActionListener(actionListener);
	}

}
