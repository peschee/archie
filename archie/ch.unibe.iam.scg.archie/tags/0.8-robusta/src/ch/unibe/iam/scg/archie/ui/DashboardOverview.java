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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import ch.elexis.data.Person;
import ch.elexis.data.RnStatus;
import ch.unibe.iam.scg.archie.utils.DatabaseHelper;

/**
 * <p>
 * Dashboard overview is the dashboard description panel on the top side of the
 * entire dashboard. Contains some basic welcome message and some general data
 * about the system (simple gender overview of users in the system and more).<br>
 * <br>
 * The overview panel also contains two buttons, one to start the creation of
 * the charts, the other to recreate them once available.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class DashboardOverview extends Composite {

	/**
	 * Public constructor.
	 * 
	 * @param dashboard
	 *            The dashboard object containing the dashboard charts or their
	 *            placeholders.
	 * @param parent
	 *            Parent composite.
	 * @param style
	 *            SWT control style.
	 */
	public DashboardOverview(final Composite parent, final int style) {
		super(parent, style);
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
	 * Creates the description panel for this dashboard overview. This is the
	 * left hand side of the overview, containing the buttons that control the
	 * chart generation.
	 * 
	 * @param parent
	 *            Parent composite.
	 * @return Composite containing the created controls.
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

		return container;
	}

	/**
	 * Creates the statistics panel in this dashboard overview. This is the
	 * right hand side of the overview, containing some statistical data about
	 * the system.
	 * 
	 * @param parent
	 *            Parent composite.
	 * @return Composite containing the created controls.
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
		patients.setText("Patients: " + patientsTotal + "\n" + "Male: " + writePercent(patientsMale, patientsTotal)
				+ "\n" + "Female: " + writePercent(patientsFemale, patientsTotal) + "\n" + "Unknown: "
				+ writePercent(patientsTotal - patientsFemale - patientsMale, patientsTotal));

		Label invoices = new Label(container, SWT.NONE | SWT.WRAP);
		invoices.setText("Invoices: " + invoicesTotal + "\n" + "Paid: " + writePercent(invoicesPaid, invoicesTotal)
				+ "\n" + "Open: " + writePercent(invoicesOpen, invoicesTotal) + "\n" + "Other: "
				+ writePercent(invoicesTotal - invoicesOpen - invoicesPaid, invoicesTotal));

		Label consultations = new Label(container, SWT.NONE | SWT.WRAP);
		consultations.setText("Consultations: " + consultationsTotal + "\n");
		consultations.setLayoutData(layoutData);

		return container;
	}

	/**
	 * Calculates the percent value from two given amounts.
	 * 
	 * @param givenAmount
	 *            Amount given.
	 * @param totalAmount
	 *            Total amount.
	 * @return float How much percent is givenAmount of totalAmount
	 */
	private float calculatePercent(final float givenAmount, final float totalAmount) {
		return Math.round((givenAmount / totalAmount) * 100);
	}

	/**
	 * Writes the percent value from two given amounts.
	 * 
	 * @param givenAmount
	 *            Amount given.
	 * @param totalAmount
	 *            Total amount.
	 * @return String How much percent is givenAmount of totalAmount, written as
	 *         string containing the % sign.
	 */
	private String writePercent(final float givenAmount, final float totalAmount) {
		return calculatePercent(givenAmount, totalAmount) + " %";
	}
}
