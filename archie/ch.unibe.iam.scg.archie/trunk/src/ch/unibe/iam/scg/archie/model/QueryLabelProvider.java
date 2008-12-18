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

import java.util.Currency;
import java.util.Locale;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.rgw.tools.Money;

/**
 * <p>
 * Standard label provider for the queries. If no special labels or model is
 * required, this label provider will do nicely. It provides just the labels
 * given at the specific row/columns.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class QueryLabelProvider extends LabelProvider implements ITableLabelProvider {

	/**
	 * Does nothing, returns null.
	 * 
	 * @return Returns null.
	 */
	public Image getColumnImage(final Object element, final int columnIndex) {
		return null;
	}

	/**
	 * Returns the textual representation of each row at a given column index.
	 * This method merely calls the <code>toString()</code> method on those
	 * objects. No special labels are being returned, except for
	 * <code>Money</code> classes where we use the currency based on the locale
	 * (<code>Locale.getDefault()</code>) as a prefix.
	 * 
	 * @return Returns the <code>toString()</code> representation of the object
	 *         in the given row at the given column index.
	 */
	public String getColumnText(final Object element, final int columnIndex) {
		Comparable<?>[] row = (Comparable[]) element;
		if (row[columnIndex].getClass() == Money.class) {
			Currency cur = Currency.getInstance(Locale.getDefault());
			return cur + " " + row[columnIndex].toString();
		}
		return row[columnIndex].toString();
	}
}