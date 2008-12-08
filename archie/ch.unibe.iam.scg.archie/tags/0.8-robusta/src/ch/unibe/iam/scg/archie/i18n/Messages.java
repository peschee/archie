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

/**
 * TODO: Document.
 */
package ch.unibe.iam.scg.archie.i18n;

import org.eclipse.osgi.util.NLS;

/**
 * Message class
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "ch.unibe.iam.scg.archie.i18n.messages"; //$NON-NLS-1$

	public static String ARCHIE_STARTED;

	// Export action
	public static String ACTION_EXPORT_TITLE;
	public static String ACTION_EXPORT_DESCRIPTION;
	
	// New statistic action
	public static String ACTION_NEWSTAT_TITLE;
	public static String ACTION_NEWSTAT_DESCRIPTION;
	
	// Chart wizard action
	public static String CHART_WIZARD_TITLE;
	public static String CHART_WIZARD_DESCRIPTION;

	// User messages
	public static String WORKING;
	public static String NO_PLUGIN_SELECTED;
	public static String RESULT_EMPTY;
	
	// GUI parts
	public static String STATISTICS_LIST_TITLE;
	public static String EMPTY_PROVIDER_DESCRIPTION;
	public static String BUTTON_DATE_SELECT;

	// Error messages
	public static String ERROR;
	public static String ERROR_WRITING_FILE_TITLE;
	public static String ERROR_WRITING_FILE;
	public static String ERROR_DATE_FORMAT;
	public static String ERROR_FIELDS_NOT_VALID_TITLE;
	public static String ERROR_FIELDS_NOT_VALID;
	
	public static String ERROR_SET_START_DATE;
	public static String ERROR_SET_END_DATE;
	public static String ERROR_START_DATE_VALID;
	public static String ERROR_END_DATE_VALID;
	public static String ERROR_DATE_DIFFERENCE;
	
	public static String NO_CHART_MODEL;

	// Fields
	public static String FIELD_GENERAL_ERROR;
	public static String FIELD_GENERAL_ERROR_QUICKFIX;
	public static String FIELD_GENERAL_VALID;
	public static String FIELD_GENERAL_WARNING;
	
	public static String FIELD_NUMERIC_QUICKFIX;
	public static String FIELD_NUMERIC_ERROR;
	
	// ACL
	public static String ACL_ACCESS;
	public static String ACL_ACCESS_DENIED;
	

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
