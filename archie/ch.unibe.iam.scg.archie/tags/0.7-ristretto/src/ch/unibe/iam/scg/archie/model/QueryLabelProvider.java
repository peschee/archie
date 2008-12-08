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

import java.util.Currency;
import java.util.Locale;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.rgw.tools.Money;

/**
 * Standard label provider for the queries. If no special labels or model is
 * required, this label provider will do nicely. It provides just the labels
 * given at the specific row/columns.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class QueryLabelProvider extends LabelProvider implements ITableLabelProvider {


	/** (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(final Object element, final int columnIndex) {
		return null;
	}

	/** (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(final Object element, final int columnIndex) {
		Object[] row = (Object[]) element;
		if (row[columnIndex].getClass() == Money.class) {
			Currency cur = Currency.getInstance(Locale.getDefault());
			return cur + " " + row[columnIndex].toString();
		}
		return row[columnIndex].toString();
	}
}