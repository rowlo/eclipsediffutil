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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

import de.rowlo.diffeclipse.folderscanner.EclipseFolderScannerCaller;
import de.rowlo.diffeclipse.folderscanner.EclipseFolderScannerForTree;
import de.rowlo.diffeclipse.model.tree.TreeModel;
import de.rowlo.diffeclipse.provider.TreeModelContentProvider;
import de.rowlo.diffeclipse.provider.TreeModelLabelProvider;
import de.rowlo.diffeclipse.sorter.TreeModelSorter;
import de.rowlo.diffeclipse.util.io.EclipseProductFileFilter;

/**
 * The {@link TextLocationEclipseHandler} is essentially a
 * {@link ModifyListener} that can be installed on {@link Text} widgets. Upon a
 * change it'll:
 * <ol>
 * <li>attempt to resolve the text as a path to an Eclipse installation or an
 * Eclipse product</li>
 * <li>perform a full scan of that folder for all files and directories</li>
 * <li>create a {@link TreeModel} reflecting the files and directories</li>
 * <li>set the root node of the {@link TreeModel} as input to a
 * {@link TreeViewer}</li>
 * </ol>
 * 
 * @see TreeModelContentProvider
 * @see TreeModelLabelProvider
 * @see TreeModelSorter
 * @see EclipseProductFileFilter
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class TextLocationEclipseHandler extends AbstractTextModificationHandler implements EclipseFolderScannerCaller {

	private final TreeViewer treeViewer;
	private EclipseFolderScannerForTree folderScanner = null;
	private final List<FolderScannerListener> folderScannerListeners = new ArrayList<FolderScannerListener>();

	public TextLocationEclipseHandler(Text text, TreeViewer treeViewer) {
		super(text);
		this.treeViewer = treeViewer;
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	@Override
	public void modifyText(ModifyEvent me) {
		Text text = getText();
		if (text != null && text.equals(me.getSource()) && getTreeViewer() != null) {
			while (folderScanner != null) {
				folderScanner.quit();
				try {
					folderScanner.join();
				} catch (InterruptedException e) {
				}
			}
			folderScanner = new EclipseFolderScannerForTree(this);
			folderScanner.start();
		}
	}

	public void addFolderScannerListener(FolderScannerListener listener) {
		if (listener != null && !folderScannerListeners.contains(listener)) {
			folderScannerListeners.add(listener);
		}
	}

	public void removeFolderScannerListener(FolderScannerListener listener) {
		if (listener != null && folderScannerListeners.contains(listener)) {
			folderScannerListeners.remove(listener);
		}
	}

	public void notifyListeners(String folderLocation, Set<String> eclipseFiles) {
		for (FolderScannerListener listener : folderScannerListeners) {
			FolderScannedEvent event = new FolderScannedEvent(this, eclipseFiles, folderLocation);
			listener.onFolderScanned(event);
		}
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
		return (getText() != null) ? getText().getText() : "";
	}

}
