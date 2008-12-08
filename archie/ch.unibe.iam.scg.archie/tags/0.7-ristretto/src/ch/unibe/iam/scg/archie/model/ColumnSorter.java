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

import ch.unibe.iam.scg.archie.utils.ArrayUtils;

/**
 * A ViewerSorter which can sort top down and bottom up depending on the setting
 * of the reverse boolean.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ColumnSorter extends ViewerSorter {

	/** Reverse ordering */
	private boolean reverse = false;

	/** Index of the column which should be used to sort the results. */
	private int index;

	/**
	 * @param index
	 */
	public ColumnSorter(final int index) {
		this.index = index;
	}

	/** (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {
		Object o1 = ((Object[]) e1)[this.index];
		Object o2 = ((Object[]) e2)[this.index];
		
		int result;
		
		Class<?>[] o1interfaces = o1.getClass().getInterfaces();
		Class<?>[] o2interfaces = o2.getClass().getInterfaces();
		
		if (ArrayUtils.hasInterface(o1interfaces, Comparable.class) && ArrayUtils.hasInterface(o2interfaces, Comparable.class)) {
			result = ((Comparable) o1).compareTo((Comparable) o2);
		} else {
			result = o1.toString().compareTo(o2.toString());
		}

		return (this.reverse ? result * (-1) : result); // invert if reversed
	}

	/**
	 * @param reverse
	 */
	public void setReverse(final boolean reverse) {
		this.reverse = reverse;
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
