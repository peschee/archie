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
import ch.unibe.iam.scg.archie.i18n.Messages;
import ch.unibe.iam.scg.archie.ui.DashboardOverview;
import ch.unibe.iam.scg.archie.ui.GraphicalMessage;
import ch.unibe.iam.scg.archie.ui.charts.AbstractChartComposite;
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

	/** ID of this view. */
	public static final String ID = ArchieActivator.PLUGIN_ID + ".ui.views.Dashboard";

	private ArrayList<AbstractChartComposite> charts;

	private DashboardOverview overview;

	private Composite composite;

	private int jobCounter;

	/**
	 * Creates a Dashboard
	 */
	public Dashboard() {
		this.charts = new ArrayList<AbstractChartComposite>(4);
		GlobalEvents.getInstance().addUserListener(this);
		this.jobCounter = 0;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite composite) {
		this.composite = composite;
		this.initialize();
	}

	/**
	 * Initializes the dashboard. This method is also called when a UserChanged
	 * event is propagated to redraw the contents of the dashboard according to
	 * the current user's access permissions.
	 * 
	 * @param composite
	 *            Parent composite.
	 */
	private void initialize() {
		// Create according to ACL
		if (ArchieACL.userHasAccess()) {
			this.initializeCharts();
		} else {
			this.cancelAllCreators();
			this.initializeAccessDisabled();
		}
		this.composite.layout();
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
	 * @param composite
	 */
	private void initializeCharts() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = true;

		this.composite.setLayout(layout);

		GridData layoutData = new GridData(GridData.FILL_BOTH);
		this.composite.setLayoutData(layoutData);

		// Add dashboard overview
		this.overview = new DashboardOverview(this, this.composite, SWT.NONE);
		this.overview.setEnabled(false);

		// Define layout data for spanning two columns
		GridData spanData = new GridData(GridData.FILL_HORIZONTAL);
		spanData.horizontalSpan = 2;
		this.overview.setLayoutData(spanData);

		this.charts.add(new ConsultationNumberChart(this.composite, SWT.NONE));
		this.charts.add(new ConsultationMoneyChart(this.composite, SWT.NONE));

		this.charts.add(new PatientsConsHistChart(this.composite, SWT.NONE));
		this.charts.add(new AgeHistogrammChart(this.composite, SWT.NONE));

		for (AbstractChartComposite chart : this.charts) {
			chart.addJobChangeListener(this);
		}
	}

	/**
	 * Initialized the access disabled message.
	 * 
	 * @param composite
	 */
	private void initializeAccessDisabled() {
		this.composite.setLayout(new GridLayout());
		this.composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		new GraphicalMessage(this.composite, ArchieActivator.getImage(ArchieActivator.IMG_ERROR),
				Messages.ACL_ACCESS_DENIED);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Nothin here...
	}

	/**
	 * Redraws the charts in the dashboard.
	 */
	public void redrawCharts() {
		this.jobCounter = 0;
		this.overview.setEnabled(false);
		for (AbstractChartComposite chart : this.charts) {
			chart.refresh();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.elexis.actions.GlobalEvents.UserListener#UserChanged()
	 */
	public void UserChanged() {
		this.jobCounter = 0;
		this.charts.clear();

		// remove job listeners
		for (AbstractChartComposite chart : this.charts) {
			chart.removeJobChangeListener(this);
		}

		// Dispose any children if available
		for (Control child : this.composite.getChildren()) {
			child.dispose();
		}

		this.initialize(); // re-initialize
	}

	/** (non-Javadoc)
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#done(org.eclipse.core
	 * .runtime.jobs.IJobChangeEvent)
	 */
	public void done(IJobChangeEvent event) {
		// allow other threads to update this UI thread
		// http://www.eclipse.org/swt/faq.php#uithread
		Desk.getDisplay().syncExec(new Runnable() {
			public void run() {
				Dashboard.this.overview.setEnabled(++Dashboard.this.jobCounter == Dashboard.this.charts.size());
			}
		});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#aboutToRun(org.eclipse
	 * .core.runtime.jobs.IJobChangeEvent)
	 */
	public void aboutToRun(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#awake(org.eclipse.core
	 * .runtime.jobs.IJobChangeEvent)
	 */
	public void awake(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#running(org.eclipse.
	 * core.runtime.jobs.IJobChangeEvent)
	 */
	public void running(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#scheduled(org.eclipse
	 * .core.runtime.jobs.IJobChangeEvent)
	 */
	public void scheduled(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#sleeping(org.eclipse
	 * .core.runtime.jobs.IJobChangeEvent)
	 */
	public void sleeping(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}
}