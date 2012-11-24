package zz.utils.ui.mvc.view.awt;

import java.awt.PopupMenu;

import zz.utils.ui.mvc.model.ActionModel;

public class PopupMenuView extends PopupMenu {
	public void add(ActionModel item) {
		add(new MenuItemView(item));
	}
}