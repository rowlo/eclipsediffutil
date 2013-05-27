/*******************************************************************************
 * Copyright (c) 2011 Robert Wloch and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Robert Wloch - initial API and implementation
 *******************************************************************************/
package de.rowlo.diffeclipse.event;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.rowlo.diffeclipse.controller.DiffEclipseFormController;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class TextLocationHandler extends AbstractTextModificationHandler {

	private final DiffEclipseFormController controller;

	public TextLocationHandler(Text text, DiffEclipseFormController controller) {
		super(text);
		this.controller = controller;
	}

	public DiffEclipseFormController getController() {
		return controller;
	}

	@Override
	public void modifyText(ModifyEvent me) {
		Text text = getText();
		if (text != null && text.equals(me.getSource()) && getController() != null) {
			Shell shell = text.getShell();
			Display display = shell.getDisplay();
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					// recalculate enabled state of sections
					getController().updateUI();
				}
			});
		}
	}

}
