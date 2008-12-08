package ch.unibe.iam.scg.elexis_statistics.samples.i18n;

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

	private static final String BUNDLE_NAME = "ch.unibe.iam.scg.elexis_statistics.samples.i18n.messages"; //$NON-NLS-1$

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
	public static String CONSULTATION_STATS_BIRTHYEAR;
	public static String CONSULTATION_STATS_TOTAL_COSTS;
	public static String CONSULTATION_STATS_NUMBER_OF_CONSULTATIONS;
	public static String CONSULTATION_STATS_AVERAGE_COSTS;
	public static String CONSULTATION_STATS_REGEX_MESSAGE ="";
	public static String CONSULTATION_STATS_COHORT_SIZE_EXCEPTION;
	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
