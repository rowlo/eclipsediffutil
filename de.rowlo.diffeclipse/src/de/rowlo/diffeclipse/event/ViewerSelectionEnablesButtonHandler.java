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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Button;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class ViewerSelectionEnablesButtonHandler implements ISelectionChangedListener {

	private final Viewer viewer;
	private final Button button;

	public ViewerSelectionEnablesButtonHandler(Viewer viewer, Button button) {
		this.viewer = viewer;
		this.button = button;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (getViewer() != null && getButton() != null) {
			ISelection selection = getViewer().getSelection();
			boolean activate = (selection != null && !selection.isEmpty());
			getButton().setEnabled(activate);
		}
	}

	public Viewer getViewer() {
		return viewer;
	}

	public Button getButton() {
		return button;
	}

}
