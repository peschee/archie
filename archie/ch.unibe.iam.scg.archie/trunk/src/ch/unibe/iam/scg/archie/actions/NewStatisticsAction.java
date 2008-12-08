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
 * TODO: Add package description.
 */
package ch.unibe.iam.scg.archie.actions;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ch.elexis.Desk;
import ch.elexis.util.Log;
import ch.elexis.util.SWTHelper;
import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.i18n.Messages;
import ch.unibe.iam.scg.archie.model.AbstractDataProvider;
import ch.unibe.iam.scg.archie.model.ChartModelManager;
import ch.unibe.iam.scg.archie.model.DataSet;
import ch.unibe.iam.scg.archie.model.DatasetTableColumnSorter;
import ch.unibe.iam.scg.archie.model.ProviderManager;
import ch.unibe.iam.scg.archie.model.SetDataException;
import ch.unibe.iam.scg.archie.model.TableFactory;
import ch.unibe.iam.scg.archie.model.TableManager;
import ch.unibe.iam.scg.archie.ui.ParametersPanel;
import ch.unibe.iam.scg.archie.ui.ProviderInformatioPanel;
import ch.unibe.iam.scg.archie.ui.ResultPanel;
import ch.unibe.iam.scg.archie.ui.views.StatisticsView;

/**
 * <p>
 * This action is responsible for the whole procedure of creating a new query:
 * getting all information needed from the user, starting the query in the
 * background and updating the view in the end.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class NewStatisticsAction extends Action implements IJobChangeListener, Observer {

	private StatisticsView view;
	private ParametersPanel parameters;
	private ProviderInformatioPanel providerInformation;
	private ArrayList<IPropertyChangeListener> listeners;

	/** constant for a running job */
	public static final String JOB_RUNNING = "JOB_RUNNING";

	/** constant for a finished job */
	public static final String JOB_DONE = "JOB_DONE";

	/**
	 * Action for creating a new statistical analysis. This class serves as a
	 * controller and mediator between the main and sidebar view. It also acts
	 * as a job listener and listens to the job this actions data provider runs.
	 * 
	 * @param parameters
	 *            Panel containing a provider's parameters.
	 */
	public NewStatisticsAction(ParametersPanel parameters) {
		super(Messages.ACTION_NEWSTAT_TITLE, AS_PUSH_BUTTON);

		// register as observer
		ProviderManager.getInstance().addObserver(this);

		this.setToolTipText(Messages.ACTION_NEWSTAT_DESCRIPTION);
		this.setImageDescriptor(ArchieActivator.getImageDescriptor("icons/database_go.png"));

		// disabled by default
		this.setEnabled(false);

		this.parameters = parameters;
		this.listeners = new ArrayList<IPropertyChangeListener>();
	}

	/**
	 * This actions main method, called when the action is run.
	 */
	@Override
	public void run() {
		// user set a provider and all fields are valid
		if (ProviderManager.getInstance().hasProvider() && this.parameters.allFieldsValid()) {

			// get provider from manager
			AbstractDataProvider provider = ProviderManager.getInstance().getProvider();

			// try settings parameters in provider
			try {
				this.parameters.updateProviderParameters();
			} catch (SetDataException e) {
				SWTHelper.showError(Messages.ERROR, e.getMessage());
				return;
			} catch (Exception e) {
				// TODO: Internalization
				ArchieActivator.LOG.log("Could not update parameters for the given provider." + "\n"
						+ e.getLocalizedMessage(), Log.WARNINGS);
				e.printStackTrace();
			}

			// focus view
			try {
				this.view = (StatisticsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView(StatisticsView.ID);
			} catch (PartInitException e) {
				ArchieActivator.LOG.log("Could not initialize main view." + "\n" + e.getLocalizedMessage(),
						Log.WARNINGS);
				e.printStackTrace();
			}

			// clean view
			this.view.clean();
			this.view.setActionsEnabled(false);
			this.setEnabled(false);

			// add provider information
			// TODO: Refactor this, currently the provider information is being
			// handled by this class
			this.providerInformation = new ProviderInformatioPanel(this.view.getParent());
			this.providerInformation.updateProviderInformation(provider);

			// set result composite and layout
			ResultPanel resultComposite = new ResultPanel(this.view.getParent(), SWT.FLAT);
			this.view.setResultComposite(resultComposite);
			this.view.getParent().layout();

			// update listeners
			this.updateListeners();

			// run the job
			provider.schedule();
			provider.addJobChangeListener(this);
		} else {
			SWTHelper.showError(Messages.ERROR_FIELDS_NOT_VALID_TITLE, Messages.ERROR_FIELDS_NOT_VALID);
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// INTERFACE FUNCTIONS
	// //////////////////////////////////////////////////////////////////////////

	/**
	 * This metohod is being called as soon as the job this action observes,
	 * finishes. The action is enabled as soon as the last job finishes. This
	 * method also creates and sets the result table in the result view as well
	 * as information about the parameters of the active provider in the header
	 * of the result panel.
	 * 
	 * @param event
	 */
	public void done(final IJobChangeEvent event) {
		// allow other threads to update this UI thread
		// http://www.eclipse.org/swt/faq.php#uithread
		Desk.getDisplay().syncExec(new Runnable() {
			public void run() {
				final ResultPanel results = NewStatisticsAction.this.view.getResultPanel();
				final AbstractDataProvider provider = ProviderManager.getInstance().getProvider();
				final DataSet dataset = provider.getDataSet();

				results.removeLoadingMessage();
				if (dataset.isEmpty()) {
					results.showEmptyMessage();
				} else {
					// create result table
					TableFactory tableFactory = TableFactory.getInstance();
					Table table = tableFactory.createTableFromData(results, provider.getDataSet(), provider
							.getLabelProvider(), provider.getContentProvider());

					// add column dataset sorter and add table to the manager
					new DatasetTableColumnSorter(table, dataset);
					TableManager.getInstance().setTable(table);
				}

				// remove old chart models
				ChartModelManager.getInstance().clean();

				// layout results at last
				results.layout();

				// enable all actions back again
				NewStatisticsAction.this.view.setActionsEnabled(true);
				NewStatisticsAction.this.setEnabled(true);

				// delegate property change event
				for (IPropertyChangeListener listener : NewStatisticsAction.this.listeners) {
					listener.propertyChange(new PropertyChangeEvent(NewStatisticsAction.this,
							NewStatisticsAction.JOB_DONE, null, null));
				}
			}
		});
	}

	/**
	 * Registers a change listener with this action.
	 * 
	 * @param listener
	 * 
	 * @see IPropertyChangeListener
	 */
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		super.addPropertyChangeListener(listener);
		if (!this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	/**
	 * De-registers a change listener with this action.
	 * 
	 * @param listener
	 * 
	 * @see IPropertyChangeListener
	 */
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		super.removePropertyChangeListener(listener);
		this.listeners.remove(listener);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		if (ProviderManager.getInstance().hasProvider()) {
			this.setEnabled(true);
		}
	}

	/**
	 * Updated the IPropertyChangeListener listeners for this action with a new
	 * PropertyChangeEvent containing the jobs current status.
	 */
	private void updateListeners() {
		// delegate property change event
		for (IPropertyChangeListener listener : this.listeners) {
			listener.propertyChange(new PropertyChangeEvent(this, NewStatisticsAction.JOB_RUNNING, null, null));
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// UNUSED INTERFACE FUNCTIONS
	// //////////////////////////////////////////////////////////////////////////

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#aboutToRun(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void aboutToRun(final IJobChangeEvent event) {
		// does nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#awake(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void awake(final IJobChangeEvent event) {
		// does nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#running(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void running(final IJobChangeEvent event) {
		// does nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#scheduled(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void scheduled(final IJobChangeEvent event) {
		// does nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#sleeping(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void sleeping(final IJobChangeEvent event) {
		// does nothing
	}
}