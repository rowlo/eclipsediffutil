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

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public interface DiffUtilFileListListener {

	/**
	 * Notifies {@link DiffUtilFileListListener}s about a file list change for
	 * the given location.
	 * 
	 * @param event
	 *            {@link DiffUtilFileListEvent}
	 */
	void onFileListChange(DiffUtilFileListEvent event);

}
