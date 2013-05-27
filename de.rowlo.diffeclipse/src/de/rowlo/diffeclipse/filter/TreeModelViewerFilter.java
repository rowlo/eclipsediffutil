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
import de.rowlo.diffeclipse.util.DiffEclipseUtil;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class TreeModelViewerFilter extends ViewerFilter implements ActivatableViewerFilter {

	private boolean active = false;

	public TreeModelViewerFilter() {
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
			if (treeModel.isFile()) {
				String filePath = treeModel.getFilePath();
				boolean isPlainEclipseFile = DiffEclipseUtil.INSTANCE.isPlainEclipseFile(filePath);
				boolean isExtendedEclipseFile = DiffEclipseUtil.INSTANCE.isExtendedEclipseFile(filePath);
				return !(isPlainEclipseFile && isExtendedEclipseFile);
			} else {
				boolean isAnyChildSelected = false;
				for (TreeModel child : treeModel.children) {
					isAnyChildSelected = isAnyChildSelected || select(child);
				}
				return isAnyChildSelected;
			}
		}
		return true;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
