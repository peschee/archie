/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.elexis.util.Money;


/**
 * Holds any data in tabular form. A List of Strings serves as table headings<br/>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class DataSet implements Iterable<Object[]> {

	private List<Object[]> content = new ArrayList<Object[]>();

	/** Description of the columns */
	private List<String> headings;

	/** the width of the matrix. Or: How many columns exist? */
	private int width = 0;

	/**
	 * True if a summary row is added. Needed when adding a row,
	 * since the row has to be added before the column summary row
	 * and the summary has to be recomputed
	 */
	private boolean colSummary = false;
	
	public DataSet() {
		this.content = new ArrayList<Object[]>();
		this.headings = new ArrayList<String>();
	}

	/**
	 * Constructs a <code>DataSet</code> with a list of objects arrays and a heading list.
	 * @param list
	 * @param headings
	 * @throws IllegalArgumentException 
	 */
	public DataSet(final List<Object[]> list, final List<String> headings) throws IllegalArgumentException {
		if (list == null || headings == null || list.isEmpty() || headings.isEmpty()) {
			throw new IllegalArgumentException("Arguments list and headings must not be null and must not be empty!");
		}
		if (list.get(0).length != headings.size()) {
			throw new IllegalArgumentException("Array size inside list has to have the same size as provided headings!");
		}
		
		this.content = list;
		this.headings = headings;
		this.width = this.content.get(0).length;
	}

	/**
	 * Adds a summary of the given column at the end. The column has to be a
	 * Number instance or a Money instance.
	 * @param x 
	 */
	public void addColumnSum(final int x) {
		if (this.colSummary) {
			this.recomputeColumnSummary(x);
		} else {
			this.colSummary = true;
			this.createColumnSummary(x);
		}
	}

	private void createColumnSummary(int x) {
		assert (this.content.get(0) != null);
		this.addRow();
		this.recomputeColumnSummary(x);
	}

	private void recomputeColumnSummary(int x) {
		Object[] col = this.getColumn(x);

		if (col[0].getClass().equals(Money.class)) {
			// TODO: Document
		} else { // Number
			double sum = 0.0;
			for (Object object : col) {
				sum += ((Number) object).doubleValue();
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @param value
	 */
	public void set(final int x, final int y, final Object value) {
		this.content.get(y)[x] = value;
	}

	/**
	 * @param x
	 * @param y
	 * @return Object at specified location, null if list is empty.
	 */
	public Object get(final int x, final int y) {
		return this.content.get(y)[x];
	}

	/**
	 * @param y
	 * @return Object Array of specified row.
	 */
	public Object[] getRow(final int y) {
		return this.content.get(y);
	}

	/**
	 * @param y
	 * @param obj
	 */
	public void setRow(final int y, final Object[] obj) {
		this.content.set(y, obj);
	}

	/**
	 * @param x Column number
	 * @return Object Array of specified column.
	 */
	public Object[] getColumn(final int x) {
		List<Object> cols = new ArrayList<Object>(this.content.size());

		for (Object[] objects : this.content) {
			cols.add(objects[x]);
		}

		return cols.toArray();
	}

	/**
	 * Adds a row
	 */
	public void addRow() {
		this.addRow(new Object[this.width]);
	}

	/**
	 * @param obj
	 */
	public void addRow(final Object[] obj) {
		assert (obj.length == this.width);
		if (this.colSummary) {
			this.content.add(this.content.size() - 2, obj);
		} else {
			this.content.add(obj);
		}
	}

	/** 
	 * @return Iterator over list.
	 */
	public Iterator<Object[]> iterator() {
		return this.content.iterator();
	}

	/**
	 * @return List
	 */
	public List<Object[]> getContent() {
		return this.content;
	}

	/**
	 * @param list
	 */
	public void setContent(final List<Object[]> list) {
		this.content = list;
		this.width = list.get(0).length; 
	}

	/**
	 * @return List of Headings.
	 */
	public List<String> getHeadings() {
		return this.headings;
	}

	/**
	 * @param headings
	 */
	public void setHeadings(final List<String> headings) {
		this.headings = headings;
	}
}