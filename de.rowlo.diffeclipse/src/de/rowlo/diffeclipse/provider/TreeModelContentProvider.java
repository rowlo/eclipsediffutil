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
package de.rowlo.diffeclipse.provider;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.rowlo.diffeclipse.model.tree.TreeModel;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class TreeModelContentProvider implements ITreeContentProvider {

	public static final TreeModelContentProvider INSTANCE = createTreeModelContentProvider();

	protected TreeModelContentProvider() {
	}

	protected static TreeModelContentProvider createTreeModelContentProvider() {
		return new TreeModelContentProvider();
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof TreeModel) {
			ArrayList<TreeModel> children = ((TreeModel) inputElement).children;
			return children.toArray();
		}
		return new Object[] {};
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	public Object getParent(Object element) {
		if (element instanceof TreeModel) {
			return ((TreeModel) element).parent;
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof TreeModel) {
			return ((TreeModel) element).children.size() > 0;
		}
		return false;
	}
}