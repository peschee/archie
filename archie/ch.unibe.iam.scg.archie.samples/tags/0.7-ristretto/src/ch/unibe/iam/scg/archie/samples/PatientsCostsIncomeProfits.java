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
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.elexis.Hub;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Patient;
import ch.elexis.data.Query;
import ch.rgw.tools.Money;
import ch.unibe.iam.scg.archie.annotations.GetProperty;
import ch.unibe.iam.scg.archie.annotations.SetProperty;
import ch.unibe.iam.scg.archie.model.AbstractTimeSeries;
import ch.unibe.iam.scg.archie.samples.i18n.Messages;
import ch.unibe.iam.scg.archie.ui.FieldTypes;

/**
 * <p>
 * Shows all patients and costs, income and profits of their consultations in the given timeframe.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class PatientsCostsIncomeProfits extends AbstractTimeSeries {

	/**
	 * Shows only patients for active mandator if true, all patients in the
	 * system else.
	 */
	private boolean currentMandatorOnly;

	/**
	 * Date format for data that comes from the database.
	 */
	private static final String DATE_DB_FORMAT = "yyyyMMdd";

	/**
	 * Construct Patient Costs Statistics
	 */
	public PatientsCostsIncomeProfits() {
		super(Messages.PATIENTS_PROFITS_TITLE);
		this.currentMandatorOnly = true;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return Messages.PATIENTS_PROFITS_TITLE;
	}
	
	/** {@inheritDoc} */
	@Override
	protected List<String> createHeadings() {
		ArrayList<String> headings = new ArrayList<String>(4);
		headings.add(Messages.PATIENTS_PROFITS_HEADING_PATIENT);
		headings.add(Messages.PATIENTS_PROFITS_HEADING_COSTS);
		headings.add(Messages.PATIENTS_PROFITS_HEADING_INCOME);
		headings.add(Messages.PATIENTS_PROFITS_HEADING_PROFIT);

		return headings;
	}
	
	/** {@inheritDoc} */
	@Override
	protected IStatus createContent(IProgressMonitor monitor) {

		final SimpleDateFormat databaseFormat = new SimpleDateFormat(DATE_DB_FORMAT);

		// Prepare DB query
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

		// Patients TreeMap will hold patients names and their costs, income, profits
		TreeMap<String, ArrayList<Money>> patients = new TreeMap<String, ArrayList<Money>>();
		
		for (Konsultation consultation : consultations) {
			// Check for user cancellation
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			
			// Holds costs, income and profit of a patient for all his consultations.
			ArrayList<Money> patientsContent = new ArrayList<Money>(3);
			
			Patient currentPatient = consultation.getFall().getPatient();
			
			// Patient isn't in our our TreeMap so far, add him and this consultation data.
			if (!patients.containsKey(currentPatient.getLabel())) {
				// CAVEAT: Money(int) is constructed with cents, Money(double) isn't. We have to divide by 100 :(
				patientsContent.add(new Money(((Integer)consultation.getKosten()).doubleValue()/100));
				patientsContent.add(new Money(consultation.getUmsatz()/100));
				patientsContent.add(new Money(consultation.getGewinn()/100));
				patients.put(currentPatient.getLabel(), patientsContent);
			}
			// Patient is already in your TreeMap, add this consultation data to the one we already have.
			else {
				ArrayList<Money> patientsContentOld = patients.get(currentPatient.getLabel());
				// CAVEAT: Money.addAmount expects cents, we don'd divide by 100.
				patientsContentOld.get(0).addAmount(((Integer)consultation.getKosten()).doubleValue()/100);
				patientsContentOld.get(1).addAmount(consultation.getUmsatz()/100);
				patientsContentOld.get(2).addAmount(consultation.getGewinn()/100);
			}
			monitor.worked(1); // monitoring
		}

		List<Comparable<?>[]> datasetContent = new ArrayList<Comparable<?>[]>();
		
		for (final Entry<String, ArrayList<Money>> entry : patients.entrySet()) {
			Comparable<?>[] row = { entry.getKey(), entry.getValue().get(0), entry.getValue().get(1), entry.getValue().get(2)};
			datasetContent.add(row);
		}
		
		// set dataset content
		this.dataSet.setContent(datasetContent);

		// job finished successfully
		monitor.done();
		return Status.OK_STATUS;
	}

	/**
	 * @return true if only patients for active mendate should be shown, false
	 *         if all patients should be shown.
	 */
	@GetProperty(name = "Active Mandator Only", index = 3, description = "Show only patients which belong to active mandator", fieldType = FieldTypes.BUTTON_CHECKBOX)
	public boolean getShowForMandate() {
		return this.currentMandatorOnly;
	}

	/**
	 * @param showForMandate
	 */
	@SetProperty(name = "Active Mandator Only")
	public void setShowForMandate(final boolean showForMandate) {
		this.currentMandatorOnly = showForMandate;
	}
}