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
package ch.unibe.iam.scg.archie.model;

/**
 * <p>A Cohort represents a certain age-group (e.g. all patients with ages from 10 to 20).
 * lowerBound must always be smaller than upperBound</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class Cohort implements Comparable<Cohort> {
	
	/**
	 * Delimiter used for the title of a cohort.
	 */
	public final static String TITLE_DELIMITER = " - ";
	
	private int lowerBound;
	private int upperBound;
	private Object value;
	
	
	/**
	 * @param lowerBound
	 * @param upperBound
	 * @param value
	 */
	public Cohort(final int lowerBound, final int upperBound, final Object value) {
		// Checking Preconditions:
		if (lowerBound > upperBound) {
			throw new IllegalArgumentException("lowerBound has to be smaller than upperBound!");
		}
		this.setLowerBound(lowerBound);
		this.setUpperBound(upperBound);
		this.setValue(value);
	}
		
	/**
	 * @return Cohort size. The size is always 1 larger than the real difference, 
	 * since a cohort includes both the lower and upper Bound.
	 */
	public int getCohortSize() {
		return Math.abs(this.upperBound - this.lowerBound) + 1;
	}

	/**
	 * @param lowerBound the lowerBound to set
	 */
	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	/**
	 * @return the lowerBound
	 */
	public int getLowerBound() {
		return lowerBound;
	}

	/**
	 * @param upperBound the upperBound to set
	 */
	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	/**
	 * @return the upperBound
	 */
	public int getUpperBound() {
		return upperBound;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * @return title of this cohort (made up of lower- and upper bound.
	 */
	@Override
	public String toString() {
		if (this.lowerBound == this.upperBound) {
			return ((Integer) this.lowerBound).toString();
		}
		return this.lowerBound + TITLE_DELIMITER + this.upperBound;
	}

	/**
	 * A Cohort is smaller than another if its lower bound is smaller. 
	 * If the lower bound of two cohorts is equal, the cohort with the 
	 * smaller cohort size is smaller.
	 * 
	 * @param otherCohort 
	 * @return -1 if this cohort is smaller, 0 if equal, 1 is larger
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Cohort otherCohort) {
		 if (this.lowerBound == otherCohort.getLowerBound()) {
			 if(this.getCohortSize() < otherCohort.getCohortSize()) {
				 return -1;
			 }
			 else if (this.getCohortSize() > otherCohort.getCohortSize()) {
				 return 1;
			 }
			 return 0;
		 }
		 else if (this.lowerBound < otherCohort.getLowerBound()) {
			 return -1;
		 }
		 else if (this.lowerBound > otherCohort.getLowerBound()) {
			 return 1;
		 }
		 return 0;
	}
	
	/**
	 * @param object
	 * @return true if this cohort is equal to another cohort (same lower- and upperBound)
	 */
	@Override
	public boolean equals(Object object) {
		 if (object instanceof Cohort) {
			Cohort otherCohort = ((Cohort) object);
			 if (this.lowerBound == otherCohort.getLowerBound() && this.upperBound == otherCohort.getUpperBound()) {
				 return true;
			 }
		 }
		return false;
	}
	
	/**
	 * @return HashCode of the name of this Cohort.
	 */
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
}
