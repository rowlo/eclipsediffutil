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
package de.rowlo.diffeclipse.controller;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

import de.rowlo.diffeclipse.event.DiffUtilFileListHandler;
import de.rowlo.diffeclipse.event.HandlerFactory;
import de.rowlo.diffeclipse.event.TextLocationEclipseHandler;
import de.rowlo.diffeclipse.filter.FeaturesAndPluginsViewerFilter;
import de.rowlo.diffeclipse.filter.TreeModelViewerFilter;
import de.rowlo.diffeclipse.screen.DiffEclipseForm;
import de.rowlo.diffeclipse.util.DiffEclipseUtil;
import de.rowlo.diffeclipse.util.TreeModelFilterUtil;
import de.rowlo.diffeclipse.util.io.EclipseProductFileFilter;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class DiffEclipseFormController {

	private final DiffEclipseForm form;

	/**
	 * Creates a new {@link DiffEclipseFormController} that controls the given
	 * {@link DiffEclipseForm}.
	 * 
	 * @param form
	 *            the {@link DiffEclipseForm} to be controlled. Must not be
	 *            null!
	 * @throws NullPointerException
	 *             if form is null
	 */
	public DiffEclipseFormController(DiffEclipseForm form) throws NullPointerException {
		this.form = form;
		if (form == null) {
			throw new NullPointerException("Argument form must not be null!");
		}

		installListeners();
	}

	protected void installListeners() {
		// direct widget listeners (1st stage)
		installSelectionListeners();
		installModifyListeners();

		// listeners on event processors (2nd stage)
		installDiffUtilFileListListeners();
	}

	protected void installSelectionListeners() {
		installButtonHideSameFilesInPlainEclipseSelectionListeners();
		installButtonFeaturesAndPluginsSelectionListeners();
		installButtonHideSameFilesInExtendedEclipseSelectionListeners();

		installButtonQuitSelectionListeners();
		installButtonExtractMissingExtensionsSelectionListeners();

		installButtonChooseLocationPlainEclipseSelectionListeners();
		installButtonChooseLocationMissingExtensionsSelectionListeners();
		installButtonChooseLocationExtendedEclipseSelectionListeners();

		installTreeViewerMissingExtensionsPostSelectionChangedListeners();
	}

	protected void installButtonHideSameFilesInPlainEclipseSelectionListeners() {
		Button button = getForm().getButtonHideSameFilesInPlainEclipse();
		TreeModelViewerFilter viewerFilter = TreeModelFilterUtil.INSTANCE.getPlainEclipseViewerFilter();
		TreeViewer treeViewer = getForm().getTreeViewerPlainEclipse();
		TreeModelFilterUtil.INSTANCE.setViewerPlainEclipse(treeViewer);
		SelectionListener handler = HandlerFactory.createViewerFilterActivationButtonHandler(button, viewerFilter);
		button.addSelectionListener(handler);
	}

	protected void installButtonFeaturesAndPluginsSelectionListeners() {
		Button button = getForm().getButtonFeaturesAndPlugins();
		FeaturesAndPluginsViewerFilter viewerFilter = TreeModelFilterUtil.INSTANCE.getMissingExtensionsViewerFilter();
		TreeViewer treeViewer = getForm().getTreeViewerMissingExtensions();
		TreeModelFilterUtil.INSTANCE.setViewerMissingExtensions(treeViewer);
		SelectionListener handler = HandlerFactory.createViewerFilterActivationButtonHandler(button, viewerFilter);
		button.addSelectionListener(handler);
	}

	protected void installButtonHideSameFilesInExtendedEclipseSelectionListeners() {
		Button button = getForm().getButtonHideSameFilesInExtendedEclipse();
		TreeModelViewerFilter viewerFilter = TreeModelFilterUtil.INSTANCE.getExtendedEclipseViewerFilter();
		TreeViewer treeViewer = getForm().getTreeViewerExtendedEclipse();
		TreeModelFilterUtil.INSTANCE.setViewerExtendedEclipse(treeViewer);
		SelectionListener handler = HandlerFactory.createViewerFilterActivationButtonHandler(button, viewerFilter);
		button.addSelectionListener(handler);
	}

	protected void installButtonQuitSelectionListeners() {
		Button button = getForm().getButtonQuit();
		SelectionListener handler = HandlerFactory.createButtonQuitHandler(button);
		button.addSelectionListener(handler);
	}

	protected void installButtonExtractMissingExtensionsSelectionListeners() {
		Button button = getForm().getButtonExtractMissingExtensions();
		Text text = getForm().getTextLocationMissingExtensions();
		TreeViewer viewer = getForm().getTreeViewerMissingExtensions();
		SelectionListener handler = HandlerFactory.createButtonExtractMissingExtensionsHandler(button, text, viewer);
		button.addSelectionListener(handler);
	}

	protected void installButtonChooseLocationPlainEclipseSelectionListeners() {
		Button button = getForm().getButtonChooseLocationPlainEclipse();
		Text text = getForm().getTextLocationPlainEclipse();
		SelectionListener handler = HandlerFactory.createButtonChooseLocationHandler(button, text);
		button.addSelectionListener(handler);
	}

	protected void installButtonChooseLocationMissingExtensionsSelectionListeners() {
		Button button = getForm().getButtonChooseLocationMissingExtensions();
		Text text = getForm().getTextLocationMissingExtensions();
		SelectionListener handler = HandlerFactory.createButtonChooseLocationHandler(button, text);
		button.addSelectionListener(handler);
	}

	protected void installButtonChooseLocationExtendedEclipseSelectionListeners() {
		Button button = getForm().getButtonChooseLocationExtendedEclipse();
		Text text = getForm().getTextLocationExtendedEclipse();
		SelectionListener handler = HandlerFactory.createButtonChooseLocationHandler(button, text);
		button.addSelectionListener(handler);
	}

	protected void installTreeViewerMissingExtensionsPostSelectionChangedListeners() {
		TreeViewer treeViewer = getForm().getTreeViewerMissingExtensions();
		Button button = getForm().getButtonExtractMissingExtensions();
		ISelectionChangedListener handler = HandlerFactory
				.createViewerSelectionEnablesButtonHandler(treeViewer, button);
		treeViewer.addPostSelectionChangedListener(handler);
	}

	protected void installModifyListeners() {
		installTextLocationPlainEclipseModifyListeners();
		installTextLocationMissingExtensionsModifyListeners();
		installTextLocationExtendedEclipseModifyListeners();
	}

	protected void installTextLocationPlainEclipseModifyListeners() {
		Text text = getForm().getTextLocationPlainEclipse();
		TreeViewer treeViewer = getForm().getTreeViewerPlainEclipse();
		ModifyListener handler = HandlerFactory.createTextLocationHandler(text, this);
		text.addModifyListener(handler);
		TextLocationEclipseHandler eclipseHandler = HandlerFactory.createTextLocationEclipseHandler(text, treeViewer);
		text.addModifyListener(eclipseHandler);
		DiffEclipseUtil.INSTANCE.usePlainEclipseHandler(eclipseHandler);
	}

	protected void installTextLocationMissingExtensionsModifyListeners() {
		Text text = getForm().getTextLocationMissingExtensions();
		ModifyListener handler = HandlerFactory.createTextLocationHandler(text, this);
		text.addModifyListener(handler);
	}

	protected void installTextLocationExtendedEclipseModifyListeners() {
		Text text = getForm().getTextLocationExtendedEclipse();
		TreeViewer treeViewer = getForm().getTreeViewerExtendedEclipse();
		ModifyListener handler = HandlerFactory.createTextLocationHandler(text, this);
		text.addModifyListener(handler);
		TextLocationEclipseHandler eclipseHandler = HandlerFactory.createTextLocationEclipseHandler(text, treeViewer);
		text.addModifyListener(eclipseHandler);
		DiffEclipseUtil.INSTANCE.useExtendedEclipseHandler(eclipseHandler);
	}

	protected void installDiffUtilFileListListeners() {
		TreeViewer treeViewer = getForm().getTreeViewerMissingExtensions();
		DiffUtilFileListHandler handler = HandlerFactory.createDiffUtilFileListHandler(treeViewer);
		DiffEclipseUtil.INSTANCE.addDiffUtilFileListListener(handler);
	}

	/**
	 * @return the controlled {@link DiffEclipseForm}. Never null.
	 */
	protected final DiffEclipseForm getForm() {
		return form;
	}

	/**
	 * Updates the UI elements of the {@link DiffEclipseForm}.
	 */
	public void updateUI() {
		enableSectionPlainEclipse();
		enableSectionMissingExtensions();
		enableSectionExtendedEclipse();

		enableButtonExtractMissingExtensions();
	}

	protected void enableSectionPlainEclipse() {
		Text textLocation = getForm().getTextLocationPlainEclipse();
		String text = textLocation.getText();

		boolean enabled = isEclipseProduct(text);

		Section section = getForm().getSectionPlainEclipse();
		section.setEnabled(enabled);
	}

	protected void enableSectionMissingExtensions() {
		boolean enabled = false;

		Text textLocation = getForm().getTextLocationMissingExtensions();
		String text = textLocation.getText();
		File file = new File(text);
		enabled = file.exists() && file.isDirectory();

		Section section = getForm().getSectionMissingExtension();
		section.setEnabled(enabled);
	}

	protected void enableSectionExtendedEclipse() {
		Text textLocation = getForm().getTextLocationExtendedEclipse();
		String text = textLocation.getText();

		boolean enabled = isEclipseProduct(text);

		Section section = getForm().getSectionExtendedEclipse();
		section.setEnabled(enabled);
	}

	protected boolean isEclipseProduct(String path) {
		boolean isEclipseProduct = false;
		if (path == null) {
			return isEclipseProduct;
		}

		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			EclipseProductFileFilter filter = EclipseProductFileFilter.INSTANCE;
			File[] essentialFiles = file.listFiles(filter);
			isEclipseProduct = filter.isEclipseProduct(essentialFiles);
		}

		return isEclipseProduct;
	}

	protected void enableButtonExtractMissingExtensions() {
		TreeViewer treeViewer = getForm().getTreeViewerMissingExtensions();
		ISelection selection = treeViewer.getSelection();
		boolean enabled = !selection.isEmpty();

		Button button = getForm().getButtonExtractMissingExtensions();
		button.setEnabled(enabled);
	}

}
