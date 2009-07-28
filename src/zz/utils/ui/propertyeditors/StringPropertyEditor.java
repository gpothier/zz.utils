package zz.utils.ui.propertyeditors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;
import zz.utils.properties.IRWProperty;
import zz.utils.ui.StackLayout;

public class StringPropertyEditor extends SimplePropertyEditor<String>
implements IPropertyListener<String>, ActionListener, FocusListener
{
	private final JTextField itsTextField = new JTextField();
	
	public StringPropertyEditor(IRWProperty<String> aProperty)
	{
		super(aProperty);
		setLayout(new StackLayout());
		add(itsTextField);
		itsTextField.setText(getProperty().get());
		itsTextField.addActionListener(this);
		itsTextField.addFocusListener(this);
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
	
	public void propertyChanged(IProperty<String> aProperty, String aOldValue, String aNewValue)
	{
		itsTextField.setText(aNewValue);
	}
	
	public void propertyValueChanged(IProperty<String> aProperty)
	{
	}

	public void focusGained(FocusEvent aE)
	{
	}

	public void focusLost(FocusEvent aE)
	{
		getProperty().set(itsTextField.getText());
	}

	public void actionPerformed(ActionEvent aE)
	{
		getProperty().set(itsTextField.getText());
	}
}
