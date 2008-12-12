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

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.controller.ChartModelManager;
import ch.unibe.iam.scg.archie.model.ChartModel;
import ch.unibe.iam.scg.archie.model.DataSet;
import ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite;
import ch.unibe.iam.scg.archie.ui.fields.TextFieldComposite;
import ch.unibe.iam.scg.archie.utils.DatasetHelper;

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
public class PieChartPage extends AbstractChartPage implements Listener {

	/**
	 * Name of this page.
	 */
	protected static final String PAGE_NAME = "PieChartPage";

	/**
	 * Title of this page. This is being set when creating this page.
	 */
	private static final String PAGE_TITLE = "Choose Pie Chart Parameters";

	/**
	 * Description of this page. This is being set when creating this page.
	 */
	private static final String PAGE_DESCRIPTION = "PieChartPage Description";

	private TextFieldComposite chartName;

	private Combo keysColumn;
	private Combo valuesColumn;

	private Button threeDimensional;

	/**
	 * Constructs ChartWizardMainPage
	 */
	public PieChartPage() {
		super(PieChartPage.PAGE_NAME, PieChartPage.PAGE_TITLE, ArchieActivator
				.getImageDescriptor("icons/chart_pie_big.png"));
		super.setDescription(PieChartPage.PAGE_DESCRIPTION);
	}

	// ///////////////////////////////////////////////////////////////////////////
	// INTERFACE FUNCTIONS
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 *      Event)
	 */
	public void handleEvent(Event event) {
		// Initialize a variable with the no error status
		Status status = new Status(IStatus.OK, ArchieActivator.PLUGIN_NAME, 0, "", null);

		// If the event is triggered by the destination or departure fields
		// set the corresponding status variable to the right value
		int valuesIndex = this.valuesColumn.getSelectionIndex();
		if (valuesIndex > -1) {
			String valueSelected = this.keysColumn.getItem(valuesIndex);
			DataSet dataset = ((ChartWizard) this.getWizard()).getModel().getDataSet();

			if (!DatasetHelper.isNumericColumn(dataset, valueSelected)
					&& !DatasetHelper.isMoneyColumn(dataset, valueSelected)) {
				status = new Status(IStatus.ERROR, ArchieActivator.PLUGIN_NAME, 0,
						"The values in a graph have to be numeric.", null);
			}
		}

		// apply status
		this.applyToStatusLine(status);
		this.getWizard().getContainer().updateButtons();
	}

	// TODO: i18n
	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 *      .Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);

		this.chartName = new TextFieldComposite(container, SWT.NONE, "Name of the Chart", null);
		GridData nameLayoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		nameLayoutData.horizontalSpan = 2;
		this.chartName.setLayoutData(nameLayoutData);

		List<String> columns = ((ChartWizard) this.getWizard()).getModel().getDataSet().getHeadings();

		Label keysLabel = new Label(container, SWT.NONE);
		keysLabel.setText("Keys");

		this.keysColumn = new Combo(container, SWT.READ_ONLY);
		this.keysColumn.setItems(columns.toArray(new String[0]));

		Label valuesLabel = new Label(container, SWT.NONE);
		valuesLabel.setText("Values");

		this.valuesColumn = new Combo(container, SWT.READ_ONLY);
		this.valuesColumn.setItems(columns.toArray(new String[0]));

		Label threeDimensionalLabel = new Label(container, SWT.NONE);
		threeDimensionalLabel.setText("3D Graph");

		this.threeDimensional = new Button(container, SWT.CHECK);
		this.threeDimensional.setToolTipText("Check if you want the graph to be three dimensional.");

		// Add listener to our controls
		this.chartName.addListener(SWT.KeyUp, this);
		this.keysColumn.addListener(SWT.Selection, this);
		this.valuesColumn.addListener(SWT.Selection, this);
		this.threeDimensional.addListener(SWT.Selection, this);

		// Adjust label sizes
		this.chartName.pack();
		int width = this.chartName.getLabel().getBounds().width;
		GridData widthData = new GridData();
		widthData.widthHint = width + AbstractFieldComposite.STD_COLUMN_HORIZONTAL_SPACING;
		keysLabel.setLayoutData(widthData);
		valuesLabel.setLayoutData(widthData);
		threeDimensionalLabel.setLayoutData(widthData);

		// set data from already used model if available
		this.initializePreviousModelData();

		this.setControl(container);
	}

	// ///////////////////////////////////////////////////////////////////////////
	// PRIVATE HELPER FUNCTIONS
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Saves the gathered data to the model.
	 */
	private void saveDataToModel() {
		// Gets the model
		final ChartWizard wizard = (ChartWizard) this.getWizard();
		final ChartModel chartModel = wizard.getModel();

		chartModel.setChartName(this.chartName.getValue().toString());
		chartModel.setKeysIndex(this.keysColumn.getSelectionIndex());
		chartModel.setValuesIndex(this.valuesColumn.getSelectionIndex());
		chartModel.setThreeDimensional(this.threeDimensional.getSelection());
	}

	/**
	 * 
	 */
	private void initializePreviousModelData() {
		if (ChartModelManager.getInstance().hasPieChartModel()) {
			ChartModel model = ChartModelManager.getInstance().getPieChartModel();

			this.chartName.setValue(model.getChartName());
			this.keysColumn.select(model.getKeysIndex());
			this.valuesColumn.select(model.getValuesIndex());
			this.threeDimensional.setSelection(model.isThreeDimensional());
		}
	}

	// ///////////////////////////////////////////////////////////////////////////
	// OVERRIDE FUNCTIONS
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IWizardPage getPreviousPage() {
		return this.getWizard().getPage(ChartWizardMainPage.PAGE_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IWizardPage getNextPage() {
		this.saveDataToModel();
		return this.getWizard().getPage(ContentSelectionPage.PAGE_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canFlipToNextPage() {
		return this.getErrorMessage() == null && this.keysColumn.getSelectionIndex() != -1
				&& this.valuesColumn.getSelectionIndex() != -1 && !this.chartName.getValue().toString().equals("");
	}
}