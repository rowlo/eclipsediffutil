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
package de.rowlo.diffeclipse.folderscanner;

import de.rowlo.diffeclipse.model.tree.TreeModel;
import de.rowlo.diffeclipse.util.DiffEclipseUtil;

/**
 * This filter is a negative hit for any {@link TreeModel} whose file path is a
 * plain Eclipse file according to
 * {@link DiffEclipseUtil#isPlainEclipseFile(String)}.
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class NoPlainEclipseFilesEclipseFolderScannerFilter implements EclipseFolderScannerFilter {

	public static final String FEATURES = "features";
	public static final String PLUGINS = "plugins";

	@Override
	public boolean apply(TreeModel subject) {
		if (subject != null && (subject.isFile() || isFeaturesOrPlugins(subject.parent))) {
			String filePath = subject.getFilePath();
			String filePathWithoutSource = null;
			if (filePath != null && filePath.contains(".source_")) {
				filePathWithoutSource = filePath.replaceFirst("\\.source_", "_");
			}
			return !(DiffEclipseUtil.INSTANCE.isPlainEclipseFile(filePath) || DiffEclipseUtil.INSTANCE
					.isPlainEclipseFile(filePathWithoutSource));
		}
		return true;
	}

	protected boolean isFeaturesOrPlugins(TreeModel parent) {
		if (parent != null && parent.getLabel() != null) {
			String label = parent.getLabel();
			return PLUGINS.equals(label) || FEATURES.equals(label);
		}
		return false;
	}

}
