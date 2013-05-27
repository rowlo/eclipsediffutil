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
public class DiffUtilFileListEvent {
	private final String location;

	public DiffUtilFileListEvent(final String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}
}
