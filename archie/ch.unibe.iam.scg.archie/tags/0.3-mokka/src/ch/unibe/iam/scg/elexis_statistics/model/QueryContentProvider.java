package ch.unibe.iam.scg.elexis_statistics.model;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


/**
 * A standard content provider for the queries if no special data should be
 * represented. Each row will be handled as data object in this content
 * provider. E.g. If you want to have the patient as the model represented, you
 * need another content provider.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class QueryContentProvider implements IStructuredContentProvider {

	private DataSet dataSet;

	public QueryContentProvider(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	public Object[] getElements(Object inputElement) {
		return this.dataSet.getContent().toArray();
	}

	public void dispose() {
		// TODO: Document
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO: Document
	}
}
