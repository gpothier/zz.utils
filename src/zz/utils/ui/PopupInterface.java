/*
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Dec 7, 2001
 * Time: 4:59:05 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package zz.utils.ui;

import javax.swing.*;


/**
 * This interface can be implemented by components that are to be used as the popup
 * in a PopupComponent, or through the {@link PopupManager}.
 * It allows the popup to access the {@link PopupManager.Popup}.
 * This is useful if the popup itself needs to close the popup window.
 */
public interface PopupInterface
{
	public void setContext (PopupManager.Popup aPopup, JFrame aFrame);
}
