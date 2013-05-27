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
package de.rowlo.diffeclipse.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import de.rowlo.diffeclipse.model.tree.TreeModel;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class FeaturesAndPluginsViewerFilter extends ViewerFilter implements ActivatableViewerFilter {
	public static final String FEATURES = "features";
	public static final String PLUGINS = "plugins";

	private boolean active = false;

	public FeaturesAndPluginsViewerFilter() {
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (isActive()) {
			boolean isElementSelected = select(element);
			return isElementSelected;
		}
		return true;
	}

	protected boolean select(Object element) {
		if (element instanceof TreeModel) {
			TreeModel treeModel = (TreeModel) element;
			if (treeModel.parent != null) {
				TreeModel parentsParent = treeModel.parent.parent;
				if (parentsParent == null && !treeModel.isFile()) {
					String label = treeModel.getLabel();
					return FEATURES.equals(label) || PLUGINS.equals(label);
				} else if (parentsParent != null) {
					// any TreeModel deeper than first visible level
					return true;
				} // else {
				// other top level TreeModels we don't want to see
				// }
			}
		}
		return false;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
