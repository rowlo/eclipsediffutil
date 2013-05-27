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

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import de.rowlo.diffeclipse.filter.ActivatableViewerFilter;
import de.rowlo.diffeclipse.util.TreeModelFilterUtil;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class ViewerFilterActivationButtonHandler extends AbstractButtonSelectionHandler {

	private final ActivatableViewerFilter viewerFilter;

	protected ViewerFilterActivationButtonHandler(Button button, ActivatableViewerFilter viewerFilter) {
		super(button);
		this.viewerFilter = viewerFilter;
	}

	@Override
	public void widgetSelected(SelectionEvent se) {
		super.widgetSelected(se);
		if (getButton() != null && getButton().equals(se.getSource())) {
			ActivatableViewerFilter viewerFilter = getViewerFilter();
			if (viewerFilter != null) {
				boolean active = getButton().getSelection();
				viewerFilter.setActive(active);
				TreeModelFilterUtil.INSTANCE.filterChanged(viewerFilter);
			}
		}
	}

	public ActivatableViewerFilter getViewerFilter() {
		return viewerFilter;
	}

}
