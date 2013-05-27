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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import de.rowlo.diffeclipse.controller.DiffEclipseFormController;
import de.rowlo.diffeclipse.filter.ActivatableViewerFilter;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public final class HandlerFactory {

	private HandlerFactory() {
	}

	public static ButtonQuitHandler createButtonQuitHandler(Button button) {
		return new ButtonQuitHandler(button);
	}

	public static SelectionListener createButtonExtractMissingExtensionsHandler(Button button, Text text, Viewer viewer) {
		return new ButtonExtractMissingExtensionsHandler(button, text, viewer);
	}

	public static ButtonChooseLocationHandler createButtonChooseLocationHandler(Button button, Text text) {
		return new ButtonChooseLocationHandler(button, text);
	}

	public static TextLocationHandler createTextLocationHandler(Text text, DiffEclipseFormController controller) {
		return new TextLocationHandler(text, controller);
	}

	public static TextLocationEclipseHandler createTextLocationEclipseHandler(Text text, TreeViewer treeViewer) {
		return new TextLocationEclipseHandler(text, treeViewer);
	}

	public static SelectionListener createViewerFilterActivationButtonHandler(Button button,
			ActivatableViewerFilter viewerFilter) {
		return new ViewerFilterActivationButtonHandler(button, viewerFilter);
	}

	public static DiffUtilFileListHandler createDiffUtilFileListHandler(TreeViewer treeViewer) {
		return new DiffUtilFileListHandler(treeViewer);
	}

	public static ISelectionChangedListener createViewerSelectionEnablesButtonHandler(TreeViewer treeViewer,
			Button button) {
		return new ViewerSelectionEnablesButtonHandler(treeViewer, button);
	}
}
