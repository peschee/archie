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
package ch.unibe.iam.scg.archie.ui.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

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
public class ComboFieldComposite extends AbstractFieldComposite {

	/**
	 * @param parent
	 * @param style
	 * @param labelText
	 */
	public ComboFieldComposite(Composite parent, int style, final String labelText) {
		super(parent, style, labelText);

		// Create Label
		this.label = new Label(this, SWT.NONE);
		this.label.setText(labelText);

		// Create Combo
		this.control = new Combo(this, SWT.READ_ONLY);

		// Layout Data
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		this.layout.horizontalSpacing = AbstractFieldComposite.STD_COLUMN_HORIZONTAL_SPACING;
		this.control.setLayoutData(layoutData);
	}

	/**
	 * @param items
	 *            String[] items
	 */
	public void setItem(String[] items) {
		((Combo) this.control).setItems(items);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite#getValue()
	 */
	@Override
	public Object getValue() {
		return ((Combo) this.control).getText();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite#isValid()
	 */
	@Override
	public boolean isValid() {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(final Object value) {
		((Combo) this.control).setText(value.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(final String description) {
		this.label.setToolTipText(description);
		this.control.setToolTipText(description);

	}

}
