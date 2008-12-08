/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.model;

import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Creates a <code>TableViewer</code> from data provided by an implementation of
 * AbstractDataprovider.<br/>
 * Uses singleton pattern.<br/>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class TableFactory {

	private static TableFactory INSTANCE = null;

	/** constructor */
	private TableFactory() {
		// private constructor, singleton pattern
	}

	/**
	 * Returns an instance of this object. Static method as this class
	 * implements the Singleton pattern.
	 * 
	 * @return TabeFactory Instance
	 */
	public static final TableFactory getInstance() {
		if (TableFactory.INSTANCE == null) {
			TableFactory.INSTANCE = new TableFactory();
		}
		return TableFactory.INSTANCE;
	}

	/**
	 * Creates a tableviewer from the given provider.
	 * 
	 * @param parent
	 *            Composite that holds the table.
	 * @param provider
	 *            A data provider.
	 */
	public void createTableFromData(final Composite parent, final AbstractDataProvider provider) {
		Table table = this.createTable(parent, provider);

		TableViewer tableViewer = new TableViewer(table);
		tableViewer.setLabelProvider(provider.getLabelProvider());
		tableViewer.setContentProvider(provider.getContentProvider());

		// invoke the inputChanged method after a content provider is set
		tableViewer.setInput(table);

		this.addColumnSort(tableViewer);
	}

	/**
	 * Creates the table based on the DataProvider passed to this function.
	 * 
	 * @param parent Composite that holds the table.
	 * @param provider A data provider.
	 * @return A table object.
	 */
	private Table createTable(final Composite parent, final AbstractDataProvider provider) {
		Table table = new Table(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		// we don't need this anymore, using tablecolumnlayout below
		// GridData gridData = new GridData(GridData.FILL_VERTICAL |
		// GridData.FILL_HORIZONTAL);
		// gridData.grabExcessHorizontalSpace = true;
		// table.setLayoutData(gridData);

		TableColumnLayout tableLayout = new TableColumnLayout();
		parent.setLayout(tableLayout);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		this.createColumns(table, provider, tableLayout);

		return table;
	}

	/**
	 * Creates all columns in this table. This method sets a weight to each
	 * column so that all columns are layed out equally in their container.
	 * 
	 * @param table The table to perform column operations on.
	 * @param provider A data provider.
	 * @param layout Layout to use for the columns.
	 */
	private void createColumns(final Table table, final AbstractDataProvider provider, TableColumnLayout layout) {
		int i = 0;
		List<String> headings = provider.getDataSet().getHeadings();
		for (String text : headings) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(text);
			column.setMoveable(true);
			column.pack();

			// computes the weight for each column based on the total number of
			// columns and sets the columndata in the layout accordingly
			int weight = Math.round((100 / headings.size()));
			layout.setColumnData(column, new ColumnWeightData(weight));

			i++;
		}
	}

	/**
	 * Adds a selection listener to all columns in order to be able to sort by
	 * column later.
	 * 
	 * @param viewer A TableViewer object.
	 */
	private void addColumnSort(TableViewer viewer) {
		TableColumn[] cols = viewer.getTable().getColumns();
		for (int i = 0; i < cols.length; i++) {
			cols[i].addSelectionListener(new ColumnSorterAdapter(viewer, i));
		}
	}
}
