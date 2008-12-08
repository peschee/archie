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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.elexis.data.Prescription;
import ch.elexis.data.Query;
import ch.unibe.iam.scg.archie.model.AbstractTimeSeries;
import ch.unibe.iam.scg.archie.samples.i18n.Messages;

/**
 * <p>
 * Simple Prescriptions Overview
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class PrescriptionsOverview extends AbstractTimeSeries {

	private static final String DATE_DB_FORMAT = "yyyyMMdd";
	private static final String DATE_PRESCRIPTION_FORMAT = "dd.MM.yyyy";
	private static final String DB_START_DATE = "DatumVon";
	private static final String DB_END_DATE = "DatumBis";

	/**
	 * Constructs Prescription Overview
	 */
	public PrescriptionsOverview() {
		super(Messages.PRESCRIPTIONS_OVERVIEW_TITLE);
	}

	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.model.AbstractDataProvider#createHeadings()
	 */
	@Override
	protected List<String> createHeadings() {
		final ArrayList<String> headings = new ArrayList<String>(3);
		headings.add("Name");
		headings.add("Prescribed Count");
		headings.add("Avarage Prescription Time");
		return headings;
	}

	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.model.AbstractDataProvider#getDescription()
	 */
	@Override
	public String getDescription() {
		return Messages.PRESCRIPTIONS_OVERVIEW_DESCRIPTION;
	}

	/** {@inheritDoc} */
	@Override
	public IStatus createContent(IProgressMonitor monitor) {
		// initialize list
		final List<Comparable<?>[]> content = new ArrayList<Comparable<?>[]>();

		// query settings
		final SimpleDateFormat databaseFormat = new SimpleDateFormat(DATE_DB_FORMAT);
		final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PRESCRIPTION_FORMAT);

		Query<Prescription> query = new Query<Prescription>(Prescription.class);
		query.add(DB_END_DATE, ">=", databaseFormat.format(this.getStartDate().getTime()));
		query.add(DB_START_DATE, "<=", databaseFormat.format(this.getEndDate().getTime()));
		List<Prescription> prescriptions = query.execute();

		// set job size and begin task
		this.size = prescriptions.size() * 2; // Double size because we have two
												// loops.
		monitor.beginTask(Messages.DB_QUERYING, this.size); // monitor

		TreeMap<String, List<Prescription>> prescriptionCount = new TreeMap<String, List<Prescription>>();

		// group prescriptions by count
		for (Prescription prescription : prescriptions) {
			// check for cancelation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			String key = prescription.getArtikel().getLabel();
			if (!prescriptionCount.containsKey(key)) {
				ArrayList<Prescription> prescriptionList = new ArrayList<Prescription>();
				prescriptionList.add(prescription);
				prescriptionCount.put(key, prescriptionList);
			} else {
				List<Prescription> prescritionList = prescriptionCount.get(key);
				prescritionList.add(prescription);
			}
			monitor.worked(1); // monitoring
		}

		// compute prescription stats in grouped list
		for (final Entry<String, List<Prescription>> entry : prescriptionCount.entrySet()) {
			// check for cancelation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			final Comparable<?>[] row = new Comparable<?>[this.dataSet.getHeadings().size()];
			row[0] = entry.getKey();
			row[1] = entry.getValue().size();

			long startDate = 0;
			long endDate = 0;
			long durationInMiliSeconds = 0;

			// Get Average Time of Prescription
			for (final Prescription prescription : entry.getValue()) {
				try {
					startDate = (dateFormat.parse(prescription.getBeginDate())).getTime();
					endDate = (dateFormat.parse(prescription.getEndDate())).getTime();
					durationInMiliSeconds += (endDate - startDate);

				} catch (ParseException e) {
					e.printStackTrace();
				}
				monitor.worked(1); // monitoring
			}

			durationInMiliSeconds /= entry.getValue().size();

			row[2] = (durationInMiliSeconds / (24 * 60 * 60 * 1000)) + " days";

			content.add(row);
		}

		// set content
		this.dataSet.setContent(content);

		// job finished successfully
		monitor.done();
		return Status.OK_STATUS;
	}
}