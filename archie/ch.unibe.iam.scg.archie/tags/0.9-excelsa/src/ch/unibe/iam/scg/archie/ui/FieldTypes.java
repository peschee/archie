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
package ch.unibe.iam.scg.archie.ui;

/**
 * Enum that stores available implementations of <code>AbstractFieldComposite</code>.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public enum FieldTypes {
	/**
	 * Vanilla Text Field
	 */
	TEXT, 
	/**
	 * Numeric Text Field
	 */
	TEXT_NUMERIC, 
	/**
	 * Date Text Field
	 */
	TEXT_DATE, 
	/**
	 * Checkbox Button
	 */
	BUTTON_CHECKBOX;
}
