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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * <p>A simple FieldComposite containing a checkbox button.</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class CheckboxFieldComposite extends AbstractFieldComposite {
	
	/**
	 * @param parent Composite
	 * @param style Integer
	 * @param labelText String
	 */
	public CheckboxFieldComposite(Composite parent, int style, final String labelText) {
		super(parent, style, labelText);

		// Create Label
		this.label = new Label(this, SWT.NONE);
		this.label.setText(labelText);
		
		// Create Checkbox
		this.control = new Button(this, SWT.CHECK);
		
		// Layout Data
		GridData layoutData = new GridData(GridData.GRAB_HORIZONTAL);
		this.layout.horizontalSpacing = AbstractFieldComposite.STD_COLUMN_HORIZONTAL_SPACING;
		this.control.setLayoutData(layoutData);
	}
	
	/**
	 * @return true if checkbox is selected, false else.
	 */
	public boolean getSelection() {
		return ((Button) this.control).getSelection();
	}

	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite#getValue()
	 */
	@Override
	public Object getValue() {
		return this.getSelection();
	}

	/**
	 * Checkbox is always valid.
	 */
	@Override
	public boolean isValid() {
		return true;
	}


	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(final Object value) {
		if(value instanceof Boolean) {
			((Button) this.control).setSelection((Boolean) value);	
		} else {
			throw new IllegalArgumentException("Must be a boolean.");	
		}
	}

	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.label.setToolTipText(description);
		this.control.setToolTipText(description);
	}
}
