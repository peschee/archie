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
package ch.unibe.iam.scg.archie.ui.charts;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.jfree.data.general.AbstractDataset;

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
public abstract class AbstractDatasetCreator extends Job {

	protected AbstractDataset dataset;

	/**
	 * Creates AbstractDatasetCreator
	 * 
	 * @param jobName
	 */
	public AbstractDatasetCreator(String jobName) {
		super(jobName);
	}

	/**
	 * Returns the dataset for this creator.
	 * 
	 * @return Created dataset.
	 */
	public AbstractDataset getDataset() {
		return this.dataset;
	}

	/**
	 * Creates the content for this dataset creator. Subclasses need to
	 * implement this method and do their main work in here - create the
	 * dataset.
	 * 
	 * @return Status that reflect the outcome of the content creation.
	 */
	public abstract IStatus createContent(IProgressMonitor monitor);

	/**
	 * This method runs the job. In this implementation, this means calling the
	 * content creation method of a dataset creator.
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
	 *      IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		return this.createContent(monitor);
	}
}