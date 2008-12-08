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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.unibe.iam.scg.archie.utils.DatasetHelper;

/**
 * Sorts the columns of a TableViewer in a simple manner. It does not consider
 * any sorting done before.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ColumnSorterAdapter extends SelectionAdapter {

	private TableViewer viewer;
	private int index;
	private boolean reverse = false;

	/**
	 * Public constructor.
	 * 
	 * @param viewer
	 *            A tableViewer object.
	 * @param index
	 *            Index of the current column.
	 */
	public ColumnSorterAdapter(TableViewer viewer, int index) {
		this.viewer = viewer;
		this.index = index;
	}

	/** (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(final SelectionEvent event) {
		// determine new sort column and direction
		Table table = this.viewer.getTable();
		
		TableColumn sortColumn = table.getSortColumn();
		TableColumn currentColumn = (TableColumn) event.widget;
		
		int dir = table.getSortDirection();
		if (sortColumn == currentColumn) {
			dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
		} else {
			table.setSortColumn(currentColumn);
			dir = SWT.UP;
		}
		
		ColumnSorter sorter = (ColumnSorter) this.viewer.getSorter();

		if (sorter == null) {
			sorter = new ColumnSorter(0);
			this.viewer.setSorter(sorter);
		}

		if (sorter.getIndex() == this.index) {
			this.reverse = this.reverse ? false : true; // flip flop.
			sorter.setReverse(this.reverse);
		} else { // start with top down sorting.
			sorter.setIndex(this.index);
			this.reverse = false;
			sorter.setReverse(false);
		}

		// update data displayed in table
		table.setSortDirection(dir);
		table.clearAll();
		
		this.viewer.refresh(); // sort
	}
}