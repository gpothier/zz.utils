package zz.utils.ui.mvc.view.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import zz.utils.Utils;
import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;
import zz.utils.ui.UniversalRenderer;
import zz.utils.ui.mvc.model.ChoicePropertyModel;
import zz.utils.ui.propertyeditors.PropertyComboBoxModel;

public class JComboBoxView<T> extends JComboBox<T>
{
	private ChoicePropertyModel<T> model;
	
	private final IPropertyListener<T> valueListener = new IPropertyListener<T>() {
		@Override
		public void propertyChanged(IProperty<T> aProperty, T aOldValue, T aNewValue) {
			if (! Utils.equalOrBothNull(getSelectedItem(), aNewValue)) setSelectedItem(aNewValue);
		}
	};
	
	private final IPropertyListener<Boolean> enabledListener = new IPropertyListener<Boolean>() {
		@Override
		public void propertyChanged(IProperty<Boolean> aProperty, Boolean aOldValue, Boolean aNewValue) {
			setEnabled(aNewValue);
		}
	};
	
	private final IPropertyListener<Boolean> validListener = new IPropertyListener<Boolean>() {
		@Override
		public void propertyChanged(IProperty<Boolean> aProperty, Boolean aOldValue, Boolean aNewValue) {
			setValid(aNewValue);
		}
	};
	
	private final ActionListener actionListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent aE)
		{
			model.pValue.set((T) getSelectedItem());
		}
	};
	
	public JComboBoxView(ChoicePropertyModel<T> model) 
	{
		setModel(new PropertyComboBoxModel<T>(model.pChoices, model.pValue, true));
		this.model = model;
		setEnabled(model.pEnabled.get());
		setValid(model.pValid.get());
		setSelectedItem(model.pValue.get());
		model.pEnabled.addListener(enabledListener);
		model.pValid.addListener(validListener);
		model.pValue.addListener(valueListener);
		
		addActionListener(actionListener);
	}
	
	private void setValid(boolean valid) {
		repaint();
	}

	protected String format(T value) {
		return ""+value;
	}
	
	@Override
	protected void paintComponent(Graphics g0)
	{
		Graphics2D g = (Graphics2D) g0;
		super.paintComponent(g);
		if (! model.pValid.get()) {
			g.setColor(Color.PINK);
			Composite oldComposite = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setComposite(oldComposite);
		}
	}
}
