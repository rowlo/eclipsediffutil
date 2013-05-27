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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import de.rowlo.diffeclipse.model.tree.TreeModel;
import de.rowlo.diffeclipse.util.io.EclipseProductFileFilter;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class EclipseFolderScannerForTree extends Thread {
	private final AtomicBoolean shouldQuit = new AtomicBoolean();
	private Set<String> eclipseFiles = null;
	private String pathToEclipse = null;
	private final EclipseFolderScannerCaller scannerCaller;
	private final List<EclipseFolderScannerFilter> eclipseFolderScannerFilters = new ArrayList<EclipseFolderScannerFilter>();

	public EclipseFolderScannerForTree(final EclipseFolderScannerCaller textLocationEclipseHandler) {
		this.scannerCaller = textLocationEclipseHandler;
	}

	protected EclipseFolderScannerCaller getScannerCaller() {
		return scannerCaller;
	}

	protected String getPathToEclipse() {
		return pathToEclipse;
	}

	public void addEclipseFolderScannerFilter(EclipseFolderScannerFilter filter) {
		if (filter != null && !eclipseFolderScannerFilters.contains(filter)) {
			eclipseFolderScannerFilters.add(filter);
		}
	}

	public void removeEclipseFolderScannerFilter(EclipseFolderScannerFilter filter) {
		if (filter != null && eclipseFolderScannerFilters.contains(filter)) {
			eclipseFolderScannerFilters.remove(filter);
		}
	}

	public void quit() {
		shouldQuit.set(true);
	}

	@Override
	public void run() {
		shouldQuit.set(false);
		if (getScannerCaller() == null) {
			return;
		}
		Display display = getScannerCaller().getTreeViewer().getTree().getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				eclipseFiles = new HashSet<String>();
				pathToEclipse = getScannerCaller().getLocation();
				File file = new File(pathToEclipse);
				if (file != null && file.isDirectory()) {
					EclipseProductFileFilter filter = EclipseProductFileFilter.INSTANCE;
					File[] essentialFiles = file.listFiles(filter);
					boolean isEclipseProduct = filter.isEclipseProduct(essentialFiles);
					if (isEclipseProduct && !shouldQuit.get()) {
						scanEclipseProductFolder(file);
					}
				}
				getScannerCaller().notifyListeners(pathToEclipse, eclipseFiles);
			}

		});
		getScannerCaller().folderScannerFinished();
	}

	protected void scanEclipseProductFolder(File file) {
		TreeModel root = TreeModel.createRoot();

		if (!shouldQuit.get()) {
			makeTreeModel(file, root);
		}

		if (!shouldQuit.get()) {
			updateTreeViewerInput(root);
		}
	}

	protected void makeTreeModel(File parent, TreeModel parentModel) {
		File[] children = parent.listFiles();
		if (shouldQuit.get() || children == null) {
			return;
		}
		for (File child : children) {
			if (shouldQuit.get()) {
				return;
			}
			TreeModel childModel = createTreeModelForFile(child, parentModel);
			if (childModel != null) {
				parentModel.children.add(childModel);
				if (!childModel.isFile()) {
					makeTreeModel(child, childModel);
				}
			}
		}
	}

	protected TreeModel createTreeModelForFile(final File file, final TreeModel parent) {
		TreeModel result = null;
		if (file != null && file.exists()) {
			String name = file.getName();
			String filePath = file.getAbsolutePath();
			if (getPathToEclipse() != null) {
				String trimmedFilePath = filePath.substring(getPathToEclipse().length());
				eclipseFiles.add(trimmedFilePath);
			}
			boolean isFile = file.isFile();
			result = createTreeModel(name, filePath, isFile, parent);
		}
		result = applyFilter(result);
		return result;
	}

	protected TreeModel createTreeModel(String name, String filePath, boolean isFile, TreeModel parent) {
		return new TreeModel(name, filePath, isFile, parent);
	}

	protected TreeModel applyFilter(final TreeModel subject) {
		List<EclipseFolderScannerFilter> filters = new ArrayList<EclipseFolderScannerFilter>(
				eclipseFolderScannerFilters);
		for (EclipseFolderScannerFilter filter : filters) {
			if (!filter.apply(subject)) {
				return null;
			}
		}
		return subject;
	}

	protected void updateTreeViewerInput(final TreeModel input) {
		final TreeViewer treeViewer = scannerCaller.getTreeViewer();
		Tree tree = treeViewer.getTree();
		Shell shell = tree.getShell();
		Display display = shell.getDisplay();
		if (shouldQuit.get()) {
			return;
		}
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				treeViewer.setInput(input);
			}
		});
	}
}
