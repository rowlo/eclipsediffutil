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
package de.rowlo.diffeclipse.sorter;

import org.eclipse.jface.viewers.ViewerSorter;

import de.rowlo.diffeclipse.model.tree.TreeModel;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class TreeModelSorter extends ViewerSorter {

	public static final TreeModelSorter INSTANCE = createTreeModelSorter();

	protected TreeModelSorter() {
	}

	protected static TreeModelSorter createTreeModelSorter() {
		return new TreeModelSorter();
	}

	@Override
	public int category(Object element) {
		if (element instanceof TreeModel) {
			TreeModel treeModel = (TreeModel) element;
			if (treeModel.isFile()) {
				return 20;
			} else {
				return 10;
			}
		}
		return super.category(element);
	}
}
