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
package de.rowlo.diffeclipse.util;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;

import de.rowlo.diffeclipse.filter.ActivatableViewerFilter;
import de.rowlo.diffeclipse.filter.FeaturesAndPluginsViewerFilter;
import de.rowlo.diffeclipse.filter.TreeModelViewerFilter;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public final class TreeModelFilterUtil {

	public static final TreeModelFilterUtil INSTANCE = new TreeModelFilterUtil();
	private TreeModelViewerFilter plainEclipseViewerFilter = null;
	private FeaturesAndPluginsViewerFilter missingExtensionsViewerFilter = null;
	private TreeModelViewerFilter extendedEclipseViewerFilter = null;
	private TreeViewer viewerPlainEclipse = null;
	private TreeViewer viewerMissingExtensions = null;
	private TreeViewer viewerExtendedEclipse = null;

	protected TreeModelFilterUtil() {
	}

	public TreeModelViewerFilter getPlainEclipseViewerFilter() {
		if (plainEclipseViewerFilter == null) {
			plainEclipseViewerFilter = new TreeModelViewerFilter();
		}
		return plainEclipseViewerFilter;
	}

	public FeaturesAndPluginsViewerFilter getMissingExtensionsViewerFilter() {
		if (missingExtensionsViewerFilter == null) {
			missingExtensionsViewerFilter = new FeaturesAndPluginsViewerFilter();
		}
		return missingExtensionsViewerFilter;
	}

	public TreeModelViewerFilter getExtendedEclipseViewerFilter() {
		if (extendedEclipseViewerFilter == null) {
			extendedEclipseViewerFilter = new TreeModelViewerFilter();
		}
		return extendedEclipseViewerFilter;
	}

	public void setViewerPlainEclipse(TreeViewer treeViewer) {
		this.viewerPlainEclipse = treeViewer;
	}

	public void setViewerMissingExtensions(TreeViewer treeViewer) {
		this.viewerMissingExtensions = treeViewer;
	}

	public void setViewerExtendedEclipse(TreeViewer treeViewer) {
		this.viewerExtendedEclipse = treeViewer;
	}

	public TreeViewer getViewerPlainEclipse() {
		return viewerPlainEclipse;
	}

	public TreeViewer getViewerMissingExtensions() {
		return viewerMissingExtensions;
	}

	public TreeViewer getViewerExtendedEclipse() {
		return viewerExtendedEclipse;
	}

	public void filterChanged(ActivatableViewerFilter viewerFilter) {
		final TreeViewer viewerPlain = getViewerPlainEclipse();
		final TreeViewer viewerMissing = getViewerMissingExtensions();
		final TreeViewer viewerExtended = getViewerExtendedEclipse();
		if (viewerFilter != null && viewerPlain != null && viewerMissing != null && viewerExtended != null) {
			Display display = viewerPlain.getTree().getDisplay();

			ViewerFilter plainFilter = getPlainEclipseViewerFilter();
			ViewerFilter missingFilter = getMissingExtensionsViewerFilter();
			ViewerFilter extendedFilter = getExtendedEclipseViewerFilter();
			if (viewerFilter.equals(plainFilter)) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						viewerPlain.refresh();
					}
				});
			} else if (viewerFilter.equals(missingFilter)) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						viewerMissing.refresh();
					}
				});
			} else if (viewerFilter.equals(extendedFilter)) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						viewerExtended.refresh();
					}
				});
			}
		}
	}
}
