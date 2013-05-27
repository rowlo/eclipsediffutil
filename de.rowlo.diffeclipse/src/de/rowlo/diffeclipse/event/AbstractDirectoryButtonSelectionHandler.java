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

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class AbstractDirectoryButtonSelectionHandler extends AbstractButtonSelectionHandler {
	private static String lastChoosenPath = null;
	private final Text text;

	public AbstractDirectoryButtonSelectionHandler(Button button, Text text) {
		super(button);
		this.text = text;
	}

	public Text getText() {
		return text;
	}

	public static final synchronized void setLastChoosenPath(String lastChoosenPath) {
		AbstractDirectoryButtonSelectionHandler.lastChoosenPath = lastChoosenPath;
	}

	public static final synchronized String getLastChoosenPath() {
		return lastChoosenPath;
	}

}
