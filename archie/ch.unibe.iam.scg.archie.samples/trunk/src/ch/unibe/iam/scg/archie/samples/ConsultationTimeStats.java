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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.elexis.Hub;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Query;
import ch.rgw.tools.Money;
import ch.unibe.iam.scg.archie.annotations.GetProperty;
import ch.unibe.iam.scg.archie.annotations.SetProperty;
import ch.unibe.iam.scg.archie.model.AbstractTimeSeries;
import ch.unibe.iam.scg.archie.ui.FieldTypes;

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
public class ConsultationTimeStats extends AbstractTimeSeries {

	private static final String DATE_DB_FORMAT = "yyyyMMdd";
	private static final String DATE_CONS_FORMAT = "dd.MM.yyyy";
	private static final String DATE_MONTH_FORMAT = "yyyy-MM";

	private boolean withTime;

	/**
	 * Constructs <code>ConsultationTimeStats</code>.
	 */
	public ConsultationTimeStats() {
		super("Consultation Time");
		this.withTime = true;
	}

	@Override
	protected IStatus createContent(IProgressMonitor monitor) {
		// result list
		final ArrayList<Comparable<?>[]> content = new ArrayList<Comparable<?>[]>();

		// month to consultations map
		final TreeMap<String, ArrayList<Konsultation>> grouped = new TreeMap<String, ArrayList<Konsultation>>();

		// date formats
		final SimpleDateFormat databaseFormat = new SimpleDateFormat(DATE_DB_FORMAT);
		final SimpleDateFormat consultationFormat = new SimpleDateFormat(DATE_CONS_FORMAT);
		final SimpleDateFormat monthFormat = new SimpleDateFormat(DATE_MONTH_FORMAT);

		// prepare query
		final Query<Konsultation> query = new Query<Konsultation>(Konsultation.class);
		query.add("Datum", ">=", databaseFormat.format(this.getStartDate().getTime()));
		query.add("Datum", "<=", databaseFormat.format(this.getEndDate().getTime()));
		query.add("MandantID", "=", Hub.actMandant.getId());

		final List<Konsultation> consults = query.execute();
		
		// define size and begin task
		this.size = consults.size();
		monitor.beginTask("consulttime", this.size);

		// do the light stuff...
		monitor.subTask("consulttime.grouping");
		for (final Konsultation consult : consults) {
			try {
				// check for cancelation
				if(monitor.isCanceled()) return Status.CANCEL_STATUS;
				
				final Date consultDate = consultationFormat.parse(consult.getDatum());
				ArrayList<Konsultation> consultGroup = new ArrayList<Konsultation>();

				final String monthString = monthFormat.format(consultDate);
				if (grouped.get(monthString) != null) {
					consultGroup = grouped.get(monthString);
				}

				// add the current consult to this month group
				consultGroup.add(consult);
				grouped.put(monthString, consultGroup);
			} catch (final ParseException e) {
				e.printStackTrace();
			}
		}

		// do the super heavy stuff ^^
		monitor.subTask("consulttime.computing");
		for (final Entry<String, ArrayList<Konsultation>> entry : grouped.entrySet()) {
			// check for cancelation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			final Comparable<?>[] row = new Comparable<?>[this.dataSet.getHeadings().size()];
			int column = 0;

			// first column is month
			row[column++] = entry.getKey();

			// compute time and money statistics
			int consTotal = 0, total = 0, max = 0;
			double income = 0, spending = 0, profit = 0;

			for (final Konsultation consultation : entry.getValue()) {
				// check for cancelation
				if(monitor.isCanceled()) return Status.CANCEL_STATUS;
				
				// time statistics for a month
				consTotal++;
				if (this.withTime) {
					final int minutes = consultation.getMinutes();
					total += minutes;
					max = (minutes > max) ? minutes : max;
				}

				// money statistics
				income += consultation.getUmsatz();
				spending += consultation.getKosten();

				monitor.worked(1);
			}

			// add all that to the row
			row[column++] = consTotal;

			if (this.withTime) {
				// rounded avg
				final DecimalFormat df = new DecimalFormat("0.00");
				final String avg = df.format((double) total / entry.getValue().size());

				row[column++] = total;
				row[column++] = max;
				row[column++] = avg;
			}

			// compute profit
			profit = income - spending;

			row[column++] = new Money(income / 100);
			row[column++] = new Money(spending / 100);
			row[column++] = new Money(profit / 100);

			content.add(row);
		}
		
		// set heading and content
		this.dataSet.setContent(content);
		
		monitor.done();
		return Status.OK_STATUS;
	}

	@Override
	protected List<String> createHeadings() {
		final ArrayList<String> headings = new ArrayList<String>();
		headings.add("Date");
		headings.add("Consultations Total");

		// time statistics
		if (this.withTime) {
			headings.add("Time Total");
			headings.add("Time Max");
			headings.add("Time Average");
		}

		// money statistics
		headings.add("Income");
		headings.add("Spending");
		headings.add("Profit");

		return headings;
	}

	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.model.AbstractDataProvider#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Creates statistics about consultation times."
				+ "This includes the total number of consultation minutes"
				+ "as well as the minimum, maximum and average grouped by month.";
	}

	/**
	 * @return whithTime
	 */
	@GetProperty(name = "Include Time", index = 1, fieldType = FieldTypes.BUTTON_CHECKBOX, description = "Include time statistics for consultations.")
	public boolean getShowTime() {
		return this.withTime;
	}

	/**
	 * @param showTime
	 */
	@SetProperty(name = "Include Time")
	public void setShowTime(final boolean showTime) {
		this.withTime = showTime;
	}
}