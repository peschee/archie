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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.unibe.iam.scg.archie.model.ChartModel;
import ch.unibe.iam.scg.archie.model.DataSet;
import ch.unibe.iam.scg.archie.model.DatasetTableColumnSorter;
import ch.unibe.iam.scg.archie.model.ProviderManager;
import ch.unibe.iam.scg.archie.model.TableFactory;
import ch.unibe.iam.scg.archie.model.TableManager;

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
public class ContentSelectionPage extends WizardPage implements SelectionListener {

	/**
	 * Table created from the dataset from the currently selected chart model.
	 */
	private Table table;

	/**
	 * Name of this page.
	 */
	protected static final String PAGE_NAME = "ContentSelectionPage";

	/**
	 * Title of this page. This is being set when creating this page.
	 */
	private static final String PAGE_TITLE = "Choose Pie Chart Parameters";

	/**
	 * Description of this page. This is being set when creating this page.
	 */
	private static final String PAGE_DESCRIPTION = "ChartWizardMainPage Description";

	/**
	 * Constructs ChartWizardMainPage
	 */
	public ContentSelectionPage() {
		super(ContentSelectionPage.PAGE_NAME, ContentSelectionPage.PAGE_TITLE, null);
		super.setDescription(ContentSelectionPage.PAGE_DESCRIPTION);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout());
		
		final DataSet dataset = ((ChartWizard) this.getWizard()).getModel().getDataSet();

		// create table from the currently model dataset and use current data provider for label and content providers.
		this.table = TableFactory.getInstance().createTableFromData(composite, dataset, ProviderManager.getInstance().getProvider().getLabelProvider(), ProviderManager.getInstance().getProvider().getContentProvider());
		
		// add selection listener
		this.table.addSelectionListener(this);
		for (TableColumn column : this.table.getColumns()) {
			column.addSelectionListener(this);
		}
		
		// add dataset - table column sorter
		new DatasetTableColumnSorter(this.table, dataset);

		// check if the table manager has sort direction and column
		TableManager manager = TableManager.getInstance();
		if (manager.hasTable()) {
			this.table.setSortColumn(manager.getSortColumn());
			this.table.setSortDirection(manager.getSortDirection());
		}

		this.setControl(composite);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
		// nothing to do here...
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		// get selection indices
		int[] indices = this.table.getSelectionIndices();

		// update model
		ChartModel model = ((ChartWizard) this.getWizard()).getModel();
		model.setRows(indices);

		// update page buttons
		this.getWizard().getContainer().updateButtons();
	}
}