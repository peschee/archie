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
package ch.unibe.iam.scg.archie.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import ch.unibe.iam.scg.archie.model.RegexValidation;

/**
 * <p>
 * Combo box field composite. TODO: Test me, use me.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ComboWidget extends AbstractWidget {

	/**
	 * @param parent
	 * @param style
	 * @param labelText
	 */
	public ComboWidget(Composite parent, int style, final String labelText, RegexValidation regex) {
		super(parent, style, labelText, regex);

		// Create Label
		this.label = new Label(this, SWT.NONE);
		this.label.setText(labelText);

		// Create Combo
		this.control = new Combo(this, SWT.READ_ONLY);

		// Layout Data
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		this.layout.horizontalSpacing = AbstractWidget.STD_COLUMN_HORIZONTAL_SPACING;
		this.control.setLayoutData(layoutData);
	}

	/**
	 * Sets the combox items (array of strings).
	 * @param items
	 *            String[] items
	 */
	public void setItems(String[] items) {
		((Combo) this.control).setItems(items);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue() {
		return ((Combo) this.control).getText();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return ((Combo) this.control).getSelectionIndex() > -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(final Object value) {
		if(value instanceof String) {
			((Combo) this.control).setText(value.toString());	
		} else {
			throw new IllegalArgumentException("Must be a string.");	
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescription(final String description) {
		this.label.setToolTipText(description);
		this.control.setToolTipText(description);
	}
}
