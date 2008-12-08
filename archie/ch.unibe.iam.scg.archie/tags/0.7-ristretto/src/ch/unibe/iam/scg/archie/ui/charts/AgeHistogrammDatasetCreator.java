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

import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jfree.data.general.DefaultKeyedValues2DDataset;

import ch.elexis.data.Patient;
import ch.elexis.data.Person;
import ch.elexis.data.Query;
import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.model.Cohort;
import ch.unibe.iam.scg.archie.preferences.PreferenceConstants;

/**
 * <p>TODO: DOCUMENT ME!</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class AgeHistogrammDatasetCreator extends AbstractDatasetCreator {
	
	private final static int MALE_INDEX = 0;
	private final static int FEMALE_INDEX = 1;
	
	private int cohortSize;

	/**
	 * Creates a AgeHistogrammDatasetCreator
	 * @param jobName 
	 */
	public AgeHistogrammDatasetCreator(String jobName) {
		super(jobName);
		
		int cohortSize = ArchieActivator.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_COHORT_SIZE);
		this.setCohortSize(cohortSize);
	}
	
	/** (non-Javadoc)
	 * @see ch.elexis.actions.BackgroundJob#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus createContent(final IProgressMonitor monitor) {
		this.dataset = new DefaultKeyedValues2DDataset();
		
		Query<Patient> query = new Query<Patient>(Patient.class);
		List<Patient> patients = query.execute();
		
		monitor.beginTask("Querying Database...", patients.size());
		
		// TreeSet with Cohort title as key, and the cohort as value
		TreeMap<Cohort, Cohort> histogramm = new TreeMap<Cohort, Cohort>();
		
		for(Patient patient : patients) {
			// check for cancelation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			int age = 0;
			// We get age as a string (thankyouverymuch) so we have to parse it
			try {
				age = Integer.parseInt(patient.getAlter());
			} catch (NumberFormatException exception) {
				// If the age of a patient was malformated, we just ignore him.
				continue; // gets us out of the loop...
			}
			
			String gender = patient.getGeschlecht();
			
			// If the gender of a patient is neither male nor female, we just ignore him.
			if (!gender.equals(Person.MALE) && !gender.equals(Person.FEMALE)) {
				continue; // gets us out of the loop...
			}
			
			// Calculate bounds of the cohort the current patient fits in
			int lowerBound = ((age / this.cohortSize) * this.cohortSize); // gets rounded down
			int upperBound = lowerBound + (this.cohortSize);
						
			Integer[] genderCount = new Integer[2]; // Empty integer array for male and female count.
			Cohort cohort = new Cohort(lowerBound, upperBound, genderCount);
			
			// No entry for this age group: we create one.
			if (!histogramm.containsKey(cohort)) {

				// We use negative numbers for male count, positive for female count
			
				if (gender.equals(Person.MALE)) {
					genderCount[MALE_INDEX] = -1; // It's a boy!
					genderCount[FEMALE_INDEX] = 0;
					histogramm.put(cohort, cohort);
				}
				// We already checked for malformed gender, so at this point we are sure the patient is female.
				else  {
					genderCount[MALE_INDEX] = 0;
					genderCount[FEMALE_INDEX] = 1; // It's a girl!
					histogramm.put(cohort, cohort);
				}			
			}
			else {
				if (gender.equals(Person.MALE)) {
					Integer[] genderCountTmp = (Integer[]) histogramm.get(cohort).getValue();
					genderCountTmp[MALE_INDEX] -= 1;
				}
				// We already checked for malformed gender, so at this point we are sure the patient is female.
				else {
					Integer[] genderCountTmp = (Integer[]) histogramm.get(cohort).getValue();
					genderCountTmp[FEMALE_INDEX] += 1;
				}
			}
			monitor.worked(1);
		}
		
		for (Entry<Cohort, Cohort> entry : histogramm.entrySet()) {
			// check for cancellation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			Integer[] genderCount = new Integer[2];
			genderCount = (Integer[]) entry.getValue().getValue();
						
			((DefaultKeyedValues2DDataset) this.dataset).addValue(genderCount[MALE_INDEX], "Male", entry.getKey());
			((DefaultKeyedValues2DDataset) this.dataset).addValue(genderCount[FEMALE_INDEX], "Female", entry.getKey());
		}
		
		monitor.done();
		return Status.OK_STATUS;
	}
	
	/**
	 * Sets the cohort size for this chart creator.
	 * @param cohortSize
	 */
	public void setCohortSize(int cohortSize) {
		this.cohortSize = cohortSize;
	}
}