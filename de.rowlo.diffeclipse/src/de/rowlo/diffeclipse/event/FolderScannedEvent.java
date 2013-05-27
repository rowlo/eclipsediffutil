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

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public class FolderScannedEvent {

	private final TextLocationEclipseHandler eclipseHandler;
	private final Set<String> files;
	private final String folderLocation;

	public FolderScannedEvent(final TextLocationEclipseHandler eclipseHandler, final Set<String> files,
			final String folderLocation) {
		this.eclipseHandler = eclipseHandler;
		this.files = files;
		this.folderLocation = folderLocation;
	}

	public TextLocationEclipseHandler getEclipseHandler() {
		return eclipseHandler;
	}

	public Set<String> getFiles() {
		return files;
	}

	public String getFolderLocation() {
		return folderLocation;
	}

}
