/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.menus;

import java.awt.event.ActionEvent;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.menu.items.WindowEntry;
import org.homeunix.thecave.moss.swing.menu.MossMenu;
import org.homeunix.thecave.moss.swing.window.MossFrame;
import org.homeunix.thecave.moss.swing.window.ApplicationTracker;

/**
 * This is a special class which displays all open windows.  Can easily be adapted
 * for general use, assuming the GUI framework used keeps track of open windows
 * (see my Moss implementation of AbstractFrame, for instance).
 * @author wyatt
 *
 */
public class WindowMenu extends MossMenu {
	public static final long serialVersionUID = 0;

	public WindowMenu(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_WINDOW));
	}
	
	public void actionPerformed(ActionEvent e) {
		updateMenus();
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();
		
		this.removeAll();
		for (MossFrame targetFrame : ApplicationTracker.getInstance().getOpenFrames()) {
			this.add(new WindowEntry(getFrame(), targetFrame));
		}
	}
}