/**
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Jun 20, 2003
 * Time: 10:55:05 AM
 */
package zz.utils.ui;

import zz.utils.SimpleListModel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JFontChooser extends JPanel implements ListSelectionListener, ActionListener, ItemListener
{
	private JList itsFontsList;
	private JComboBox itsSizeCombo;
	private JCheckBox itsItalicCheckBox;
	private JCheckBox itsBoldCheckBox;
	private JTextField itsPreviewTextField;

	private Map itsFontFamiliesMap = new HashMap ();

	private boolean itsUpdateEnabled = true;

	/**
	 * The currently constructed font. Takes into account the font family, size, and attributes.
	 */
	private Font itsFont;

	public JFontChooser ()
	{
		createUI ();
	}

	private void createUI ()
	{

		// Init fonts list
		String[] theFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		for (int i = 0; i < theFontFamilyNames.length; i++)
		{
			String theFontFamilyName = theFontFamilyNames[i];
			Font theFont = new Font(theFontFamilyName, Font.PLAIN, 14);
			itsFontFamiliesMap.put (theFontFamilyName, theFont);
		}

		itsFontsList = new JList(theFontFamilyNames)
		{
			protected void paintComponent (Graphics g)
			{
				Graphics2D theGraphics = (Graphics2D) g;
				theGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent (g);
			}
		};
		itsFontsList.setCellRenderer(new FontRenderer());
		itsFontsList.getSelectionModel().addListSelectionListener(this);

		// Init sizes combo
		int[] theSizes = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 24, 28, 32, 40, 48, 56, 64, 70};
		Integer[] theIntegerSizes = new Integer[theSizes.length];
		for (int i = 0; i < theSizes.length; i++)
		{
			int theSize = theSizes[i];
			theIntegerSizes[i] = new Integer (theSize);
		}

		itsSizeCombo = new JComboBox(theIntegerSizes);
		itsSizeCombo.addActionListener(this);
		itsSizeCombo.addItemListener(this);

		// Init checkboxes
		itsItalicCheckBox = new JCheckBox("<html><i>Italic</i></html>")
		{
			protected void paintComponent (Graphics g)
			{
				Graphics2D theGraphics = (Graphics2D) g;
				theGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent (g);
			}
		};

		itsItalicCheckBox.addActionListener(this);

		itsBoldCheckBox = new JCheckBox("<html><b>Bold</b></html>")
		{
			protected void paintComponent (Graphics g)
			{
				Graphics2D theGraphics = (Graphics2D) g;
				theGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent (g);
			}
		};

		itsBoldCheckBox.addActionListener(this);

		// Init preview
		itsPreviewTextField = new JTextField("Type in a text!")
		{
			protected void paintComponent (Graphics g)
			{
				Graphics2D theGraphics = (Graphics2D) g;
				theGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent (g);
			}
		};


		// Setup layout
		setLayout (new BorderLayout());

		JPanel theCheckBoxPanel = new JPanel(new GridStackLayout(1));
		theCheckBoxPanel.add (itsBoldCheckBox);
		theCheckBoxPanel.add (itsItalicCheckBox);

		JPanel theCenterPanel = new JPanel();
		theCenterPanel.add (new JScrollPane (itsFontsList));
		theCenterPanel.add (itsSizeCombo);
		theCenterPanel.add (theCheckBoxPanel);

		add (theCenterPanel, BorderLayout.CENTER);
		add (itsPreviewTextField, BorderLayout.SOUTH);
	}

	public void valueChanged (ListSelectionEvent e)
	{
		fontChanged();
	}

	public void actionPerformed (ActionEvent e)
	{
		fontChanged();
	}

	public void itemStateChanged(ItemEvent e)
	{
		fontChanged();
	}

	private void fontChanged ()
	{
		if (! itsUpdateEnabled) return;

		Font theFamilyFont = (Font) itsFontFamiliesMap.get (itsFontsList.getSelectedValue());
		boolean theItalic = itsItalicCheckBox.isSelected();
		boolean theBold = itsBoldCheckBox.isSelected();
		Integer theSize = (Integer) itsSizeCombo.getSelectedItem();

		int theStyle = (theItalic ? Font.ITALIC : 0) | (theBold ? Font.BOLD : 0);

		itsFont = theFamilyFont.deriveFont(theStyle, theSize.floatValue());
		itsPreviewTextField.setFont(getSelectedFont());
	}

	public Font getSelectedFont ()
	{
		return itsFont;
	}

	public void setSelectedFont (Font aFont)
	{
		itsFont = aFont;

		String theFamilyName = aFont.getName();
		int theSize = aFont.getSize();
		int theStyle = aFont.getStyle();
		boolean theItalic = (theStyle & Font.ITALIC) != 0;
		boolean theBold = (theStyle & Font.BOLD) != 0;

		itsUpdateEnabled = false;
		itsFontsList.setSelectedValue(theFamilyName, true);
		itsSizeCombo.setSelectedItem(new Integer (theSize));
		itsItalicCheckBox.setSelected(theItalic);
		itsBoldCheckBox.setSelected(theBold);
		itsPreviewTextField.setFont(aFont);
		itsUpdateEnabled = true;
	}

	private class FontRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			JLabel theLabel = (JLabel) super.getListCellRendererComponent (list, value, index, isSelected, cellHasFocus);
			Font theFont = (Font) itsFontFamiliesMap.get (value);
			theLabel.setFont(theFont);
			theLabel.setText(theFont.getName());
			return theLabel;
		}
	}

}
