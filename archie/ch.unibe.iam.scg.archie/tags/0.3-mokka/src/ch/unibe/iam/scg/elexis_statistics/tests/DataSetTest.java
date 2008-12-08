/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.tests;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.unibe.iam.scg.elexis_statistics.model.DataSet;

/**
 * Test <code>DataSet</code>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class DataSetTest {
	
	/**
	 * SampleDataSet:
	 * 
	 * 		| First Name	| Last Name | Address 			| Country
	 * 		=============================================================
	 * 	x/y | 0				| 1			| 2					| 3
	 * 	-----------------------------------------------------------------
	 * 	0   | Hans       	| Muster    | Superstrasse 1	| Switzerland
	 * 	1   | Vreni      	| Müller    | Superstrasse 1	| Switzerland
	 * 	2   | Jakob      	| Meier    	| Superstrasse 3	| Switzerland
	 */
	
	private static DataSet sampleDataSet;
	private static DataSet emptyDataSet;
	private static ArrayList<String> sampleHeadings  = new ArrayList<String>();
	private static ArrayList<String> sampleHeadingsTooFew = new ArrayList<String>();
	private static ArrayList<Object[]> sampleContent = new ArrayList<Object[]>();
	
	private static String[] sampleRow1 = {"Hans", "Muster", "Superstrasse 1", "Switzerland"};
	private static String[] sampleRow2 = {"Vreni", "Müller", "Superstrasse 1", "Switzerland"};
	private static String[] sampleRow3 = {"Jakob", "Meier", "Superstrasse 3", "Switzerland"};
	
	@BeforeClass 
	public static void setUpClass() {
		sampleHeadings.add("First Name");
		sampleHeadings.add("Last Name");
		sampleHeadings.add("Address");
		sampleHeadings.add("Country");
		
		sampleHeadingsTooFew.add("First Name");
		sampleHeadingsTooFew.add("Last Name");
		sampleHeadingsTooFew.add("Address");
		
		sampleContent.add(sampleRow1);
		sampleContent.add(sampleRow2);
		sampleContent.add(sampleRow3);
		
		sampleDataSet = new DataSet(sampleContent, sampleHeadings);
		emptyDataSet = new DataSet();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithNullAsArguments() {
		DataSet dataSet = new DataSet(null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithEmptyArguments() {
		DataSet dataSet = new DataSet(new ArrayList(), new ArrayList());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithTooFewHeaders() {
		DataSet dataSet = new DataSet(sampleContent, sampleHeadingsTooFew);
	}

	@Test
	public void testGetters() {
		Assert.assertEquals(sampleContent, sampleDataSet.getContent());
		Assert.assertEquals(sampleHeadings, sampleDataSet.getHeadings());
		Assert.assertArrayEquals(sampleRow1, sampleDataSet.getRow(0));
		Assert.assertArrayEquals(sampleRow2, sampleDataSet.getRow(1));
		Assert.assertArrayEquals(sampleRow3, sampleDataSet.getRow(2));
		Assert.assertEquals("Hans", sampleDataSet.get(0, 0));
		Assert.assertEquals("Meier", sampleDataSet.get(1, 2));
		Assert.assertEquals("Vreni", sampleDataSet.get(0, 1));
		Object[] column1 = new Object[3];
		column1[0] = "Muster";
		column1[1] = "Müller";
		column1[2] = "Meier";
		Assert.assertArrayEquals(column1, sampleDataSet.getColumn(1));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetWithEmptyDataSet() {
		emptyDataSet.get(3, 4);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testSetWithEmptyDataSet() {
		emptyDataSet.set(2, 4, "Some String");
	}
}
