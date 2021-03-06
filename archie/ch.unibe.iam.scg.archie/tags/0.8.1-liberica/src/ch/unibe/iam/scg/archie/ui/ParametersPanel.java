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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import ch.unibe.iam.scg.archie.annotations.GetProperty;
import ch.unibe.iam.scg.archie.annotations.SetProperty;
import ch.unibe.iam.scg.archie.model.AbstractDataProvider;
import ch.unibe.iam.scg.archie.ui.fields.AbstractFieldComposite;
import ch.unibe.iam.scg.archie.ui.fields.CheckboxFieldComposite;
import ch.unibe.iam.scg.archie.ui.fields.DateTextFieldComposite;
import ch.unibe.iam.scg.archie.ui.fields.NumericTextFieldComposite;
import ch.unibe.iam.scg.archie.ui.fields.TextFieldComposite;
import ch.unibe.iam.scg.archie.utils.ProviderHelper;
import ch.unibe.iam.scg.archie.utils.RegexValidation;

/**
 * <p>A composite panel which contains all the parameter fields for a data
 * provider. The parameters of a provider are set accordingly. Parameter fields
 * and their content are determined at runtime through annotations.</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ParametersPanel extends Composite {

	/**
	 * Map containing all text fields an their name. Used to feed the query with
	 * the user input.
	 */
	private Map<String, AbstractFieldComposite> fieldMap;

	/**
	 * Map containing the getter method names and their default values. We need
	 * this because of the control decorations that have to be set after the
	 * text fields have been created.
	 */
	private Map<String, Object> defaultValuesMap;

	/**
	 * The query which is selected at the moment and will be configured
	 * according to the user input in this panel.
	 */
	private AbstractDataProvider provider;

	/**
	 * @param parent
	 * @param style
	 */
	public ParametersPanel(final Composite parent, final int style) {
		super(parent, style);

		// define layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;

		this.setLayout(layout);
	}

	/**
	 * @param provider
	 */
	public void updateParameterList(AbstractDataProvider provider) {
		this.provider = provider; // the query which was selected.

		// clear this composite
		for (Control child : this.getChildren()) {
			child.dispose();
		}

		// initialize an empty field map
		this.fieldMap = new TreeMap<String, AbstractFieldComposite>();
		this.defaultValuesMap = new HashMap<String, Object>();

		// populate again
		this.createParameterFields();

		// re-show everything
		this.layout();

		// adjust label widths
		// TODO: Probably not so efficient, maybe refactor the abstract
		// composites to only hold the text input fields and put all labels and
		// abstract fields into one layout?
		this.adjustLabelWidths();

		this.setDefaultValues();
	}

	/**
	 * Adjusts the width of the labels left to the text fields in abstract field
	 * composites.
	 */
	private void adjustLabelWidths() {
		// calculate max label width
		int maxWidth = 0;
		for (AbstractFieldComposite field : this.fieldMap.values()) {
			Label label = field.getLabel();
			int width = label.getBounds().width;
			maxWidth = (width > maxWidth) ? width : maxWidth;
		}

		// set all labels to that max width
		for (AbstractFieldComposite field : this.fieldMap.values()) {
			Label label = field.getLabel();
			GridData data = new GridData();
			data.widthHint = maxWidth;
			label.setLayoutData(data);
		}
		this.layout();
	}

	/**
	 * Sets the default values of the text fields.
	 */
	private void setDefaultValues() {
		for (Entry<String, Object> name : this.defaultValuesMap.entrySet()) {
			this.fieldMap.get(name.getKey()).setValue(name.getValue());
		}
	}

	/**
	 * Create Parameter Fields.
	 */
	private void createParameterFields() {
		// get all getters
		for (Method method : ProviderHelper.getGetterMethods(this.provider, true)) {
			GetProperty getter = method.getAnnotation(GetProperty.class);

			RegexValidation regex = null; // can be null
			if (!getter.validationRegex().equals("") && !getter.validationMessage().equals("")) {
				regex = new RegexValidation(getter.validationRegex(), getter.validationMessage());
			}

			// create the appropriate text field
			AbstractFieldComposite fieldComposite = this.createTextField(this, ProviderHelper.getValue(method, this.provider),
					getter.name(), getter.fieldType(), regex );
			
			if(!getter.description().equals("")) {
				fieldComposite.setDescription(getter.description());
			}

			// put field and label title in the map
			this.fieldMap.put(getter.name(), fieldComposite);
			this.defaultValuesMap.put(getter.name(), ProviderHelper.getValue(method, this.provider));
		}
	}

	/**
	 * Updates the provider parameters according to the user input in the fields
	 * in this panel.
	 * 
	 * @throws Exception
	 */
	public void updateProviderParameters() throws Exception {
		this.setProviderData();
	}

	/**
	 * Checks the validity of all input fields in this composite.
	 * 
	 * @return true if all fields are valid, false else.
	 */
	public boolean allFieldsValid() {
		for (Map.Entry<String, AbstractFieldComposite> entry : this.fieldMap.entrySet()) {
			if (!entry.getValue().isValid()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Enables or disables all controls in the <code>fieldMap</code> belonging
	 * to this <code>ParameterPanel</code>
	 * 
	 * @param enabled
	 *            true to enable, false to disable
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (this.fieldMap != null) {
			for (Map.Entry<String, AbstractFieldComposite> entry : this.fieldMap.entrySet()) {
				entry.getValue().setEnabled(enabled);
			}
		}
	}

	private AbstractFieldComposite createTextField(final Composite parent, Object value, final String label, 
			FieldTypes fieldType, final RegexValidation regex) {
		switch (fieldType) {
		case TEXT_DATE:
			return new DateTextFieldComposite(parent, SWT.NONE, label, regex);
		case TEXT_NUMERIC:
			return new NumericTextFieldComposite(parent, SWT.NONE, label, regex);
		case BUTTON_CHECKBOX:
			return new CheckboxFieldComposite(parent, SWT.NONE, label);
		case TEXT:
		default: // Standard text field
			return new TextFieldComposite(parent, SWT.NONE, label, regex);
		}
	}

	/** Sets all fields via the meta model in the given query. */
	private void setProviderData() throws Exception {
		assert (this.provider != null);
		this.setData(ProviderHelper.getSetterMethods(this.provider, true));
	}

	/**
	 * @param setterList
	 * @throws Exception
	 */
	private void setData(ArrayList<Method> setterList) throws Exception {
		for (Method method : setterList) {
			SetProperty setter = method.getAnnotation(SetProperty.class);
			AbstractFieldComposite field = this.fieldMap.get(setter.name());
			Object value = field.getValue();
			ProviderHelper.setValue(this.provider, method, value);
		}
	}
}