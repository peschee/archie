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

import java.util.ArrayList;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;

import ch.elexis.Desk;
import ch.elexis.actions.GlobalEvents;
import ch.elexis.actions.GlobalEvents.UserListener;
import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.acl.ArchieACL;
import ch.unibe.iam.scg.archie.actions.CreateChartsAction;
import ch.unibe.iam.scg.archie.actions.RefreshChartsAction;
import ch.unibe.iam.scg.archie.i18n.Messages;
import ch.unibe.iam.scg.archie.model.MutexRule;
import ch.unibe.iam.scg.archie.ui.DashboardOverview;
import ch.unibe.iam.scg.archie.ui.GraphicalMessage;
import ch.unibe.iam.scg.archie.ui.charts.AbstractChartComposite;
import ch.unibe.iam.scg.archie.ui.charts.AbstractDatasetCreator;
import ch.unibe.iam.scg.archie.ui.charts.AgeHistogrammChart;
import ch.unibe.iam.scg.archie.ui.charts.ConsultationMoneyChart;
import ch.unibe.iam.scg.archie.ui.charts.ConsultationNumberChart;
import ch.unibe.iam.scg.archie.ui.charts.PatientsConsHistChart;

/**
 * <p>
 * TODO: DOCUMENT ME!
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class Dashboard extends ViewPart implements UserListener, IJobChangeListener {

	/**
	 * ID of this view.
	 */
	public static final String ID = ArchieActivator.PLUGIN_ID + ".ui.views.Dashboard";

	/**
	 * List of chart composites in this dashboard.
	 */
	private ArrayList<AbstractChartComposite> charts;

	/**
	 * Composite container of all parts in this view.
	 */
	private Composite container;

	/**
	 * The bottom part of the view.
	 */
	private Composite bottomPart;

	/**
	 * Initial message about the not created charts.
	 */
	private Composite chartsNotCreatedMessage;

	/**
	 * Internal variable to count the number of finished chart creator jobs.
	 */
	private int jobCounter;

	private RefreshChartsAction refreshChartsAction;
	private CreateChartsAction createChartsAction;

	/**
	 * Creates a Dashboard
	 */
	public Dashboard() {
		this.charts = new ArrayList<AbstractChartComposite>(4);
		this.jobCounter = 0;

		GlobalEvents.getInstance().addUserListener(this);
	}

	// ////////////////////////////////////////////////////////////////////////////
	// PRIVATE HELPER METHODS
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * Initializes the dashboard. This method is also called when a UserChanged
	 * event is propagated to redraw the contents of the dashboard according to
	 * the current user's access permissions.
	 */
	private void initialize() {
		// Create according to ACL
		if (ArchieACL.userHasAccess()) {
			this.initializeParts();
			this.initializeChartsNotCreatedMessage();
		} else {
			this.cancelAllCreators();
			this.initializeAccessDisabled();
		}
		this.container.layout();
	}

	/**
	 * Add actions to this view.
	 */
	private void addActions() {
		this.createChartsAction = new CreateChartsAction(this);
		this.refreshChartsAction = new RefreshChartsAction(this);

		IToolBarManager manager = this.getViewSite().getActionBars().getToolBarManager();
		manager.add(this.createChartsAction);
		manager.add(this.refreshChartsAction);
	}

	/**
	 * Cancels all running jobs that have been started by the chart's creators.
	 */
	private void cancelAllCreators() {
		for (AbstractChartComposite chart : this.charts) {
			chart.cancelCreator();
		}
	}

	/**
	 * Initialize charts in the given parent.
	 * 
	 * @param container
	 */
	private void initializeCharts() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = true;

		this.bottomPart.setLayout(layout);
		this.bottomPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		this.charts.add(new ConsultationNumberChart(this.bottomPart, SWT.NONE));
		this.charts.add(new ConsultationMoneyChart(this.bottomPart, SWT.NONE));

		this.charts.add(new PatientsConsHistChart(this.bottomPart, SWT.NONE));
		this.charts.add(new AgeHistogrammChart(this.bottomPart, SWT.NONE));

		// register job change listener
		for (AbstractChartComposite chart : this.charts) {
			chart.addJobChangeListener(this);
		}

		// layout container
		this.bottomPart.layout();
	}

	/**
	 * Initializes the access disabled message.
	 */
	private void initializeAccessDisabled() {
		this.container.setLayout(new GridLayout());
		this.container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		new GraphicalMessage(this.container, ArchieActivator.getImage(ArchieActivator.IMG_ERROR),
				Messages.ACL_ACCESS_DENIED);
	}

	/**
	 * Initializes the parts of this dashboard. The dashboard consists of two
	 * parts, one upper part that has the a dashboard overview, a lower part
	 * containing either a message about the status of the charts or the charts
	 * themselves.
	 */
	private void initializeParts() {
		DashboardOverview overview = new DashboardOverview(this.container, SWT.NONE);
		this.bottomPart = new Composite(this.container, SWT.NONE);

		GridLayout layout = new GridLayout();

		this.container.setLayout(layout);
		this.container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		this.bottomPart.setLayout(layout);
		this.bottomPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		overview.setLayout(layout);
		overview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		assert (overview != null);
		assert (this.bottomPart != null);
	}

	/**
	 * Initializes the charts not created yet message.
	 */
	private void initializeChartsNotCreatedMessage() {
		assert (this.bottomPart != null);

		this.chartsNotCreatedMessage = new GraphicalMessage(this.bottomPart, ArchieActivator
				.getImage(ArchieActivator.IMG_INFO),
				"Charts have not been created yet.\nUse the chart creation button on the top right side of this view.");
	}

	/**
	 * Removes the charts not created message container if available.
	 */
	private void clearChartsNotCreatedMessage() {
		if (this.chartsNotCreatedMessage != null && !this.chartsNotCreatedMessage.isDisposed()) {
			this.chartsNotCreatedMessage.dispose();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * Redraws the charts in the dashboard.
	 */
	public void redrawCharts() {
		this.jobCounter = 0;
		for (AbstractChartComposite chart : this.charts) {
			chart.refresh();
		}
	}

	/**
	 * This method starts the chart creation. It starts running the jobs of each
	 * chart container creator on demand.
	 */
	public void createCharts() {
		// dispose message if set
		this.clearChartsNotCreatedMessage();
		this.initializeCharts();

		MutexRule rule = new MutexRule();
		for (AbstractChartComposite chart : this.charts) {
			AbstractDatasetCreator creator = chart.getCreator();
			creator.setRule(rule);
			creator.schedule();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	// INTERFACE AND OVERRIDING METHODS
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite composite) {
		this.container = composite;
		this.addActions();
		this.initialize();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.elexis.actions.GlobalEvents.UserListener#UserChanged()
	 */
	public void UserChanged() {
		// reset job counter
		this.jobCounter = 0;

		// remove job listeners and clear charts
		for (AbstractChartComposite chart : this.charts) {
			chart.removeJobChangeListener(this);
		}
		this.charts.clear();

		// Dispose any children if available
		if (this.container != null) {
			for (Control child : this.container.getChildren()) {
				child.dispose();
			}
		}

		this.initialize(); // re-initialize
	}

	/**
	 * 
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#done(org.eclipse.core
	 *      .runtime.jobs.IJobChangeEvent)
	 */
	public void done(IJobChangeEvent event) {
		// allow other threads to update this UI thread
		// http://www.eclipse.org/swt/faq.php#uithread
		Desk.getDisplay().syncExec(new Runnable() {
			public void run() {
				Dashboard.this.refreshChartsAction.setEnabled(++Dashboard.this.jobCounter == Dashboard.this.charts
						.size());
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void scheduled(IJobChangeEvent event) {
		// allow other threads to update this UI thread
		// http://www.eclipse.org/swt/faq.php#uithread
		Desk.getDisplay().syncExec(new Runnable() {
			public void run() {
				Dashboard.this.createChartsAction.setEnabled(false);
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////////
	// UNUSED INTERFACE METHODS
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Nothing here...
	}

	/**
	 * {@inheritDoc}
	 */
	public void aboutToRun(IJobChangeEvent event) {
		// Nothing here...
	}

	/**
	 * {@inheritDoc}
	 */
	public void awake(IJobChangeEvent event) {
		// Nothing here...
	}

	/**
	 * {@inheritDoc}
	 */
	public void running(IJobChangeEvent event) {
		// Nothing here...
	}

	/**
	 * {@inheritDoc}
	 */
	public void sleeping(IJobChangeEvent event) {
		// Nothing here...
	}
}