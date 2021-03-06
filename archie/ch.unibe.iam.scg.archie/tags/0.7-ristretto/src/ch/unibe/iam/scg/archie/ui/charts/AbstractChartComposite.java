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
package ch.unibe.iam.scg.archie.ui.charts;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.elexis.Desk;
import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.i18n.Messages;
import ch.unibe.iam.scg.archie.ui.GraphicalMessage;

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
public abstract class AbstractChartComposite extends Composite implements IJobChangeListener {

	protected Composite parent;
	protected AbstractDatasetCreator creator;
	protected JFreeChart chart;

	private ChartComposite chartComposite;
	private GridData layoutData;

	/**
	 * Default constructor.
	 * 
	 * @param parent
	 * @param style
	 */
	public AbstractChartComposite(final Composite parent, final int style) {
		super(parent, style);
		this.parent = parent;

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		this.layoutData = new GridData(GridData.FILL_BOTH);

		this.setLoadingMessage();

		this.setLayout(layout);
		this.setLayoutData(layoutData);

		this.creator = this.initializeCreator();
		this.creator.addJobChangeListener(this);
		this.creator.schedule();
	}

	// ////////////////////////////////////////////////////////////////////////////
	// ABSTRACT METHODS
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * Initialized the dataset creator. Subclasses have to initialize their
	 * specialized creators.
	 * 
	 * @return A JFreeChart dataset creator.
	 */
	abstract protected AbstractDatasetCreator initializeCreator();

	/**
	 * Initializes the chart.
	 * 
	 * @return An initialized chart object.
	 */
	abstract protected JFreeChart initializeChart();

	// ////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * Display loading screen
	 */
	public void setLoadingMessage() {
		new GraphicalMessage(this, ArchieActivator.getDefault().getImageRegistry().get(ArchieActivator.IMG_COFFEE),
				Messages.WORKING);
		this.layout();
	}

	/**
	 * Refreshes this objects dataset creator
	 */
	public void refresh() {
		this.clean();
		this.setLoadingMessage();
		this.creator.schedule();
	}

	/**
	 * Cleans this AbstractChartComposite of all content.
	 */
	public void clean() {
		if (this.chartComposite != null) {
			this.chartComposite.dispose();
		}
		for (Control child : this.getChildren()) {
			child.dispose();
		}
	}

	/**
	 * TODO: DOCUMENT ME!
	 */
	public void cancelCreator() {
		this.creator.cancel();
	}

	/**
	 * @param listener
	 */
	public void addJobChangeListener(IJobChangeListener listener) {
		this.creator.addJobChangeListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removeJobChangeListener(IJobChangeListener listener) {
		this.creator.removeJobChangeListener(listener);

	}

	// ////////////////////////////////////////////////////////////////////////////
	// INTERFACE METHODS
	// ////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#done(org.eclipse.core
	 * .runtime.jobs.IJobChangeEvent)
	 */
	public void done(IJobChangeEvent event) {
		// allow other threads to update this UI thread
		// http://www.eclipse.org/swt/faq.php#uithread
		Desk.getDisplay().syncExec(new Runnable() {
			public void run() {
				AbstractChartComposite.this.clean();

				AbstractChartComposite.this.chartComposite = new ChartComposite(AbstractChartComposite.this, SWT.NONE,
						AbstractChartComposite.this.initializeChart());
				AbstractChartComposite.this.chartComposite.setLayoutData(AbstractChartComposite.this.layoutData);

				AbstractChartComposite.this.layout();
			}
		});
	}

	// /////////////////////////////////////////////////////////////////////////////
	// UNUSED INTERFACE METHODS
	// /////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#aboutToRun(org.eclipse
	 * .core.runtime.jobs.IJobChangeEvent)
	 */
	public void aboutToRun(IJobChangeEvent event) {
		// nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#awake(org.eclipse.core
	 * .runtime.jobs.IJobChangeEvent)
	 */
	public void awake(IJobChangeEvent event) {
		// nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#running(org.eclipse.
	 * core.runtime.jobs.IJobChangeEvent)
	 */
	public void running(IJobChangeEvent event) {
		// nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#scheduled(org.eclipse
	 * .core.runtime.jobs.IJobChangeEvent)
	 */
	public void scheduled(IJobChangeEvent event) {
		// nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.IJobChangeListener#sleeping(org.eclipse
	 * .core.runtime.jobs.IJobChangeEvent)
	 */
	public void sleeping(IJobChangeEvent event) {
		// nothing
	}
}