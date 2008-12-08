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

/**
 * TODO: Document.
 */
package ch.unibe.iam.scg.archie.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.osgi.util.NLS;

import ch.unibe.iam.scg.archie.annotations.GetProperty;
import ch.unibe.iam.scg.archie.annotations.SetProperty;
import ch.unibe.iam.scg.archie.i18n.Messages;
import ch.unibe.iam.scg.archie.ui.FieldTypes;
import ch.unibe.iam.scg.archie.ui.fields.DateTextFieldComposite;

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

	/**
	 * @param name
	 */
	public AbstractTimeSeries(final String name) {
		super(name);
		this.initDates();
	}

	private void initDates() {
		this.setStartDate(Calendar.getInstance());
		this.getStartDate().set(this.getStartDate().get(Calendar.YEAR), Calendar.JANUARY, 1);

		this.setEndDate(Calendar.getInstance());
		this.getEndDate().set(this.getEndDate().get(Calendar.YEAR), Calendar.DECEMBER, 31);
	}

	/**
	 * @param startDate
	 */
	public void setStartDate(final Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return Calendar
	 */
	public Calendar getStartDate() {
		return this.startDate;
	}

	/**
	 * @param endDate
	 */
	public void setEndDate(final Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return Calendar
	 */
	public Calendar getEndDate() {
		return this.endDate;
	}

	/* ------------------------meta accessor methods--------------------------- */

	/**
	 * @return the start date of this query. Inclusive. 
	 * */
	@GetProperty(name = "Start Date", index = -2, fieldType = FieldTypes.TEXT_DATE, validationRegex = "\\d{2}\\.\\d{2}\\.\\d{4}", validationMessage = "Datumsformat bla...")
	public String metaGetStartDate() {
		SimpleDateFormat format = new SimpleDateFormat(DateTextFieldComposite.VALID_DATE_FORMAT);
		return format.format(this.getStartDate().getTime());
	}

	/**
	 * Set the start date of this query. Inclusive the given date. Consult the
	 * DateTextFieldComposite class for valid date format.
	 * 
	 * @param startDate 
	 * @throws SetDataException
	 * @see DateTextFieldComposite
	 */
	@SetProperty(name = "Start Date", index = -2)
	public void metaSetStartDate(String startDate) throws SetDataException {
		Calendar cal;
		try {
			SimpleDateFormat format = new SimpleDateFormat(DateTextFieldComposite.VALID_DATE_FORMAT);
			Date date = format.parse(startDate);
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.get(Calendar.DAY_OF_MONTH); // these throw IllegalArgument...
			cal.get(Calendar.MONTH);
			cal.get(Calendar.YEAR);
		} catch (ParseException e) { // converting failure
			throw new SetDataException(NLS.bind(Messages.ERROR_SET_START_DATE, DateTextFieldComposite.VALID_DATE_FORMAT));
		} catch (IllegalArgumentException e) { // illegal date
			throw new SetDataException(Messages.ERROR_START_DATE_VALID);
		}
		this.setStartDate(cal);
	}

	/** 
	 * @return the end date of this query. Inclusive.
	 */
	@GetProperty(name = "End Date", fieldType = FieldTypes.TEXT_DATE, validationRegex = "\\d{2}\\.\\d{2}\\.\\d{4}", validationMessage = "Datumsformat blubb...")
	public String metaGetEndDate() {
		SimpleDateFormat format = new SimpleDateFormat(DateTextFieldComposite.VALID_DATE_FORMAT);
		return format.format(this.getEndDate().getTime());
	}

	/**
	 * Set the end date of this query. Inclusive the given date. Consult the
	 * DateTextFieldComposite class for valid date format.
	 *
	 * @param endDate 
	 * @see DateTextFieldComposite
	 * @throws SetDataException
	 */
	@SetProperty(name = "End Date")
	public void metaSetEndDate(final String endDate) throws SetDataException {
		Calendar cal;
		try {
			SimpleDateFormat format = new SimpleDateFormat(DateTextFieldComposite.VALID_DATE_FORMAT);
			Date date = format.parse(endDate);
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.get(Calendar.DAY_OF_MONTH); // these throw IllegalArgument...
			cal.get(Calendar.MONTH);
			cal.get(Calendar.YEAR);
		} catch (ParseException e) { // converting failure
			throw new SetDataException(NLS.bind(Messages.ERROR_SET_END_DATE, DateTextFieldComposite.VALID_DATE_FORMAT));
		} catch (IllegalArgumentException e) { // illegal date
			throw new SetDataException(Messages.ERROR_END_DATE_VALID);
		}
		if (cal.compareTo(this.getStartDate()) < 0) {
			throw new SetDataException(Messages.ERROR_DATE_DIFFERENCE);
		}
		this.setEndDate(cal);
	}

}
