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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jfree.data.statistics.Statistics;

import ch.elexis.Hub;
import ch.elexis.data.IDiagnose;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Patient;
import ch.elexis.data.Query;
import ch.unibe.iam.scg.archie.annotations.GetProperty;
import ch.unibe.iam.scg.archie.annotations.SetProperty;
import ch.unibe.iam.scg.archie.model.AbstractTimeSeries;
import ch.unibe.iam.scg.archie.samples.i18n.Messages;
import ch.unibe.iam.scg.archie.ui.FieldTypes;

/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class DiagnoseStats extends AbstractTimeSeries {

	/**
	 * Date format for data that comes from the database.
	 */
	private static final String DATE_DB_FORMAT = "yyyyMMdd";

	private boolean currentMandatorOnly;

	/**
	 * Constructs DiagnoseStats
	 */
	public DiagnoseStats() {
		super("Diagnose Statistics");
		this.currentMandatorOnly = true;
	}

	@Override
	protected IStatus createContent(IProgressMonitor monitor) {
		// result list
		final ArrayList<Comparable<?>[]> content = new ArrayList<Comparable<?>[]>();
		final TreeMap<String, List<Patient>> diagnoseMap = new TreeMap<String, List<Patient>>();

		// form query
		final SimpleDateFormat databaseFormat = new SimpleDateFormat(DATE_DB_FORMAT);
		final Query<Konsultation> query = new Query<Konsultation>(Konsultation.class);

		query.add("Datum", ">=", databaseFormat.format(this.getStartDate().getTime()));
		query.add("Datum", "<=", databaseFormat.format(this.getEndDate().getTime()));

		if (this.currentMandatorOnly) {
			query.add("MandantID", "=", Hub.actMandant.getId());
		}

		final List<Konsultation> consults = query.execute();

		this.size = consults.size();
		monitor.beginTask(Messages.DB_QUERYING, this.size);

		// get consultations and their patient and diagnoses stats and put them
		// all in a map that we can process later
		for (Konsultation consult : consults) {
			// check for cancelation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			List<IDiagnose> diagnoses = consult.getDiagnosen();
			Patient patient = consult.getFall().getPatient();

			for (IDiagnose diagnose : diagnoses) {
				List<Patient> patientList = diagnoseMap.get(diagnose.getLabel());

				if (patientList != null) {
					patientList.add(patient);
				} else {
					ArrayList<Patient> list = new ArrayList<Patient>();
					list.add(patient);
					diagnoseMap.put(diagnose.getLabel(), list);
				}
			}
			monitor.worked(1);
		}

		// build up result list from diagnose map
		for (Entry<String, List<Patient>> entry : diagnoseMap.entrySet()) {
			// check for cancelation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			Comparable<?>[] row = new Comparable<?>[this.dataSet.getHeadings().size()];
			List<Patient> patients = entry.getValue();
			int column = 0;

			row[column++] = entry.getKey();
			row[column++] = patients.size();

			// compute patient age stats
			double ageMin = 10000, ageMax = 0, ageTotal = 0, ageMedian = 0;
			ArrayList<Integer> ageList = new ArrayList<Integer>();
			for (Patient patient : patients) {
				Integer age = new Integer(patient.getAlter());
				ageList.add(age);
				ageMin = (age < ageMin) ? age : ageMin;
				ageMax = (age > ageMax) ? age : ageMax;
				ageTotal += age;
			}

			// sort ages and compute median
			ageMedian = Statistics.calculateMedian(ageList);

			final DecimalFormat df = new DecimalFormat("0.0");
			final String ageAvg = df.format((double) ageTotal / patients.size());

			row[column++] = ageMin;
			row[column++] = ageMax;
			row[column++] = ageAvg;
			row[column++] = ageMedian;

			content.add(row);
		}
		
		// set content
		this.dataSet.setContent(content);

		// job finished successfully
		monitor.done();
		return Status.OK_STATUS;
	}

	@Override
	protected List<String> createHeadings() {
		final ArrayList<String> headings = new ArrayList<String>();
		headings.add("Diagnose");
		headings.add("Count");
		headings.add("Age Min");
		headings.add("Age Max");
		headings.add("Age Avg");
		headings.add("Age Median");
		return headings;
	}

	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.model.AbstractDataProvider#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Generates statistics about common diagnoses, their costs and age distribution.";
	}

	/**
	 * @return currentMandatorOnly
	 */
	@GetProperty(name = "Active Mandator Only", index = 1, fieldType = FieldTypes.BUTTON_CHECKBOX, description = "Compute statistics only for the current mandant. If unchecked, the statistics will be computed for all mandants.")
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
}
