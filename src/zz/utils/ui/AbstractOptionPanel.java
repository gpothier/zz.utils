/**
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Apr 22, 2003
 * Time: 4:49:35 PM
 */
package zz.utils.ui;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public abstract class AbstractOptionPanel extends JPanel
{
	private JButton itsOKButton;
	private JButton itsCancelButton;
	private List itsOptionListeners = new ArrayList ();
	private JPanel itsButtonsPanel;

	private JComponent itsComponent;

	public AbstractOptionPanel()
	{
		createUI();
	}

	/**
	 * This abstract method create and returns the main component of the panel.
	 * Implementations should not rely
	 * on fields initialized in the declaration, as they are initialized
	 * after this method is called.
	 * <p>
	 * Note that this method will be called only once.
	 */
	protected abstract JComponent createComponent ();

	private void createUI ()
	{
		setLayout(new BorderLayout ());
		setBorder (BorderFactory.createEtchedBorder());

		itsButtonsPanel = new JPanel();
		setComponent(createComponent());


		itsOKButton = new JButton ("OK");
		itsOKButton.addActionListener(new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				ok ();
			}
		});
		itsCancelButton = new JButton ("Cancel");
		itsCancelButton.addActionListener(new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				cancel ();
			}
		});
		itsButtonsPanel.add (itsOKButton);
		itsButtonsPanel.add (itsCancelButton);

		add (itsButtonsPanel, BorderLayout.SOUTH);
	}

	protected void setComponent (JComponent aComponent)
	{
		if (itsComponent != null) remove(itsComponent);
		itsComponent = aComponent;
		if (itsComponent != null) add (itsComponent, BorderLayout.CENTER);
	}

	/**
	 * Adds the specified component to the buttons panel.
	 * Should be used by subclasses within the {@link #createComponent} method.
	 */
	protected void addToButtonsPanel (JComponent aComponent)
	{
		itsButtonsPanel.add (aComponent);
	}

	public void addOptionListener (OptionListener aListener)
	{
		itsOptionListeners.add (aListener);
	}

	public void removeOptionListener (OptionListener aListener)
	{
		itsOptionListeners.remove (aListener);
	}

	protected void fireOptionSelected (OptionListener.Option aOption)
	{
		for (Iterator theIterator = itsOptionListeners.iterator (); theIterator.hasNext ();)
		{
			OptionListener theListener = (OptionListener) theIterator.next ();
			theListener.optionSelected(aOption);
		}
	}

	protected void ok ()
	{
		fireOptionSelected(OptionListener.Option.OK);
	}

	protected void cancel ()
	{
		fireOptionSelected(OptionListener.Option.CANCEL);
	}
}
