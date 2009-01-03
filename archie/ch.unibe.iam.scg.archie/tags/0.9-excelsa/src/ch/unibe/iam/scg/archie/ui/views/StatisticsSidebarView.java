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
package ch.unibe.iam.scg.archie.ui.views;

import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
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

import ch.elexis.actions.GlobalEvents;
import ch.elexis.actions.GlobalEvents.UserListener;
import ch.elexis.util.Log;
import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.acl.ArchieACL;
import ch.unibe.iam.scg.archie.actions.NewStatisticsAction;
import ch.unibe.iam.scg.archie.controller.ProviderManager;
import ch.unibe.iam.scg.archie.i18n.Messages;
import ch.unibe.iam.scg.archie.model.AbstractDataProvider;
import ch.unibe.iam.scg.archie.ui.DetailsPanel;

/**
 * <p>In this View a user can chose a statistic, set options for it and run it.</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class StatisticsSidebarView extends ViewPart implements IPropertyChangeListener, UserListener {

	/** ID of this view. */
	public static final String ID = ArchieActivator.PLUGIN_ID + ".ui.views.StatisticsSidebarView";

	/** List of all available providers. */
	protected Hashtable<String, AbstractDataProvider> providers;

	protected Combo list;

	protected DetailsPanel details;

	protected AutoCompleteField autoComplete;

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		// fill statistics table
		this.initializeAvailableStatistics();

		// create a new container for sidebar controls
		Composite container = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		// Create a simple field for auto complete.
		Group availableStatistics = new Group(container, SWT.NONE);
		availableStatistics.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		availableStatistics.setLayout(layout);
		availableStatistics.setText(Messages.STATISTICS_LIST_TITLE);

		// Create an auto-complete field
		String[] availableTitles = this.providers.keySet().toArray(new String[this.providers.size()]);

		this.list = new Combo(availableStatistics, SWT.BORDER | SWT.DROP_DOWN);
		this.list.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.autoComplete = new AutoCompleteField(this.list, new ComboContentAdapter(), availableTitles);
		this.list.setItems(availableTitles);

		// add listeners
		this.list.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String title = StatisticsSidebarView.this.list.getText();

				if (StatisticsSidebarView.this.isValidProviderTitle(title)) {
					AbstractDataProvider provider = StatisticsSidebarView.this.providers.get(title);
					ProviderManager.getInstance().setProvider(provider);
				} else {
					StatisticsSidebarView.this.details.reset();
				}
			}
		});

		// Add parameters group.
		Group statisticParameters = new Group(container, SWT.NONE);
		statisticParameters.setLayoutData(new GridData(GridData.FILL_BOTH));
		statisticParameters.setLayout(layout);
		statisticParameters.setText(Messages.STATISTIC_PARAMETERS_TITLE);

		// Add details panel containing details and parameters.
		this.details = new DetailsPanel(statisticParameters, SWT.NONE);
		this.details.addPropertyChangeListener(this);

		// Disable by default if user has no access rights.
		this.setEnabled(ArchieACL.userHasAccess());

		// Register this view for UserChanged events.
		GlobalEvents.getInstance().addUserListener(this);
	}

	/**
	 * Fills the statistics hashtable with all available plugins mapping a
	 * statistics plugin title to its datasource instance.
	 */
	private void initializeAvailableStatistics() {
		this.providers = new Hashtable<String, AbstractDataProvider>();

		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg.getConfigurationElementsFor("ch.unibe.iam.scg.archie.dataprovider");
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement element = extensions[i];
			try {
				AbstractDataProvider source = (AbstractDataProvider) element.createExecutableExtension("class");

				// add to list of available statistics
				this.providers.put(source.getName(), source);
			} catch (CoreException e) {
				ArchieActivator.LOG.log("Error while trying to load a data provider." + "\n" + element.getName() + "\n"
						+ e.getLocalizedMessage(), Log.WARNINGS);
				e.printStackTrace();
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
		return this.providers.get(title) != null;
	}

	/**
	 * Nothing is done on focus here.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
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

	/**
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange
	 *      (org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(NewStatisticsAction.JOB_RUNNING)) {
			this.setEnabled(false);
		}
		if (event.getProperty().equals(NewStatisticsAction.JOB_DONE)) {
			this.setEnabled(true);
		}
	}

	/**
	 * Removes any currently managed provider from the provider manager.
	 * 
	 * @see ProviderManager
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		ProviderManager.getInstance().setProvider(null);
		super.dispose();
	}

	/**
	 * @see ch.elexis.actions.GlobalEvents.UserListener#UserChanged()
	 */
	public void UserChanged() {
		// Set enabled according to ACL.
		boolean accessEnabled = ArchieACL.userHasAccess();
		this.setEnabled(accessEnabled);

		// If a user has no access at all, disable everything.
		if (!accessEnabled) {
			this.details.setCancelButtonEnabled(accessEnabled);
			this.details.setActionEnabled(accessEnabled);
			// If there's a provider currently selected, enable the query
			// action.
		} else if (this.list.getSelectionIndex() != -1) {
			this.details.setActionEnabled(accessEnabled);
		}

		// Cancel any previous job if running.
		if (ProviderManager.getInstance().hasProvider()) {
			ProviderManager.getInstance().getProvider().cancel();
		}
	}
}