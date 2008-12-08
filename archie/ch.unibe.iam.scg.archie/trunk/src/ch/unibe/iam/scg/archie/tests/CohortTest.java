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
import java.util.Collections;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.unibe.iam.scg.archie.model.Cohort;

/**
 * <p>
 * TODO: DOCUMENT ME!
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class CohortTest {

	private static Cohort cohort;
	private static Cohort cohortSame;
	private static Cohort cohortSmall;
	private static Cohort cohortLarge;
	private static Cohort cohortStrange;

	@BeforeClass
	public static void setUpClass() {
		cohort = new Cohort(5, 30, "Hello World");
		cohortSame = new Cohort(5, 30, "Something else");
		cohortSmall = new Cohort(10, 15, new Integer(3));
		cohortLarge = new Cohort(10, 30, new Integer(5));
		cohortStrange = new Cohort(-30, -10, cohort);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor() {
		new Cohort(-5, -10, "Preconditions not satisfied!");
		new Cohort(45, 13, "Preconditions not satisfied!");
		new Cohort(9, -1, "Preconditions not satisfied!");
	}

	@Test
	public void testCompareTo() {
		Assert.assertEquals(cohortSmall.compareTo(cohortLarge), -1);
		Assert.assertEquals(cohortLarge.compareTo(cohortSmall), 1);
		Assert.assertEquals(cohortLarge.compareTo(cohortLarge), 0);
		ArrayList<Cohort> cohorts = new ArrayList<Cohort>(5);
		cohorts.add(cohort);
		cohorts.add(cohortSame);
		cohorts.add(cohortStrange);
		cohorts.add(cohortLarge);
		cohorts.add(cohortSmall);
		Collections.sort(cohorts);
		Assert.assertTrue(cohorts.get(0).equals(cohortStrange));
		Assert.assertTrue(cohorts.get(1).equals(cohortSame));
		Assert.assertTrue(cohorts.get(2).equals(cohort));
		Assert.assertTrue(cohorts.get(3).equals(cohortSmall));
		Assert.assertTrue(cohorts.get(4).equals(cohortLarge));
	}

	@Test
	public void testEquals() {
		Assert.assertTrue(cohort.equals(cohort));
		Assert.assertTrue(cohortStrange.getValue().equals(cohort));
		Assert.assertTrue(cohort.equals(cohortSame));
		Assert.assertFalse(cohort.equals(cohortLarge));
		Assert.assertFalse(cohort.equals(cohortSmall));
	}

	@Test
	public void testGettersAndSetters() {
		Assert.assertEquals(cohort.getCohortSize(), 26);
		Assert.assertEquals(cohort.getLowerBound(), 5);
		Assert.assertEquals(cohort.getUpperBound(), 30);
		Assert.assertEquals(cohort.getValue(), "Hello World");
		Assert.assertEquals(cohort.toString(), "5 - 30");
		cohort.setLowerBound(7);
		cohort.setUpperBound(12);
		cohort.setValue("Another String");
		Assert.assertEquals(cohort.getCohortSize(), 6);
		Assert.assertEquals(cohort.getLowerBound(), 7);
		Assert.assertEquals(cohort.getUpperBound(), 12);
		Assert.assertEquals(cohort.getValue(), "Another String");
		Assert.assertEquals(cohort.toString(), "7 - 12");
		cohort.setLowerBound(2);
		cohort.setUpperBound(2);
		Assert.assertEquals(cohort.toString(), "2");
		Assert.assertEquals(cohort.getCohortSize(), 1);
		Assert.assertEquals(cohortStrange.getCohortSize(), 21);
	}
}