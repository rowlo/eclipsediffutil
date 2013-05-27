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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.rowlo.diffeclipse.DiffEclipse;
import de.rowlo.diffeclipse.model.tree.TreeModel;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class TreeModelLabelProvider extends LabelProvider {

	public static final String FEATURES = "features";
	public static final String PLUGINS = "plugins";

	private static final String EXT_EXE = ".exe";

	private static final String EXT_JAR = ".jar";

	public static final TreeModelLabelProvider INSTANCE = createTreeModelLabelProvider();

	protected TreeModelLabelProvider() {
	}

	protected static TreeModelLabelProvider createTreeModelLabelProvider() {
		return new TreeModelLabelProvider();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof TreeModel) {
			TreeModel treeModel = (TreeModel) element;
			TreeModel parent = treeModel.parent;
			if (parent != null && parent.parent != null && parent.parent.parent == null
					&& FEATURES.equals(parent.getLabel())) {
				return DiffEclipse.getImage(DiffEclipse.IMAGE_FEATURE);
			}
			if (parent != null && parent.parent != null && parent.parent.parent == null
					&& PLUGINS.equals(parent.getLabel())) {
				return DiffEclipse.getImage(DiffEclipse.IMAGE_PLUGIN);
			}
			if (!treeModel.children.isEmpty()) {
				return DiffEclipse.getImage(DiffEclipse.IMAGE_FOLDER);
			}
			String name = treeModel.getLabel();
			if (name != null && name.toLowerCase().endsWith(EXT_JAR)) {
				return DiffEclipse.getImage(DiffEclipse.IMAGE_JAR);
			}
			if (name != null && name.toLowerCase().endsWith(EXT_EXE)) {
				return DiffEclipse.getImage(DiffEclipse.IMAGE_ECLIPSE);
			}
			return DiffEclipse.getImage(DiffEclipse.IMAGE_FILE);
		}

		return super.getImage(element);
	}
}
