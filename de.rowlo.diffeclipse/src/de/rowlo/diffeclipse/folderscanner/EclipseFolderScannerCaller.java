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

import java.util.Set;

import org.eclipse.jface.viewers.TreeViewer;

import de.rowlo.diffeclipse.model.tree.TreeModel;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public interface EclipseFolderScannerCaller {

	/**
	 * @return the location of the folder to be scanned; not null
	 */
	String getLocation();

	/**
	 * Calling back the caller to notify about the scan result.
	 * 
	 * @param pathToEclipse
	 * @param eclipseFiles
	 */
	void notifyListeners(String pathToEclipse, Set<String> eclipseFiles);

	/**
	 * Calling back the caller to notify that the scanner is about to finish.
	 */
	void folderScannerFinished();

	/**
	 * @return the {@link TreeViewer} widget on which to set the new
	 *         {@link TreeModel} as input.
	 */
	TreeViewer getTreeViewer();

}
