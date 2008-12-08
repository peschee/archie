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
package ch.unibe.iam.scg.archie.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

import ch.unibe.iam.scg.archie.model.ChartModel;
import ch.unibe.iam.scg.archie.model.ProviderManager;

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

public class ChartWizard extends Wizard {

	private ChartModel chartModel;

	/**
	 * Pages in this wizard
	 */
	private ChartWizardMainPage mainPage;
	private PieChartPage pieChartPage;
	private BarChartPage barChartPage;
	private ContentSelectionPage selectionPage;

	/**
	 * Constructs a ChartWizard
	 */
	public ChartWizard() {
		super();

		assert ProviderManager.getInstance().hasProvider();

		// create a new chart model and add a CLONED dataset
		this.chartModel = new ChartModel();
		this.chartModel.setDataSet(ProviderManager.getInstance().getProvider().getDataSet().clone());

		assert this.chartModel.hasDataSet();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return this.getContainer().getCurrentPage() instanceof ContentSelectionPage && this.chartModel.isValid();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		// main page
		this.mainPage = new ChartWizardMainPage();
		this.addPage(this.mainPage);

		// consecutive pages
		this.pieChartPage = new PieChartPage();
		this.addPage(this.pieChartPage);

		this.barChartPage = new BarChartPage();
		this.addPage(this.barChartPage);

		// final page
		this.selectionPage = new ContentSelectionPage();
		this.addPage(this.selectionPage);
	}

	/**
	 * @return ChartModel
	 */
	public ChartModel getModel() {
		return this.chartModel;
	}
}