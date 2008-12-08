/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.tests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.unibe.iam.scg.elexis_statistics.utils.RegexValidation;

/**
 * Tests RegexValidation class.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class RegexValidationTest {
	
	private static RegexValidation someRegexVal;
	private static final String SOME_PATTERN = "\\d{3,}";
	private static final String SOME_MESSAGE = "Some Regex Validation Message";
	private static final String ANOTHER_PATTERN = "\\d{1,}";
	private static final String ANOTHER_MESSAGE = "Another Regex Validation Message";
	
	@BeforeClass 
	public static void setUpClass() {
		someRegexVal = new RegexValidation(SOME_PATTERN, SOME_MESSAGE);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorArgumentsTest() {
		RegexValidation nullRegexVal = new RegexValidation(null, null);
	}

	@Test
	public void getterAndSetterTest() {
		Assert.assertNotNull(someRegexVal.getMessage());
		Assert.assertNotNull(someRegexVal.getPattern());
		Assert.assertEquals(someRegexVal.getMessage(), SOME_MESSAGE);
		Assert.assertEquals(someRegexVal.getPattern(), SOME_PATTERN);
		someRegexVal.setMessage(ANOTHER_MESSAGE);
		someRegexVal.setPattern(ANOTHER_PATTERN);
		Assert.assertEquals(someRegexVal.getMessage(), ANOTHER_MESSAGE);
		Assert.assertEquals(someRegexVal.getPattern(), ANOTHER_PATTERN);
	}
}
