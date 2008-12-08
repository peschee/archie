package ch.unibe.iam.scg.elexis_statistics.model;

import java.util.Currency;
import java.util.Locale;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import ch.elexis.Desk;
import ch.elexis.util.Money;

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
public class QueryLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

	private static int CURRENT_ROW = 0;
	private static int ROW_ODD = 1;

	/**
	 * Color for alternate rows in the result table, HEX color string. The
	 * default color used here is the one that is being used on Mac OS X by
	 * default (light blue).
	 */
	private static String ALTERNATE_ROW_COLOR = "ECF3FE";

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		Object[] row = (Object[]) element;
		if (row[columnIndex].getClass() == Money.class) {
			Currency cur = Currency.getInstance(Locale.getDefault());
			return cur + " " + row[columnIndex].toString();
		}
		return row[columnIndex].toString();
	}

	public Color getBackground(Object element, int columnIndex) {
		Object[] row = (Object[]) element;
		int columns = row.length;

		Color rowColor = null;

		// set different background color on odd rows
		if ((QueryLabelProvider.CURRENT_ROW % 2) == QueryLabelProvider.ROW_ODD) {
			rowColor = Desk.getColorFromRGB(QueryLabelProvider.ALTERNATE_ROW_COLOR);
		}

		// alternate row after a set of columns
		if ((columnIndex + 1) == columns) {
			QueryLabelProvider.CURRENT_ROW++;
		}

		return rowColor;
	}

	public Color getForeground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}