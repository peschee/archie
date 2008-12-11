/**
 * 
 */
package ch.unibe.iam.scg.archie.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>TODO: DOCUMENT ME!</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for ch.unibe.iam.scg.archie.tests");
		//$JUnit-BEGIN$
		suite.addTest(RegexValidationTest.suite());
		suite.addTest(AssertionTest.suite());
		suite.addTest(DataSetTest.suite());
		suite.addTest(DatasetHelperTest.suite());
		suite.addTest(StringHelperTest.suite());
		suite.addTest(CohortTest.suite());
		//$JUnit-END$
		return suite;
	}

}
