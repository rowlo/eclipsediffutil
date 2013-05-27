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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.rowlo.diffeclipse.model.tree.TreeModel;
import de.rowlo.diffeclipse.util.DiffEclipseUtil;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class ButtonExtractMissingExtensionsHandler extends AbstractButtonSelectionHandler {

	private final Text targetLocationText;
	private final Viewer viewer;
	private int totalCounter = 0;

	protected ButtonExtractMissingExtensionsHandler(Button button, Text text, Viewer viewer) {
		super(button);
		this.targetLocationText = text;
		this.viewer = viewer;
	}

	@Override
	public void widgetSelected(SelectionEvent se) {
		super.widgetSelected(se);

		final Button button = getButton();
		final Viewer viewer = getViewer();
		if (button != null && button.isEnabled() && button.equals(se.getSource()) && viewer != null) {
			button.setEnabled(false);

			ISelection selection = viewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection sel = (IStructuredSelection) selection;
				extractSelectedElements(sel);
			}

			button.setEnabled(true);
		}
	}

	protected void extractSelectedElements(IStructuredSelection selection) {
		final Text text = getTargetLocationText();
		if (selection != null && text != null) {
			final String sourceLocation = DiffEclipseUtil.INSTANCE.getExtendedEclipseLocation();
			final String targetLocation = text.getText();
			final String dropStructure = File.separator + "additionalExtensions" + File.separator + "eclipse";

			final List<?> selectedElements = selection.toList();
			final int size = count(selectedElements);
			final ArrayList<Integer> counterResult = new ArrayList<Integer>(1);
			Shell shell = text.getShell();
			try {
				ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
				progressMonitorDialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						String taskMsg = MessageFormat.format("Extracting Missing Extensions to: {0}", targetLocation
								+ dropStructure);
						monitor.beginTask(taskMsg, size);
						totalCounter = 0;
						processElements(selectedElements, monitor);
						counterResult.clear();
						counterResult.add(totalCounter);
						monitor.done();
						if (monitor.isCanceled()) {
							throw new InterruptedException("The long extraction was cancelled");
						}
					}

					private void processElements(List<?> selectedElements, IProgressMonitor monitor) {
						if (selectedElements == null || monitor == null) {
							return;
						}
						for (Object selectedElement : selectedElements) {
							if (selectedElement instanceof TreeModel) {
								TreeModel treeModel = (TreeModel) selectedElement;
								String filePath = treeModel.getFilePath();
								String sourceFilePath = sourceLocation + filePath;
								String targetFilePath = targetLocation + dropStructure + filePath;
								if (treeModel.isFile()) {
									copyFile(sourceFilePath, targetFilePath);
								} else {
									createDirectory(targetFilePath);
									ArrayList<Object> childElements = new ArrayList<Object>(treeModel.children);
									processElements(childElements, monitor);
								}
								totalCounter++;
								monitor.worked(1);
								String taskMsg = MessageFormat.format(
										"Extracting Missing Extensions to: {0} ({1}/{2})", targetLocation
												+ dropStructure, totalCounter, size);
								monitor.setTaskName(taskMsg);
							}
						}
					}
				});
			} catch (InvocationTargetException e) {
				MessageDialog.openError(shell, "Error", e.getMessage());
			} catch (InterruptedException e) {
				MessageDialog.openInformation(shell, "Cancelled", e.getMessage());
			}

			int counter = counterResult.size() > 0 ? counterResult.get(0) : 0;
			String msg = MessageFormat.format("Extracted {1} extensions to: {0}", targetLocation + dropStructure,
					counter);
			MessageDialog.openInformation(shell, "Extract Missing Extensions", msg);
		}
	}

	protected int count(List<?> elements) {
		int sum = 0;
		if (elements != null) {
			for (Object element : elements) {
				if (element instanceof TreeModel) {
					TreeModel treeModel = (TreeModel) element;
					sum++;
					if (treeModel.isFile()) {
					} else {
						ArrayList<Object> childElements = new ArrayList<Object>(treeModel.children);
						sum += count(childElements);
					}
				}
			}
		}
		return sum;
	}

	protected void copyFile(String sourceFilePath, String targetFilePath) {
		if (sourceFilePath != null && targetFilePath != null) {
			File sourceFile = new File(sourceFilePath);
			File targetFile = new File(targetFilePath);
			if (sourceFile != null && sourceFile.isFile() && targetFile != null && !targetFile.exists()
					&& targetFile.getParentFile() != null && targetFile.getParentFile().isDirectory()) {
				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;
				try {
					FileInputStream fis = new FileInputStream(sourceFile);
					bis = new BufferedInputStream(fis);
				} catch (FileNotFoundException fnfe) {
					Shell shell = Display.getCurrent().getActiveShell();
					MessageDialog.openError(shell, "FileNotFoundException", sourceFilePath);
				}
				try {
					FileOutputStream fos = new FileOutputStream(targetFile);
					bos = new BufferedOutputStream(fos);
				} catch (FileNotFoundException fnfe) {
					Shell shell = Display.getCurrent().getActiveShell();
					MessageDialog.openError(shell, "FileNotFoundException", targetFilePath);
				}

				try {
					copy(bis, bos);
				} catch (IOException ioe) {
					Shell shell = Display.getCurrent().getActiveShell();
					MessageDialog.openError(shell, "IOException", MessageFormat.format("source: {0}\target: {1}",
							sourceFilePath, targetFilePath));
				}

			}
		}
	}

	public static void copy(BufferedInputStream bis, BufferedOutputStream bos) throws IOException {
		if (bis != null && bos != null) {
			byte[] contents = new byte[32768];
			int bytesRead = 0;
			while ((bytesRead = bis.read(contents)) != -1) {
				bos.write(contents, 0, bytesRead);
			}
			bos.flush();
			bis.close();
			bos.close();
		}
	}

	protected void createDirectory(String targetFilePath) {
		if (targetFilePath != null) {
			File file = new File(targetFilePath);
			if (file != null && !file.exists()) {
				file.mkdirs();
			}
		}
	}

	public Text getTargetLocationText() {
		return targetLocationText;
	}

	public Viewer getViewer() {
		return viewer;
	}

}
