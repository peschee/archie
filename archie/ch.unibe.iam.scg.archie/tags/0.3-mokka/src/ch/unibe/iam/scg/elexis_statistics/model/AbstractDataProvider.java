package ch.unibe.iam.scg.elexis_statistics.model;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

import ch.elexis.actions.BackgroundJob;

/**
 * An abstract class for data providers used by this plugin. An
 * <code>AbstractDataProvider</code> is being constructed by a name, is also the
 * name of the background job being run when the provider is collecting its
 * data. The provider holds a dataset object which provides convenience methods
 * for presenting and retrieving statistical data. Each provider also has to set
 * the size of its elements accordingly so the <code>BackgroundJob</code>
 * progress information is being displayed accurately.
 * 
 * $Id: AbstractDataProvider.java 258 2008-10-06 17:51:15Z psiska
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @see ch.elexis.actions.BackgroundJob
 * @version $Rev: 258
 */
public abstract class AbstractDataProvider extends BackgroundJob {

	private LabelProvider labelProvider;
	private IStructuredContentProvider contentProvider;
	protected IProgressMonitor monitor;

	// DataSet which stores results of this query in tabular form.
	protected DataSet dataSet;

	// Size of the jobs, this has to be set in the subclasses accordingly
	protected int size = 0;

	/**
	 * @param name
	 *            the AbstractDataProvider name as String.
	 */
	public AbstractDataProvider(String name) {
		super(name);
		this.dataSet = new DataSet();
		this.initializeDefaultValues();
	}

	/*
	 * ABSTRACT METHODS
	 */

	/**
	 * Returns the description for this data provider.
	 */
	public abstract String getDescription();

	/**
	 * Creates headings for each column in the dataset object of this provider.
	 * 
	 * @return List<String> A list of strings containing the headings.
	 */
	protected abstract List<String> createHeadings();

	/**
	 * Creates a list of all elements for this provider.
	 * 
	 * @return List<Object[]> List of objects for this provider.
	 */
	protected abstract List<Object[]> createContent();

	/**
	 * Initialized the default values of parameter for a provider. This method
	 * is automatically called in the constructor and needs to be implemented by
	 * all subclasses that have parameters connected to annotations.
	 */
	protected abstract void initializeDefaultValues();

	/*
	 * SUPERCLASS IMPLEMENTATIONS
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @seech.elexis.actions.BackgroundJob#execute(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public IStatus execute(IProgressMonitor monitor) {
		this.monitor = monitor;
		
		// set headings and dataset content
		this.dataSet.setHeadings(this.createHeadings());
		this.dataSet.setContent(this.createContent());

		// initialize providers
		this.initProviders();

		this.monitor.done();
		return Status.OK_STATUS;
	}

	/**
	 * Initializes content and label providers and sets them accordingly. This
	 * is a generic method using two default providers for labels and content.
	 * Every class that has custom providers needs to overrride this method.
	 */
	protected void initProviders() {
		QueryContentProvider content = new QueryContentProvider(this.dataSet);
		QueryLabelProvider label = new QueryLabelProvider();
		this.setContentProvider(content);
		this.setLabelProvider(label);
	}

	/*
	 * PUBLIC GETTERS AND SETTERS
	 */

	/**
	 * Returns the content provider for this data provider.
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider
	 */
	public IStructuredContentProvider getContentProvider() {
		return this.contentProvider;
	}

	/**
	 * Returns the label provider for this data provider.
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider
	 * @return LabelProvider A label provider for this object.
	 */
	public LabelProvider getLabelProvider() {
		return this.labelProvider;
	}

	/**
	 * Sets the label provider for this data provider.
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider
	 * @param labelProvider
	 *            A label provider.
	 */
	protected void setLabelProvider(LabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	/**
	 * Sets the content provider for this data provider.
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider
	 * @param contentProvider
	 *            A content provider for this object.
	 */
	protected void setContentProvider(IStructuredContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	/**
	 * Returns the dataset being held by this data provider.
	 * 
	 * @see ch.unibe.iam.scg.elexis_statistics.model.DataSet
	 * @return DataSet The dataset object for this provider.
	 */
	public DataSet getDataSet() {
		return this.dataSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.elexis.actions.BackgroundJob#getSize()
	 */
	@Override
	public int getSize() {
		return this.size;
	}
}
