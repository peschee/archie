/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.ui.views;

import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;

import ch.unibe.iam.scg.elexis_statistics.actions.NewStatisticsAction;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractDataProvider;
import ch.unibe.iam.scg.elexis_statistics.ui.DetailsPanel;


/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class StatisticsSidebarView extends ViewPart implements IPropertyChangeListener {

	public static final String ID = "ch.unibe.iam.scg.elexis_statistics.views.StatisticsSidebarView";

	/** List of all available queries. TODO: reflection of query package */
	protected Hashtable<String, AbstractDataProvider> statisticsTable;

	protected Combo list;

	protected DetailsPanel details;

	protected AutoCompleteField autoComplete;

	@Override
	public void createPartControl(Composite parent) {
		// fill statistics table
		this.fillStatisticsTable();

		// create a new container for sidebar controls
		Composite container = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		// Create a simple field to show how field assist can be used for auto
		// complete.
		Group availableStatistics = new Group(container, SWT.NONE);
		availableStatistics.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		availableStatistics.setLayout(layout);
		availableStatistics.setText("Available Statistics");

		// Create an auto-complete field
		String[] availableTitles = this.statisticsTable.keySet().toArray(new String[this.statisticsTable.size()]);

		this.list = new Combo(availableStatistics, SWT.BORDER | SWT.DROP_DOWN);
		this.list.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.autoComplete = new AutoCompleteField(this.list, new ComboContentAdapter(), availableTitles);
		this.list.setItems(availableTitles);

		// add listeners
		this.list.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String title = StatisticsSidebarView.this.list.getText();

				if (StatisticsSidebarView.this.isValidProviderTitle(title)) {
					AbstractDataProvider provider = StatisticsSidebarView.this.statisticsTable.get(title);
					StatisticsSidebarView.this.details.setDataProvider(provider);
				} else {
					StatisticsSidebarView.this.details.reset();
				}
			}
		});

		Group statisticParameters = new Group(container, SWT.NONE);
		statisticParameters.setLayoutData(new GridData(GridData.FILL_BOTH));
		statisticParameters.setLayout(layout);
		statisticParameters.setText("Statistic Parameters");

		this.details = new DetailsPanel(statisticParameters, SWT.NONE);
		this.details.addPropertyChangeListener(this);

		// TODO: What is this for?
		Dialog.applyDialogFont(container);
	}

	/**
	 * Fills the statistics hashtable with all available plugins mapping a
	 * statistics plugin title to its datasource instance.
	 */
	private void fillStatisticsTable() {
		this.statisticsTable = new Hashtable<String, AbstractDataProvider>();

		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg
				.getConfigurationElementsFor("ch.unibe.iam.scg.elexis_statistics.datasource");
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement element = extensions[i];
			try {
				AbstractDataProvider source = (AbstractDataProvider) element.createExecutableExtension("class");

				// add to list and hash table
				this.statisticsTable.put(source.getJobname(), source);
			} catch (CoreException e) {
				// TODO Log
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Checks whether the title passed to this function is a valid data provider
	 * we have in the statistics table.
	 * 
	 * @param title
	 *            Provider title.
	 * @return True if the there is a provider with the given title, false else.
	 */
	protected boolean isValidProviderTitle(String title) {
		return this.statisticsTable.get(title) != null;
	}

	@Override
	public void setFocus() {
		// do nothing
	}
	
	/**
	 * Sets all children enabled according to the boolean passed to this
	 * function.
	 * 
	 * @param enabled
	 *            True if children should be enabled, false else.
	 */
	public void setEnabled(boolean enabled) {
		this.list.setEnabled(enabled);
		this.details.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().equals(NewStatisticsAction.JOB_RUNNING)) {
			this.setEnabled(false);
		}
		if(event.getProperty().equals(NewStatisticsAction.JOB_DONE)) {
			this.setEnabled(true);
		}
	}
}