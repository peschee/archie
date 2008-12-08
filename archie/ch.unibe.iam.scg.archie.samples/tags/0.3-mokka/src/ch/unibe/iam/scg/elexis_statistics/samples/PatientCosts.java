package ch.unibe.iam.scg.elexis_statistics.samples;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.elexis.Hub;
import ch.elexis.actions.AbstractDataLoaderJob;
import ch.elexis.data.Fall;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Patient;
import ch.elexis.util.Money;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractTimeSeries;
import ch.unibe.iam.scg.elexis_statistics.utils.QueryUtil;

/**
 * Simple Patient Costs Overview
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class PatientCosts extends AbstractTimeSeries {

	/** Loader which loads all patients from the database */
	private AbstractDataLoaderJob loader = (AbstractDataLoaderJob) Hub.jobPool
			.getJob("PatientenListe");

	public PatientCosts() {
		super("Patient Costs");
	}

	@Override
	protected List<Object[]> createContent() {
		List<Object[]> list = new ArrayList<Object[]>();

		Object[] patients = (Object[]) loader.getData();
		this.size = patients.length;
		this.monitor.beginTask("Querying database", this.size); // monitoring

		for (Object pat : patients) {
			Patient patient = (Patient) pat;
			this.monitor.worked(1); // monitoring
			Money costs = this.handleCases(patient);

			Object[] row = { patient.getLabel(), costs };
			list.add(row);
		}

		return list;
	}

	@Override
	protected List<String> createHeadings() {
		ArrayList<String> headings = new ArrayList<String>();

		headings.add("Patient");
		headings.add("Total Costs");

		return headings;
	}

	@Override
	protected void initializeDefaultValues() {
		// Nothing here.
	}

	/*
	 * PRIVATE METHODS
	 */

	private Money handleCases(Patient patient) {
		double costs = 0;
		Fall[] faelle = patient.getFaelle();

		for (Fall fall : faelle) {
			costs += this.handleConsultation(fall);
		}
		costs /= 100; // not in cents
		return new Money(costs);
	}

	private double handleConsultation(Fall fall) {
		double costs = 0;
		Konsultation[] cons = fall.getBehandlungen(false);

		for (Konsultation konsultation : cons) {
			if (this.inPeriod(konsultation.getDatum())) {
				costs += konsultation.getUmsatz();
			}
		}

		return costs;
	}

	/**
	 * returns true if the given Fall is in the period defined. Format:
	 * dd.mm.yyyy
	 */
	private boolean inPeriod(String date) {

		Calendar givenDate = Calendar.getInstance();

		try {
			givenDate = QueryUtil.convertToCalendar(date);
		} catch (Exception e) {
			// TODO log
			e.printStackTrace();
		}

		return (givenDate.compareTo(this.getStartDate()) >= 0 && givenDate
				.compareTo(this.getEndDate()) <= 0);
	}

	@Override
	public String getDescription() {
		return "Creates a table with all patients which had a " + 
		"consultation in the given timespan, and the " + 
		"total costs, for these patients.";
	}
}