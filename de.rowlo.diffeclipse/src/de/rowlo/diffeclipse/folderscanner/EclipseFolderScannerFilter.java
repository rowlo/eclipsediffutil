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

import de.rowlo.diffeclipse.model.tree.TreeModel;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
public interface EclipseFolderScannerFilter {

	/**
	 * Attempt to filter the subject.
	 * 
	 * @param subject
	 *            {@link TreeModel}
	 * @return false if the subject is a negative hit for the filter and should
	 *         not be part of another result or true if the subject is a
	 *         positive hit that passes this filter and should be used in
	 *         another result
	 */
	boolean apply(TreeModel subject);

}
