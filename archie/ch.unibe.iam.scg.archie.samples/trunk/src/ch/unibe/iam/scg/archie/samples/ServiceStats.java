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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.elexis.Hub;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Query;
import ch.elexis.data.Verrechnet;
import ch.rgw.tools.Money;
import ch.unibe.iam.scg.archie.annotations.GetProperty;
import ch.unibe.iam.scg.archie.annotations.SetProperty;
import ch.unibe.iam.scg.archie.model.AbstractTimeSeries;
import ch.unibe.iam.scg.archie.samples.i18n.Messages;
import ch.unibe.iam.scg.archie.ui.FieldTypes;

/**
 * <p>Generates an overview of the services provided in a given timeframe.</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ServiceStats extends AbstractTimeSeries {

	private static final String DATE_DB_FORMAT = "yyyyMMdd";
	private static final String SERVICE_CLASS_DB_FIELD = "Klasse";

	private boolean currentMandatorOnly;
	private boolean groupByServiceClass;

	/**
	 * Costructs ServiceStats
	 */
	public ServiceStats() {
		super(Messages.SERVICES_TITLE);
		this.currentMandatorOnly = true;
		this.groupByServiceClass = false;

	}

	/** {@inheritDoc} */
	@Override
	protected IStatus createContent(IProgressMonitor monitor) {
		final SimpleDateFormat databaseFormat = new SimpleDateFormat(DATE_DB_FORMAT);

		// Prepare DB query.
		final Query<Konsultation> query = new Query<Konsultation>(Konsultation.class);
		query.add("Datum", ">=", databaseFormat.format(this.getStartDate().getTime()));
		query.add("Datum", "<=", databaseFormat.format(this.getEndDate().getTime()));
		if (this.currentMandatorOnly) {
			query.add("MandantID", "=", Hub.actMandant.getId());
		}

		// Get all Consultation which happened in the specified date range.
		final List<Konsultation> consultations = query.execute();

		this.size = consultations.size();
		monitor.beginTask(Messages.DB_QUERYING, this.size); // monitoring

		TreeMap<String, ArrayList<Verrechnet>> services = new TreeMap<String, ArrayList<Verrechnet>>();

		// Go through all consultations.
		for (Konsultation consultation : consultations) {

			// Check for cancellation.
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// Go through all services.
			List<Verrechnet> consServices = consultation.getLeistungen();
			for (Verrechnet service : consServices) {

				// Check for cancellation.
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}

				// Group by label...
				String key = service.getLabel();

				// ..or by service class?
				if (this.groupByServiceClass) {

					// Take the last token of the classname, delimited by a
					// period.
					StringTokenizer tokenizer = new StringTokenizer(service.get(SERVICE_CLASS_DB_FIELD), ".");
					while (tokenizer.hasMoreTokens()) {
						key = tokenizer.nextToken(); // The last one stays.
					}
				}

				ArrayList<Verrechnet> servicesList = !services.containsKey(key) ? new ArrayList<Verrechnet>()
						: services.get(key);

				servicesList.add(service);
				services.put(key, servicesList);
			}
			monitor.worked(1);
		}

		// Create dataset result
		final ArrayList<Comparable<?>[]> result = new ArrayList<Comparable<?>[]>();

		// Go over all services we stored and create actual dataset.
		for (final Entry<String, ArrayList<Verrechnet>> entry : services.entrySet()) {
			
			// Check for cancellation
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			
			double cost = 0, income = 0;
			
			// Sum up service list values
			for(Verrechnet service : entry.getValue()) {
				cost += service.getKosten().doubleValue();
				income += service.getBruttoPreis().doubleValue();
			}

			final Comparable<?>[] row = new Comparable[this.dataSet.getHeadings().size()];
			int i = 0;
			
			row[i++] = entry.getKey();
			row[i++] = entry.getValue().size();
			row[i++] = new Money(cost / 100);
			row[i++] = new Money(income / 100);
			row[i++] = new Money((income - cost) / 100);
			
			result.add(row);
		}

		// Set content.
		this.dataSet.setContent(result);

		monitor.done();
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	protected List<String> createHeadings() {
		final ArrayList<String> headings = new ArrayList<String>(2);
		headings.add(Messages.SERVICES_HEADING_SERVICE);
		headings.add(Messages.SERVICES_HEADING_AMOUNT);
		headings.add(Messages.SERVICES_HEADING_COSTS);
		headings.add(Messages.SERVICES_HEADING_INCOME);
		headings.add(Messages.SERVICES_HEADING_PROFITS);
		return headings;
	}

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return Messages.SERVICES_DESCRIPTION;
	}

	/**
	 * @return currentMandatorOnly
	 */
	@GetProperty(name = "Active Mandator Only", index = 3, fieldType = FieldTypes.BUTTON_CHECKBOX, description = "Compute statistics only for the current mandator. If unchecked, the statistics will be computed for all mandators.")
	public boolean getCurrentMandatorOnly() {
		return this.currentMandatorOnly;
	}

	/**
	 * @param currentMandatorOnly
	 */
	@SetProperty(name = "Active Mandator Only")
	public void setCurrentMandatorOnly(final boolean currentMandatorOnly) {
		this.currentMandatorOnly = currentMandatorOnly;
	}

	/**
	 * @return groupByServiceClass
	 */
	@GetProperty(name = "Groupy By Class", index = 5, fieldType = FieldTypes.BUTTON_CHECKBOX, description = "Groups Services by their general groups.")
	public boolean getgroupByServiceClass() {
		return this.groupByServiceClass;
	}

	/**
	 * @param groupByServiceClass
	 */
	@SetProperty(name = "Groupy By Class")
	public void setgroupByServiceClass(final boolean groupByServiceClass) {
		this.groupByServiceClass = groupByServiceClass;
	}

}
