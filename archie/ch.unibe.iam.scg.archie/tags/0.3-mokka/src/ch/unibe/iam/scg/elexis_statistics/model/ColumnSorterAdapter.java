package ch.unibe.iam.scg.elexis_statistics.model;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

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
	 * Pulic constructor.
	 * 
	 * @param viewer
	 *            A tableviewer object.
	 * @param index
	 *            Index of the current column.
	 */
	public ColumnSorterAdapter(TableViewer viewer, int index) {
		this.viewer = viewer;
		this.index = index;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
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

		this.viewer.refresh(); // sort
	}
}
