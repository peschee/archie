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
package ch.unibe.iam.scg.archie.tests;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.unibe.iam.scg.archie.model.DataSet;

/**
 * <p>Tests <code>DataSet</code></p>
 * <pre>
 * SampleDataSet:
 * ==============
 *     | First Name | Last Name | Address         | Country
 *     ---------------------------------------------------
 * x/y | 0          | 1         | 2               | 3
 * -------------------------------------------------------
 * 0   | Hans       | Muster    | Superstrasse 1  | Switzerland
 * 1   | Vreni      | Müller    | Musterstrasse 1 | Switzerland
 * 2   | Jakob      | Meier     | Ottweg 3        | Switzerland
 * </pre>
 * <p>E.g getCell(2,1) == Meier (Matrix notation)</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class DataSetTest {

	private static DataSet sampleDataSet;
	private static DataSet emptyDataSet;
	private static ArrayList<String> sampleHeadings  = new ArrayList<String>();
	private static ArrayList<String> sampleHeadingsTooFew = new ArrayList<String>();
	private static ArrayList<Comparable<?>[]> sampleContent = new ArrayList<Comparable<?>[]>();
	
	private static String[] sampleRow1 = {"Hans", "Muster", "Superstrasse 1", "Switzerland"};
	private static String[] sampleRow2 = {"Vreni", "Müller", "Musterstrasse 1", "Switzerland"};
	private static String[] sampleRow3 = {"Jakob", "Meier", "Ottweg 3", "Switzerland"};
	private static String[] smallRow = {"Jakob", "Meier", "Ottweg 3"};
	
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
	
	@Test
	public void testClone() {
		DataSet clonedSet = (DataSet) DataSetTest.sampleDataSet.clone();
		Assert.assertNotSame(clonedSet, DataSetTest.sampleDataSet);
		Assert.assertNotSame(clonedSet.getContent(), DataSetTest.sampleDataSet.getContent());
		Assert.assertNotSame(clonedSet.getHeadings(), DataSetTest.sampleDataSet.getHeadings());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithNullAsArguments() {
		new DataSet(null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithEmptyArguments() {
		new DataSet(new ArrayList<Comparable<?>[]>(), new ArrayList<String>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorWithTooFewHeaders() {
		new DataSet(sampleContent, sampleHeadingsTooFew);
	}

	@Test
	public void testGetters() {
		Assert.assertEquals(sampleContent, sampleDataSet.getContent());
		Assert.assertEquals(sampleHeadings, sampleDataSet.getHeadings());
		Assert.assertArrayEquals(sampleRow1, sampleDataSet.getRow(0));
		Assert.assertArrayEquals(sampleRow2, sampleDataSet.getRow(1));
		Assert.assertArrayEquals(sampleRow3, sampleDataSet.getRow(2));
		Assert.assertEquals("Hans", sampleDataSet.getCell(0, 0));
		Assert.assertEquals("Meier", sampleDataSet.getCell(2, 1));
		Assert.assertEquals("Vreni", sampleDataSet.getCell(1, 0));
		Object[] column1 = new Object[3];
		column1[0] = "Muster";
		column1[1] = "Müller";
		column1[2] = "Meier";
		Assert.assertArrayEquals(column1, sampleDataSet.getColumn(1));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetWithEmptyDataSet() {
		emptyDataSet.getCell(3, 4);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetWithEmptyDataSet() {
		emptyDataSet.setCell(2, 4, "Some String");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addTooSmallRowTest() {
		sampleDataSet.addRow(smallRow);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void settingContentBeforeHeadings() {
		DataSet dataSet = new DataSet();
		dataSet.setContent(sampleContent);
	}
	
	@Test
	public void toStringTest() {
		String desiredOutput = 
		"| First Name | Last Name  | Address         | Country     \n" +
		"----------------------------------------------------------\n" +
		"| Hans       | Muster     | Superstrasse 1  | Switzerland \n" +
		"| Vreni      | Müller     | Musterstrasse 1 | Switzerland \n" +
		"| Jakob      | Meier      | Ottweg 3        | Switzerland \n";
		Assert.assertEquals(desiredOutput, sampleDataSet.toString());
	}
}
