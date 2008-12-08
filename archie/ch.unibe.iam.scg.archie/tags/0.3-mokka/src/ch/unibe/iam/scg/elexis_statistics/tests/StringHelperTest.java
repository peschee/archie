package ch.unibe.iam.scg.elexis_statistics.tests;

import org.junit.Assert;
import org.junit.Test;

import ch.unibe.iam.scg.elexis_statistics.utils.StringHelper;

/**
 * Test the utility class for strings.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class StringHelperTest {

	@Test
	public void removeillegalCharacters() {
		String illegal = "  \"I| am an? ill!egal... Stri//<ng> {contai\\/ning} b'a'd chara%c%ter|s!. ";
		String good = StringHelper.removeIllegalCharacters(illegal, false);

		Assert.assertTrue(good.startsWith("I"));
		Assert.assertTrue(good.endsWith("!"));
		Assert.assertFalse(good.equals("I_am_an_ill!egal_String_containing_bad_characters!."));
		Assert.assertTrue(good.equals("I_am_an_ill!egal____String_containing_bad_characters!"));

		String single = StringHelper.removeIllegalCharacters(good, true);
		Assert.assertFalse(single.equals("I_am_an_ill!egal____String_containing_bad_characters!"));
		Assert.assertTrue(single.equals("I_am_an_ill!egal_String_containing_bad_characters!"));
	}
}
