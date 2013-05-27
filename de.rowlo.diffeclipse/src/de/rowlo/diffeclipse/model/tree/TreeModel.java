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
package de.rowlo.diffeclipse.model.tree;

import java.util.ArrayList;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class TreeModel {
	public final TreeModel parent;
	public final ArrayList<TreeModel> children = new ArrayList<TreeModel>();
	private final String label;
	private final String filePath;
	private final boolean isFile;

	public TreeModel(String label, String filePath, boolean isFile, TreeModel parent) {
		this.parent = parent;
		this.label = label;
		this.filePath = filePath;
		this.isFile = isFile;
	}

	public static TreeModel createRoot() {
		return new TreeModel("", "", false, null);
	}

	@Override
	public String toString() {
		return getLabel();
	}

	public String getLabel() {
		return label;
	}

	public String getFilePath() {
		return filePath;
	}

	public boolean isFile() {
		return isFile;
	}
}