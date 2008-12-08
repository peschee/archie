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
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import ch.unibe.iam.scg.archie.i18n.Messages;
import ch.unibe.iam.scg.archie.utils.RegexValidation;

/**
 * Implements <code>FieldComposite</code> with a <code>SmartNumericField</code>.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class NumericTextFieldComposite extends TextFieldComposite {

	/**
	 * @param parent Composite
	 * @param style Integer
	 * @param labelText String
	 * @param regex String
	 */
	public NumericTextFieldComposite(Composite parent, int style, final String labelText, RegexValidation regex) {
		super(parent, style, labelText, regex);

		// Create quickFix menu listener
		this.controlDecoration.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent event) {
				// no quick fix if we aren't in error state.
				if (NumericTextFieldComposite.this.smartField.isValid()) {
					return;
				}
				if (NumericTextFieldComposite.this.smartField.quickFixMenu == null) {
					NumericTextFieldComposite.this.smartField.quickFixMenu = NumericTextFieldComposite.this
							.createQuickFixMenu((SmartNumericField) NumericTextFieldComposite.this.smartField);
				}
				NumericTextFieldComposite.this.smartField.quickFixMenu.setLocation(event.x, event.y);
				NumericTextFieldComposite.this.smartField.quickFixMenu.setVisible(true);
			}
		});
	}

	/**
	 * Create a <code>SmartNumericField</code>
	 */
	@Override
	protected void createSmartField() {
		this.smartField = new SmartNumericField();
	}

	protected Menu createQuickFixMenu(final SmartNumericField field) {
		Menu newMenu = new Menu(this.control);
		MenuItem item = new MenuItem(newMenu, SWT.PUSH);
		item.setText(Messages.FIELD_NUMERIC_QUICKFIX);
		item.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				field.quickFix();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// do nothing
			}
		});
		return newMenu;
	}

	/**
	 * Is only valid if content are digits and nothing else. Provides a
	 * quick-fix to remove all characters that are not digits.
	 */
	private class SmartNumericField extends SmartField {

		public SmartNumericField() {
			super();
		}

		@Override
		public boolean isValid() {
			// perform basic validation for a numeric only field
			String contents = this.getContents();
			for (int i = 0; i < contents.length(); i++) {
				if (!Character.isDigit(contents.charAt(i))) {
					return false;
				}
			}

			// perform regex validation if available
			if (NumericTextFieldComposite.this.hasRegexValidation()
					&& !this.getContents().matches(NumericTextFieldComposite.this.regexValidation.getPattern())) {
				return false;
			}
			return true;

		}

		@Override
		public boolean hasQuickFix() {
			return true;
		}

		@Override
		protected String getQuickfixMessage() {
			return this.getErrorMessage();
		}

		@Override
		protected String getErrorMessage() {
			String error = Messages.FIELD_NUMERIC_ERROR;
			if (NumericTextFieldComposite.this.hasRegexValidation()) {
				error += "\n" + NumericTextFieldComposite.this.regexValidation.getMessage();
			}
			return error;
		}

		/**
		 * Removes all characters except digits.
		 */
		protected void quickFix() {
			String contents = this.getContents();
			StringBuffer digitsOnly = new StringBuffer();
			int length = contents.length();
			for (int i = 0; i < length;) {
				char ch = contents.charAt(i++);
				if (Character.isDigit(ch)) {
					digitsOnly.append(ch);
				}
			}
			this.setContents(digitsOnly.toString());
		}
	}
}
