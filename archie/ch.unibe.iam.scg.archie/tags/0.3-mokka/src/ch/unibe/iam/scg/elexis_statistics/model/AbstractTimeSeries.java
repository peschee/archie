package ch.unibe.iam.scg.elexis_statistics.model;

import java.util.Calendar;

import ch.unibe.iam.scg.elexis_statistics.annotations.GetProperty;
import ch.unibe.iam.scg.elexis_statistics.annotations.SetProperty;
import ch.unibe.iam.scg.elexis_statistics.ui.FieldTypes;
import ch.unibe.iam.scg.elexis_statistics.utils.QueryUtil;


/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public abstract class AbstractTimeSeries extends AbstractDataProvider {

	/**
	 * TODO: Document me.
	 */
	private Calendar startDate;

	/**
	 * TODO: Document me.
	 */
	private Calendar endDate;

	public AbstractTimeSeries(String name) {
		super(name);
		this.initDates();
	}

	private void initDates() {
		this.setStartDate(Calendar.getInstance());
		this.getStartDate().set(getStartDate().get(Calendar.YEAR), Calendar.JANUARY, 1);

		this.setEndDate(Calendar.getInstance());
		this.getEndDate().set(this.getEndDate().get(Calendar.YEAR), Calendar.DECEMBER, 31);
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getStartDate() {
		return this.startDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public Calendar getEndDate() {
		return this.endDate;
	}

	/* ------------------------meta accessor methods--------------------------- */

	/** Get the start date of this query. Inclusive. */
	@GetProperty(name = "Start Date", index = -2, fieldType = FieldTypes.DATE, validationRegex = "\\d{2}\\.\\d{2}\\.\\d{4}", validationMessage = "Datumsformat bla...")
	public String metaGetStartDate() {
		return QueryUtil.convertFromCalendar(this.getStartDate());
	}

	/**
	 * Set the start date of this query. Inclusive the given date. Format of the
	 * string has to be d[d].m[m].yyyy
	 * 
	 * @throws SetDataException
	 */
	@SetProperty(name = "Start Date", index = -2)
	public void metaSetStartDate(String startDate) throws SetDataException {
		Calendar cal;
		try {
			cal = QueryUtil.convertToCalendar(startDate);
			cal.get(Calendar.DAY_OF_MONTH); // these throw IllegalArgument...
			cal.get(Calendar.MONTH);
			cal.get(Calendar.YEAR);
		} catch (NumberFormatException e) { // converting failure
			throw new SetDataException("Anfangsdatum nicht im richtigen Format. "
					+ "Bitte in folgendem Format angeben: dd.mm.yyy");
		} catch (IllegalArgumentException e) { // illegal date
			throw new SetDataException("Das Anfangsdatum ist kein valides Datum.");
		}
		this.setStartDate(cal);
	}

	/** Get the end date of this query. Inclusive. */
	@GetProperty(name = "End Date", fieldType = FieldTypes.DATE, validationRegex = "\\d{2}\\.\\d{2}\\.\\d{4}", validationMessage = "Datumsformat blubb...")
	public String metaGetEndDate() {
		return QueryUtil.convertFromCalendar(this.getEndDate());
	}

	/**
	 * Set the end date of this query. Inclusive the given date. The string has
	 * to be in this format: d[d].m[m].yyyy
	 * 
	 * @throws SetDataException
	 */
	@SetProperty(name = "End Date")
	public void metaSetEndDate(String endDate) throws SetDataException {
		Calendar cal;
		try {
			cal = QueryUtil.convertToCalendar(endDate);
			cal.get(Calendar.DAY_OF_MONTH);// these throw IllegalArgument...
			cal.get(Calendar.MONTH);
			cal.get(Calendar.YEAR);
		} catch (NumberFormatException e) { // converting failure
			throw new SetDataException("Enddatum nicht im richtigen Format. "
					+ "Bitte in folgendem Format angeben: dd.mm.yyyy");
		} catch (IllegalArgumentException e) { // illegal date
			throw new SetDataException("Das Enddatum ist kein gültiges Datum. "
					+ "Bitte geben Sie ein gültiges Datum ein.");
		}
		if (cal.compareTo(this.getStartDate()) < 0) {
			throw new SetDataException("Enddatum vor Anfangsdatum. Bitte ändern Sie das Start- "
					+ "oder Enddatum der Auswertung.");
		}
		this.setEndDate(cal);
	}

}
