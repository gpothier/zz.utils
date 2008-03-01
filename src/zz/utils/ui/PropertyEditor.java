/*
 * Created on Oct 7, 2007
 */
package zz.utils.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;
import zz.utils.properties.IRWProperty;
import zz.utils.ui.OptionListener.Option;
import zz.utils.ui.popup.ButtonPopupComponent;

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
	
	/**
	 * Creates a {@link LabelTextFieldPropertyEditor} that edits the given property.
	 * The component displays a label, and enters editing mode when the label is double clicked.
	 */
	public static JComponent createLabelTextField(IRWProperty<String> aProperty)
	{
		return new LabelTextFieldPropertyEditor(aProperty);
	}
	
	public static <T> JComboBox createComboBox(IRWProperty<T> aProperty, T... aChoices)
	{
		return new ComboBoxPropertyEditor<T>(aProperty, aChoices);
	}
	
	public static <T extends Enum> JComboBox createEnumComboBox(IRWProperty<T> aProperty, Class<T> aClass)
	{
		return createComboBox(aProperty, aClass.getEnumConstants());
	}
	
	public static <T> JSpinner createNumberSpinner(IRWProperty<Integer> aProperty)
	{
		return new NumberSpinnerPropertyEditor(aProperty);
	}
	
	public static JComponent createColorChooser(IRWProperty<Color> aProperty)
	{
		return new ColorChooserPropertyEditor(aProperty);
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
	
	/**
	 * Similar to {@link TextFieldPropertyEditor} but by default displays a label,
	 * and only displays the text field when the label is double clicked.
	 * @author gpothier
	 */
	private static class LabelTextFieldPropertyEditor extends JPanel
	implements IPropertyListener<String>, ActionListener, FocusListener, MouseListener, KeyListener
	{
		private final IRWProperty<String> itsProperty;
		private JLabel itsLabel;
		private JTextField itsTextField;
		
		public LabelTextFieldPropertyEditor(IRWProperty<String> aProperty)
		{
			itsProperty = aProperty;
						
			itsLabel = new JLabel(itsProperty.get());
			itsLabel.addMouseListener(this);
			
			setLayout(new StackLayout());
			add(itsLabel);
		}
		
		private void startEditing()
		{
			itsTextField = new JTextField();
			itsTextField.setText(itsProperty.get());
			itsTextField.addActionListener(this);
			itsTextField.addFocusListener(this);
			itsTextField.addKeyListener(this);

			removeAll();
			add(itsTextField);
			revalidate();
			repaint();
		}
		
		private void commitEditing()
		{
			itsProperty.set(itsTextField.getText());
			stopEditing();
		}
		
		private void stopEditing()
		{
			itsTextField = null;
			
			removeAll();
			add(itsLabel);
			revalidate();
			repaint();
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
			if (itsTextField != null) itsTextField.setText(aNewValue);
			itsLabel.setText(aNewValue);
		}
		
		public void propertyValueChanged(IProperty<String> aProperty)
		{
		}
		
		public void focusGained(FocusEvent aE)
		{
		}
		
		public void focusLost(FocusEvent aE)
		{
			commitEditing();
		}
		
		public void actionPerformed(ActionEvent aE)
		{
			commitEditing();
		}

		public void mouseClicked(MouseEvent aE)
		{
			if (aE.getClickCount() == 2) startEditing();
		}

		public void mouseEntered(MouseEvent aE)
		{
		}

		public void mouseExited(MouseEvent aE)
		{
		}

		public void mousePressed(MouseEvent aE)
		{
		}

		public void mouseReleased(MouseEvent aE)
		{
		}

		public void keyPressed(KeyEvent aE)
		{
			if (aE.getKeyCode() == KeyEvent.VK_ESCAPE) stopEditing();
		}

		public void keyReleased(KeyEvent aE)
		{
		}

		public void keyTyped(KeyEvent aE)
		{
		}
	}
	
	private static class ComboBoxPropertyEditor<T> extends JComboBox
	implements ItemListener
	{
		private final IRWProperty<T> itsProperty;

		public ComboBoxPropertyEditor(IRWProperty<T> aProperty, T... aChoices)
		{
			itsProperty = aProperty;
			setModel(new DefaultComboBoxModel(aChoices));
			setSelectedItem(itsProperty.get());
			addItemListener(this);
		}

		public void itemStateChanged(ItemEvent aE)
		{
			itsProperty.set((T) getSelectedItem());
		}
	}

	private static class NumberSpinnerPropertyEditor extends JSpinner
	implements IPropertyListener<Integer>, ChangeListener, FocusListener
	{
		private final IRWProperty<Integer> itsProperty;
		
		public NumberSpinnerPropertyEditor(IRWProperty<Integer> aProperty)
		{
			super(new SpinnerNumberModel());
			itsProperty = aProperty;
			setValue(itsProperty.get());
			addChangeListener(this);
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
		
		public void propertyChanged(IProperty<Integer> aProperty, Integer aOldValue, Integer aNewValue)
		{
			setValue(aNewValue);
		}
		
		public void propertyValueChanged(IProperty<Integer> aProperty)
		{
		}

		public void focusGained(FocusEvent aE)
		{
		}

		public void focusLost(FocusEvent aE)
		{
			itsProperty.set((Integer) getValue());
		}

		public void stateChanged(ChangeEvent aE)
		{
			itsProperty.set((Integer) getValue());
		}
	}
	
	private static class ColorChooserPropertyEditor extends ButtonPopupComponent
	implements IPropertyListener<Color>, OptionListener
	{
		private final IRWProperty<Color> itsProperty;
		private ColorChooserPanel itsChooserPanel;

		public ColorChooserPropertyEditor(IRWProperty<Color> aProperty)
		{
			super(new JButton(" "));
			itsProperty = aProperty;
			getButton().setBackground(itsProperty.get());
			
			itsChooserPanel = new ColorChooserPanel();
			itsChooserPanel.addOptionListener(this);
			setPopupComponent(itsChooserPanel);
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

		private JButton getButton()
		{
			return (JButton) getContent();
		}

		public void propertyChanged(IProperty<Color> aProperty, Color aOldValue, Color aNewValue)
		{
			getButton().setBackground(aNewValue);
		}

		public void propertyValueChanged(IProperty<Color> aProperty)
		{
		}

		public void optionSelected(Option aOption)
		{
			if (aOption == Option.OK) itsProperty.set(itsChooserPanel.getSelectedColor());
			hidePopup();
		}
	}
}
