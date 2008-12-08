package ch.unibe.iam.scg.elexis_statistics.i18n;

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

	private static final String BUNDLE_NAME = "ch.unibe.iam.scg.elexis_statistics.i18n.messages"; //$NON-NLS-1$

	// Export action
	public static String EXPORT_TITLE;
	public static String EXPORT_DESCRIPTION;

	// User messages
	public static String WORKING;
	public static String NO_PLUGIN_SELECTED;

	// Error messages
	public static String ERROR_WRITING_FILE_TITLE;
	public static String ERROR_WRITING_FILE;
	public static String ERROR_DATE_FORMAT;
	public static String ERROR_FIELDS_NOT_VALID_TITLE;
	public static String ERROR_FIELDS_NOT_VALID;

	// Fields
	public static String FIELD_GENERAL_ERROR;
	public static String FIELD_GENERAL_ERROR_QUICKFIX;
	public static String FIELD_GENERAL_VALID;
	public static String FIELD_GENERAL_WARNING;
	public static String FIELD_NUMERIC_QUICKFIX;
	public static String FIELD_NUMERIC_ERROR;
	

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
