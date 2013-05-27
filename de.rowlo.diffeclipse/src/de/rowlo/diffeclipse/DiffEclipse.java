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
package de.rowlo.diffeclipse;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import de.rowlo.diffeclipse.controller.DiffEclipseFormController;
import de.rowlo.diffeclipse.screen.DiffEclipseForm;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class DiffEclipse {

	public static final String IMAGE_NULL = "/org/eclipse/jface/dialogs/images/help.gif";
	public static final String IMAGE_QUIT = "/org/eclipse/jface/action/images/stop.gif";
	public static final String IMAGE_EXTRACT = "/de/rowlo/diffeclipse/ui/exp_deployplug.gif";
	public static final String IMAGE_FOLDER = "/de/rowlo/diffeclipse/ui/fldr_obj.gif";
	public static final String IMAGE_JAR = "/de/rowlo/diffeclipse/ui/jar_obj.gif";
	public static final String IMAGE_FILE = "/de/rowlo/diffeclipse/ui/file_obj.gif";
	public static final String IMAGE_FEATURE = "/de/rowlo/diffeclipse/ui/feature_obj.gif";
	public static final String IMAGE_PLUGIN = "/de/rowlo/diffeclipse/ui/plugin_obj.gif";
	public static final String IMAGE_ECLIPSE = "/de/rowlo/diffeclipse/ui/eclipse.png";
	private static final String[] IMAGES = { IMAGE_NULL, IMAGE_QUIT, IMAGE_EXTRACT, IMAGE_FOLDER, IMAGE_JAR,
			IMAGE_FILE, IMAGE_FEATURE, IMAGE_PLUGIN, IMAGE_ECLIPSE };

	private static final Map<String, Image> images = new HashMap<String, Image>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("Eclipse Installations Diff Util");
		createImages();

		DiffEclipseForm form = new DiffEclipseForm(shell, SWT.NONE);

		SWTResourceManager.getImage(DiffEclipseForm.class, "/org/eclipse/jface/action/images/stop.gif");
		shell.pack();
		shell.setSize(950, 600);
		shell.open();

		DiffEclipseFormController controller = new DiffEclipseFormController(form);
		controller.updateUI();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		SWTResourceManager.dispose();
		display.dispose();
	}

	private static void createImages() {
		if (images.isEmpty()) {
			for (String imagePath : IMAGES) {
				Image image = SWTResourceManager.getImage(DiffEclipse.class, imagePath);
				images.put(imagePath, image);
			}
		}
	}

	public static Image getImage(String key) {
		if (key == null || !images.containsKey(key)) {
			return images.get(IMAGE_NULL);
		}
		return images.get(key);
	}

}
