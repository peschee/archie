/*******************************************************************************
 * Copyright (c) 2008 Dennis Schenk, Peter Siska.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dennis Schenk - initial implementation
 *     Peter Siska	 - initial implementation
 *******************************************************************************/
package ch.unibe.iam.scg.archie.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * <p>TODO: DOCUMENT ME!</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class SWTUtils {
	
	/**
	 * @param parent
	 * @param mode 
	 * @return Label
	 */
	public static final Label createSeparator(final Composite parent, final int mode) {
		Label separator = new Label(parent, SWT.SEPARATOR | mode);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		return separator;
	}
	
	/**
	 * 
	 * @param parent
	 * @param mode
	 * @return Label
	 */
	public static final Label createSpacedSeparator(final Composite parent, int mode) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 10;
		
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Label separator = SWTUtils.createSeparator(container, mode);
		
		return separator;
	}
	
	/**
	 * 
	 * @param parent
	 * @return
	 */
	public static final Label createLabel(final Composite parent) {
		Label label = new Label(parent, SWT.NONE | SWT.WRAP);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		return label;
	}

}
