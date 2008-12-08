package ch.unibe.iam.scg.elexis_statistics.samples;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.elexis.Hub;
import ch.elexis.actions.AbstractDataLoaderJob;
import ch.elexis.data.Fall;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Patient;
import ch.unibe.iam.scg.elexis_statistics.annotations.GetProperty;
import ch.unibe.iam.scg.elexis_statistics.annotations.SetProperty;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractTimeSeries;
import ch.unibe.iam.scg.elexis_statistics.model.SetDataException;
import ch.unibe.iam.scg.elexis_statistics.samples.i18n.Messages;
import ch.unibe.iam.scg.elexis_statistics.ui.FieldTypes;
import ch.unibe.iam.scg.elexis_statistics.utils.QueryUtil;

public class ConsultationStats extends AbstractTimeSeries {

	/** Loader which loads all patients from the database */
	private AbstractDataLoaderJob loader = (AbstractDataLoaderJob) Hub.jobPool.getJob("PatientenListe");

	/**
	 * This value is the first birth cohort. The value is determined when
	 * iterating through all patients. The initial value is set to the actual
	 * year.
	 */
	private int firstCohort;

	/** The cohort's size. */
	private int cohortSize;

	public ConsultationStats() {
		super(Messages.CONSULTATION_STATS_TITLE);
	}

	protected ArrayList<String> createHeadings() {
		ArrayList<String> headings = new ArrayList<String>();
		headings.add(Messages.CONSULTATION_STATS_BIRTHYEAR);
		headings.add(Messages.CONSULTATION_STATS_TOTAL_COSTS);
		headings.add(Messages.CONSULTATION_STATS_NUMBER_OF_CONSULTATIONS);
		headings.add(Messages.CONSULTATION_STATS_AVERAGE_COSTS);
		return headings;
	}

	protected void initializeDefaultValues() {
		this.cohortSize = 1;
	}

	protected List<Object[]> createContent() {

		Object[] patients = (Object[]) loader.getData();

		this.size = patients.length; // required by BackgroundJob
		this.monitor.beginTask("querying database", this.size); // monitor

		this.initList(patients);

		for (int i = 0; i < patients.length; i++) {
			Patient patient = (Patient) patients[i];
			this.monitor.worked(1); // monitor

			int year = QueryUtil.extractYear(patient.getGeburtsdatum());
			if (year != 0) {
				this.handleCases(patient, year);
			}

		}

		// all values are converted to doubles
		this.setContent(QueryUtil.createCohorts(this.getContent(), this.cohortSize));

		this.setContent(QueryUtil.addAverage(this.getContent(), 1, 2, 3));

		QueryUtil.convertDoubleToInteger(this.getContent(), 2); // consultation
		// count.

		QueryUtil.convertToCurrency(this.getContent(), 1);
		QueryUtil.convertToCurrency(this.getContent(), 3);

		return this.getContent();
	}

	private void setContent(List<Object[]> list) {
		this.dataSet.setContent(list);
	}

	private List<Object[]> getContent() {
		return this.dataSet.getContent();
	}

	/**
	 * Determines the first birth year of all patients and initializes the list
	 * containing all information. The first year in the resulting list is
	 * determined by the modulo 5 of the oldest patients birth year.
	 */
	private void initList(Object[] patients) {
		this.firstCohort = Calendar.getInstance().get(Calendar.YEAR);

		for (int i = 0; i < patients.length; i++) {
			int birthYear = QueryUtil.extractYear(((Patient) patients[i]).getGeburtsdatum());

			if (this.firstCohort > birthYear && birthYear > 0) {
				this.firstCohort = this.determineFirstYear(birthYear);
			}
		}

		// year, total costs (consultation), No of consultation, average costs
		this.setContent(QueryUtil.initiateYears(firstCohort, 4));
	}

	/**
	 * Returns an integer with the property: integer % 5 == 0 && integer <=
	 * birthYear.
	 */
	private int determineFirstYear(int birthYear) {
		if (birthYear % 5 == 0) {
			return birthYear;
		} else {
			int diff = birthYear % 5;
			assert ((birthYear - diff) % 5 == 0);
			return (birthYear - diff);
		}
	}

	private void handleCases(Patient patient, int birthYear) {
		Fall[] faelle = patient.getFaelle();

		for (int i = 0; i < faelle.length; i++) {
			this.handleConsultaion(faelle[i], patient, birthYear);
		}
	}

	/**
	 * returns true if the given date is in the period defined. Format:
	 * dd.mm.yyyy
	 */
	private boolean inPeriod(String date) {

		Calendar fallDate = Calendar.getInstance();

		try {
			fallDate = QueryUtil.convertToCalendar(date);
		} catch (Exception e) {
			// TODO log
			e.printStackTrace();
		}

		return (fallDate.compareTo(this.getStartDate()) >= 0 && fallDate.compareTo(this.getEndDate()) <= 0);
	}

	private void handleConsultaion(Fall fall, Patient patient, int birthYear) {
		Konsultation[] consultations = fall.getBehandlungen(false);

		for (int i = 0; i < consultations.length; i++) {
			Konsultation cons = consultations[i];
			if (this.inPeriod(cons.getDatum())) {

				this.getContent().get(birthYear - firstCohort)[1] = (Integer) this.getContent().get(birthYear - firstCohort)[1]
						+ ((Double) cons.getUmsatz()).intValue();

				this.getContent().get(birthYear - firstCohort)[2] = (Integer) this.getContent().get(birthYear - firstCohort)[2] + 1;
			}
		}
	}

	/** {@inheritDoc} */
	public String getDescription() {
		return Messages.CONSULTATION_STATS_DESCRIPTION;
	}

	/*-------------------------- meta data ---------------------------------*/
	/*
	 * The following methods are getter and setter pairs which describe all
	 * fields which can be changed by the user. Each pair is annotated since all
	 * information is gained by reflection with the java reflection framework.
	 */

	@GetProperty(
			name = "Cohort Size", 
			index = 2, 
			fieldType = FieldTypes.NUMERIC, 
			validationRegex = "\\d{1,3}", 
			validationMessage = "This field has to consist of at least one, at most three numbers."
	)
	public String getCohortSize() {
		return "" + this.cohortSize;
	}

	@SetProperty(name = "Cohort Size")
	public void setCohortSize(String cohortSize) throws SetDataException {
		Integer size = new Integer(1);
		try {
			size = new Integer(cohortSize);
			if (size < 1) {
				throw new Exception(); // size must be greater or equal 1
			}
			this.cohortSize = size;
		} catch (Exception e) {
			throw new SetDataException(Messages.CONSULTATION_STATS_COHORT_SIZE_EXCEPTION);
		}
	}
}