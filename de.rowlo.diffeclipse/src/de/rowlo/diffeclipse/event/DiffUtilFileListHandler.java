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

import java.util.Set;

import org.eclipse.jface.viewers.TreeViewer;

import de.rowlo.diffeclipse.folderscanner.EclipseFolderScannerCaller;
import de.rowlo.diffeclipse.folderscanner.EclipseFolderScannerForTree;
import de.rowlo.diffeclipse.folderscanner.NoPlainEclipseFilesEclipseFolderScannerFilter;
import de.rowlo.diffeclipse.model.tree.TreeModel;
import de.rowlo.diffeclipse.util.DiffEclipseUtil;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class DiffUtilFileListHandler implements DiffUtilFileListListener, EclipseFolderScannerCaller {

	private final TreeViewer treeViewer;
	private EclipseFolderScannerForTree folderScanner = null;

	public DiffUtilFileListHandler(final TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	@Override
	public void onFileListChange(DiffUtilFileListEvent event) {
		if (event != null) {
			while (folderScanner != null) {
				folderScanner.quit();
				try {
					folderScanner.join();
				} catch (InterruptedException e) {
				}
			}
			folderScanner = new EclipseFolderScannerForTree(this) {
				@Override
				protected TreeModel createTreeModel(String name, String filePath, boolean isFile, TreeModel parent) {
					if (getPathToEclipse() != null) {
						filePath = filePath.substring(getPathToEclipse().length());
					}
					return super.createTreeModel(name, filePath, isFile, parent);
				}
			};
			NoPlainEclipseFilesEclipseFolderScannerFilter filter = new NoPlainEclipseFilesEclipseFolderScannerFilter();
			folderScanner.addEclipseFolderScannerFilter(filter);
			folderScanner.start();
		}
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	public void notifyListeners(String folderLocation, Set<String> eclipseFiles) {
	}

	/**
	 * MUST NOT be called by anyone else but an
	 * {@link EclipseFolderScannerForTree}!
	 */
	public void folderScannerFinished() {
		folderScanner = null;
	}

	@Override
	public String getLocation() {
		String location = DiffEclipseUtil.INSTANCE.getExtendedEclipseLocation();
		return (location != null) ? location : "";
	}
}
