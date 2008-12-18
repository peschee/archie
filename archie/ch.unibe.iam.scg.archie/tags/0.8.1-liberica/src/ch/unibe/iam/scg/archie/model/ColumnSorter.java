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

/**
 * TODO: Document.
 */
package ch.unibe.iam.scg.archie.model;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import ch.unibe.iam.scg.archie.utils.ArrayUtils;

/**
 * <p>A ViewerSorter which can sort top down and bottom up depending on the setting
 * of the reverse boolean.</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ColumnSorter extends ViewerSorter {

	/** Sort direction */
	private int sortDirection;

	/** Index of the column which should be used to sort the results. */
	private int index;

	/**
	 * @param index
	 */
	public ColumnSorter(final int index) {
		this.index = index;
	}

	/**
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {
		Object o1 = ((Object[]) e1)[this.index];
		Object o2 = ((Object[]) e2)[this.index];

		int result;

		Class<?>[] o1interfaces = o1.getClass().getInterfaces();
		Class<?>[] o2interfaces = o2.getClass().getInterfaces();

		if (ArrayUtils.hasInterface(o1interfaces, Comparable.class)
				&& ArrayUtils.hasInterface(o2interfaces, Comparable.class)) {
			result = ((Comparable) o1).compareTo((Comparable) o2);
		} else {
			result = o1.toString().compareTo(o2.toString());
		}

		return (this.sortDirection == SWT.DOWN ? result * (-1) : result); // invert
	}

	/**
	 * @param sortDirection
	 *            Sort direction to use, should be one of <code>SWT.UP</code> or
	 *            <code>SWT.DOWN</code>
	 */
	public void setSortDirection(final int sortDirection) {
		this.sortDirection = sortDirection;
	}

	/**
	 * @return index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * @param index
	 */
	public void setIndex(final int index) {
		this.index = index;
	}
}
