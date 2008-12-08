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
 * TODO: Add package description.
 */
package ch.unibe.iam.scg.archie.ui.charts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import ch.elexis.Hub;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Query;

/**
 * <p>TODO: DOCUMENT ME!</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ConsultationMoneyDatasetCreator extends AbstractDatasetCreator {
	
	/**
	 * @param jobName
	 */
	public ConsultationMoneyDatasetCreator(String jobName) {
		super(jobName);
	}

	private static final String DATE_DB_FORMAT = "yyyyMMdd";
	private static final String DATE_CONS_FORMAT = "dd.MM.yyyy";

	/** (non-Javadoc)
	 * @see ch.elexis.actions.BackgroundJob#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus createContent(final IProgressMonitor monitor) {
		// date formats
		final SimpleDateFormat databaseFormat = new SimpleDateFormat(DATE_DB_FORMAT);
		final SimpleDateFormat consultationFormat = new SimpleDateFormat(DATE_CONS_FORMAT);
		
		final Query<Konsultation> query = new Query<Konsultation>(Konsultation.class);
		
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, -1);
		Calendar before = Calendar.getInstance();
		before.add(Calendar.YEAR, -1);
		before.add(Calendar.MONTH, -6);
		
		query.add("Datum", ">=", databaseFormat.format(before.getTime()));
		query.add("Datum", "<=", databaseFormat.format(now.getTime()));
		query.add("MandantID", "=", Hub.actMandant.getId());
		
		monitor.subTask("querying database");
		final List<Konsultation> consults = query.execute();
		
		// size * 2, going over consultations twice
		monitor.beginTask("doing calculations", query.size() * 2);
		
		// Consultations map, grouping consultations by month
		final TreeMap<Month, ArrayList<Konsultation>> consultsMap = new TreeMap<Month, ArrayList<Konsultation>>();
	
		// Group consults
		for(Konsultation consult : consults) {
			// check for cancelation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			try {
				Date date = consultationFormat.parse(consult.getDatum());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				
				// create month
				Month month = new Month(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
				
				if(consultsMap.containsKey(month)) {
					consultsMap.get(month).add(consult);
				} else {
					ArrayList<Konsultation> consultList = new ArrayList<Konsultation>(1);
					consultList.add(consult);
					consultsMap.put(month, consultList);
				}
				
				monitor.worked(1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		// create dataset
		TimeSeries profitSeries = new TimeSeries("Profit", Month.class);
		TimeSeries spendingSeries = new TimeSeries("Spending", Month.class);
		TimeSeries incomeSeries = new TimeSeries("Income", Month.class);
		
		// Compute money values for grouped consultations
		for(Entry<Month, ArrayList<Konsultation>> entry : consultsMap.entrySet()) {
			// check for cancellation
			if(monitor.isCanceled()) return Status.CANCEL_STATUS;
			
			Month month = entry.getKey();
			
			double profit = 0;
			double spending = 0; 
			double income = 0;
						
			for(Konsultation consult : entry.getValue()) {
				profit += consult.getGewinn();
				spending += consult.getKosten();
				income += consult.getUmsatz();
				
				monitor.worked(1);
			}
			
			profitSeries.add(month, profit / 100);
			spendingSeries.add(month, spending / 100);
			incomeSeries.add(month, income / 100);
		}
		
        this.dataset = new TimeSeriesCollection();
        
        ((TimeSeriesCollection) this.dataset).addSeries(profitSeries);
        ((TimeSeriesCollection) this.dataset).addSeries(spendingSeries);
        ((TimeSeriesCollection) this.dataset).addSeries(incomeSeries);
        
        monitor.done();
        return Status.OK_STATUS;
	}
}