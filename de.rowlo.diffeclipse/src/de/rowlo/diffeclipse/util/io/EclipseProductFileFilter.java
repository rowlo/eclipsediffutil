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
package de.rowlo.diffeclipse.util.io;

import java.io.File;
import java.io.FileFilter;

/**
 * A {@link FileFilter} that returns the file ".eclipseproduct" and the
 * directories "plugins", "features", and "dropins".
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class EclipseProductFileFilter implements FileFilter {
	private static final String DIR_PLUGINS = "plugins";
	private static final String DIR_FEATURES = "features";
	private static final String DIR_DROPINS = "dropins";
	private static final String FILE_ECLIPSEPRODUCT = ".eclipseproduct";

	public static final EclipseProductFileFilter INSTANCE = createInstance();

	protected EclipseProductFileFilter() {
	}

	protected static EclipseProductFileFilter createInstance() {
		return new EclipseProductFileFilter();
	}

	public boolean accept(File f) {
		boolean isEclipseProduct = f.getName().toLowerCase().endsWith(FILE_ECLIPSEPRODUCT) && f.isFile();
		boolean isPluginsDir = DIR_PLUGINS.equals(f.getName()) && f.isDirectory();
		boolean isFeaturesDir = DIR_FEATURES.equals(f.getName()) && f.isDirectory();
		boolean isDropinsDir = DIR_DROPINS.equals(f.getName()) && f.isDirectory();
		return isEclipseProduct || isPluginsDir || isFeaturesDir || isDropinsDir;
	}

	public boolean isEclipseProduct(File[] files) {
		// < 2 because FILE_ECLIPSEPRODUCT and at least one of the DIRS should
		// exist
		if (files == null || files.length < 2) {
			return false;
		}
		return true;
	}

}
