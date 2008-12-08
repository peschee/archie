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
package ch.unibe.iam.scg.archie.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import ch.elexis.data.Person;
import ch.elexis.data.RnStatus;
import ch.unibe.iam.scg.archie.ui.views.Dashboard;
import ch.unibe.iam.scg.archie.utils.DatabaseHelper;

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
public class DashboardOverview extends Composite {

	private Dashboard dashboard;
	private Button refreshButton;

	/**
	 * @param dashboard
	 * @param parent
	 * @param style
	 */
	public DashboardOverview(final Dashboard dashboard, final Composite parent, int style) {
		super(parent, style);
		this.dashboard = dashboard;
		
		this.setLayout(new GridLayout());

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		Group overview = new Group(this, SWT.NONE);
		overview.setText("Statistics");
		overview.setLayout(layout);
		overview.setLayoutData(layoutData);

		this.createDescriptionPanel(overview);
		this.createStatsPanel(overview);
	}
	
	/**
	 * 
	 * @param parent
	 * @return
	 */
	private Composite createDescriptionPanel(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		container.setLayout(layout);
		container.setLayoutData(layoutData);
		
		Label introduction = new Label(container, SWT.NONE | SWT.WRAP);
		introduction.setText("Welcome to Archie, the statistics analysis tool for Elexis.");
		introduction.setLayoutData(layoutData);
		
		this.refreshButton = new Button(container, SWT.NONE);
		this.refreshButton.setText("Refresh Charts");
		this.refreshButton.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
				// Nothing here.
			}

			public void mouseDown(MouseEvent e) {
				DashboardOverview.this.dashboard.redrawCharts();
				
			}

			public void mouseUp(MouseEvent e) {
				// Nothing here.
			}
		});
		
		return container;
	}
	
	/**
	 * 
	 * @param parent
	 * @return
	 */
	private Composite createStatsPanel(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		container.setLayout(layout);
		container.setLayoutData(layoutData);
		
		// Stats data
		int patientsTotal = DatabaseHelper.getNumberOfPatients();
		int patientsMale = DatabaseHelper.getNumberGenderPatients(Person.MALE);
		int patientsFemale = DatabaseHelper.getNumberGenderPatients(Person.FEMALE);

		int invoicesTotal = DatabaseHelper.getTotalNumberOfInvoices();
		int invoicesPaid = DatabaseHelper.getNumberOfInvoices(RnStatus.BEZAHLT);
		int invoicesOpen = DatabaseHelper.getNumberOfInvoices(RnStatus.OFFEN)
				+ DatabaseHelper.getNumberOfInvoices(RnStatus.OFFEN_UND_GEDRUCKT);

		int consultationsTotal = DatabaseHelper.getNumberOfConsultations();
		
		Label patients = new Label(container, SWT.NONE | SWT.WRAP);
		patients.setText("Patients: " + patientsTotal + "\n" 
			+ "Male: " + writePercent(patientsMale, patientsTotal)  + "\n" 
			+ "Female: " + writePercent(patientsFemale, patientsTotal) + "\n"
			+ "Other: " + writePercent(patientsTotal - patientsFemale - patientsMale, patientsTotal));
		

		Label invoices = new Label(container, SWT.NONE | SWT.WRAP);
		invoices.setText("Invoices: " + invoicesTotal + "\n"
			+ "Paid: " + writePercent(invoicesPaid, invoicesTotal) + "\n"
			+ "Open: " + writePercent(invoicesOpen, invoicesTotal) + "\n"
			+ "Other: " + writePercent(invoicesTotal - invoicesOpen - invoicesPaid, invoicesTotal));

		Label consultations = new Label(container, SWT.NONE | SWT.WRAP);
		consultations.setText("Consultations: " + consultationsTotal + "\n");
		consultations.setLayoutData(layoutData);
		
		return container;
	}
	
	/** (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		this.refreshButton.setEnabled(enabled);
	}

	/**
	 * @param givenAmount
	 * @param totalAmount
	 * @return float How much percent is givenAmount of totalAmount
	 */
	private float calculatePercent(final float givenAmount, final float totalAmount) {
		return Math.round((givenAmount / totalAmount) * 100);
	}
	
	/**
	 * @param givenAmount
	 * @param totalAmount
	 * @return String 
	 */
	private String writePercent(final float givenAmount, final float totalAmount) {
		return calculatePercent(givenAmount, totalAmount) + " %";
	}
}
