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
package ch.unibe.iam.scg.archie.samples.i18n;

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

	private static final String BUNDLE_NAME = "ch.unibe.iam.scg.archie.samples.i18n.messages"; //$NON-NLS-1$

	// General
	public static String DB_QUERYING;
	
	// User Overview
	public static String USER_OVERVIEW_TITLE;
	public static String USER_OVERVIEW_DESCRIPTION;
	public static String USER_OVERVIEW_USER;
	public static String USER_OVERVIEW_BIRTHDAY;
	public static String USER_OVERVIEW_GENDER;
	public static String USER_OVERVIEW_VALID;
	public static String USER_OVERVIEW_GROUPS;
	public static String USER_OVERVIEW_YES;
	public static String USER_OVERVIEW_NO;
	public static String USER_OVERVIEW_UNDEFINED;
	
	// Consultations Statistics 
	public static String CONSULTATION_STATS_TITLE;
	public static String CONSULTATION_STATS_DESCRIPTION;
	public static String CONSULTATION_STATS_AGE_GROUP;
	public static String CONSULTATION_STATS_TOTAL_COSTS;
	public static String CONSULTATION_STATS_NUMBER_OF_CONSULTATIONS;
	public static String CONSULTATION_STATS_AVERAGE_COSTS;
	public static String CONSULTATION_STATS_TOTAL_PROFITS;
	public static String CONSULTATION_STATS_AVERAGE_PROFITS;

	public static String CONSULTATION_STATS_REGEX_MESSAGE = "";
	public static String CONSULTATION_STATS_COHORT_SIZE_EXCEPTION;
	
	// Consultations Statistics
	public static String PATIENTS_PROFITS_TITLE;
	public static String PATIENTS_PROFITS_DESCRIPTION;
	public static String PATIENTS_PROFITS_HEADING_PATIENT;
	public static String PATIENTS_PROFITS_HEADING_COSTS;
	public static String PATIENTS_PROFITS_HEADING_INCOME;
	public static String PATIENTS_PROFITS_HEADING_PROFIT;
	public static String PATIENTS_PROFITS_ONLY_FOR_MANDAT;
	
	// Prescription Overview
	public static String PRESCRIPTIONS_OVERVIEW_TITLE;
	public static String PRESCRIPTIONS_OVERVIEW_DESCRIPTION;
	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
