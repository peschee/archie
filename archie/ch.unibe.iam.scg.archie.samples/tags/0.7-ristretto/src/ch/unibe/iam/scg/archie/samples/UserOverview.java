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
package ch.unibe.iam.scg.archie.samples;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.elexis.data.Anwender;
import ch.elexis.data.Query;
import ch.unibe.iam.scg.archie.model.AbstractDataProvider;
import ch.unibe.iam.scg.archie.samples.i18n.Messages;

/**
 * <p>
 * Simple User Overview
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class UserOverview extends AbstractDataProvider {

	/**
	 * Constructs User Overview
	 */
	public UserOverview() {
		super(Messages.USER_OVERVIEW_TITLE);
	}

	@Override
	protected List<String> createHeadings() {
		final ArrayList<String> headings = new ArrayList<String>(5);

		headings.add(Messages.USER_OVERVIEW_USER);
		headings.add(Messages.USER_OVERVIEW_BIRTHDAY);
		headings.add(Messages.USER_OVERVIEW_GENDER);
		headings.add(Messages.USER_OVERVIEW_VALID);
		headings.add(Messages.USER_OVERVIEW_GROUPS);

		return headings;
	}

	@Override
	protected IStatus createContent(IProgressMonitor monitor) {
		final List<Comparable<?>[]> content = new ArrayList<Comparable<?>[]>(5);

		final Query<Anwender> query = new Query<Anwender>(Anwender.class);
		final List<Anwender> anwenderList = query.execute();

		this.size = anwenderList.size();
		monitor.beginTask("querying database", this.size); // monitoring

		for (final Anwender anwender : anwenderList) {
			// check for cancelation
			if (monitor.isCanceled())
				return Status.CANCEL_STATUS;

			final String valid = (anwender.isValid() == true) ? Messages.USER_OVERVIEW_YES : Messages.USER_OVERVIEW_NO;

			final String group = (anwender.getInfoElement("Groups") != null) ? anwender.getInfoElement("Groups")
					.toString() : Messages.USER_OVERVIEW_UNDEFINED;

			final Comparable<?>[] row = { anwender.getLabel(), anwender.getGeburtsdatum(), anwender.getGeschlecht(), valid,
					group };
			content.add(row);

			monitor.worked(1); // monitoring
		}
		
		// set content
		this.dataSet.setContent(content);

		// job finished successfully
		monitor.done();
		return Status.OK_STATUS;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.archie.model.AbstractDataProvider#getDescription()
	 */
	@Override
	public String getDescription() {
		return Messages.USER_OVERVIEW_DESCRIPTION;
	}
}
