package ch.unibe.iam.scg.elexis_statistics.utils;


/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class StringHelper {

	/**
	 * Removes all illegal filename characters from a given String
	 * 
	 * @param name
	 * @return
	 * @see http://en.wikipedia.org/wiki/Filename#Reserved_characters_and_words
	 */
	public static final String removeIllegalCharacters(String name, boolean singleSpaces) {
		// remove illegal characters and replace with a more friendly char ;)
		String safe = name.trim();

		// remove illegal characters
		safe = safe.replaceAll("[\\/|\\\\|\\*|\\:|\\||\"|\'|\\<|\\>|\\{|\\}|\\?|\\%]", "");

		// replace . dots with _ and remove the _ if at the end
		safe = safe.replaceAll("\\.", "_");
		if (safe.endsWith("_")) {
			safe = safe.substring(0, safe.length() - 1);
		}

		// replace whitespace characters with _
		safe = safe.replaceAll("\\s+", "_");

		// replace double or more spaces with a single one
		if (singleSpaces) {
			safe = safe.replaceAll("_{2,}", "_");
		}

		return safe;
	}
}
