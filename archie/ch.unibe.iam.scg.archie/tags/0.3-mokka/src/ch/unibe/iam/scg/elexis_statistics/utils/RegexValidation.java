/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.utils;

/**
 * Simple utility class that provides getters and setters for a regex pattern
 * <code>String</code> and associated error message <code>String</code> (if a regex match should fail).<br/>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class RegexValidation {

	private String pattern; // Regular Expression (regex) pattern.
	private String message; // Message if the regex match fails.

	/**
	 * @param pattern
	 *            String regex pattern.
	 * @param message
	 *            String error message if regex match fails.
	 * @throws IllegalArgumentException 
	 */
	public RegexValidation(final String pattern, final String message) throws IllegalArgumentException {
		if (pattern == null || message == null) {
			throw new IllegalArgumentException("Arguments pattern and message must not be null.");
		}
		this.pattern = pattern;
		this.message = message;
	}

	/**
	 * @return String regex pattern
	 */
	public String getPattern() {
		return this.pattern;
	}

	/**
	 * @param pattern
	 *            String regex pattern
	 */
	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return String error message if regex match fails.
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message
	 *            set error message if regex match fails.
	 */
	public void setMessage(final String message) {
		this.message = message;
	}
}
