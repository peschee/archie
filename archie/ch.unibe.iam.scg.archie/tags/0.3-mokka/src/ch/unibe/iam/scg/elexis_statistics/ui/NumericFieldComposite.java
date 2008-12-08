/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import ch.unibe.iam.scg.elexis_statistics.i18n.Messages;
import ch.unibe.iam.scg.elexis_statistics.utils.RegexValidation;

/**
 * Implements <code>FieldComposite</code> with a <code>SmartNumericField</code>.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class NumericFieldComposite extends FieldComposite {

	/**
	 * @param parent
	 * @param style
	 * @param labelText
	 */
	public NumericFieldComposite(Composite parent, int style, String labelText, RegexValidation regex) {
		super(parent, style, labelText, regex);

		// Create quickFix menu listener
		this.controlDecoration.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent event) {
				// no quick fix if we aren't in error state.
				if (NumericFieldComposite.this.smartField.isValid()) {
					return;
				}
				if (NumericFieldComposite.this.smartField.quickFixMenu == null) {
					NumericFieldComposite.this.smartField.quickFixMenu = NumericFieldComposite.this
							.createQuickFixMenu((SmartNumericField) NumericFieldComposite.this.smartField);
				}
				NumericFieldComposite.this.smartField.quickFixMenu.setLocation(event.x, event.y);
				NumericFieldComposite.this.smartField.quickFixMenu.setVisible(true);
			}
		});
	}

	/**
	 * Create a <code>SmartNumericField</code>
	 */
	protected void createSmartField() {
		this.smartField = new SmartNumericField();
	}

	protected Menu createQuickFixMenu(final SmartNumericField field) {
		Menu newMenu = new Menu(this.text);
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
			if (NumericFieldComposite.this.hasRegexValidation()
					&& !this.getContents().matches(NumericFieldComposite.this.regexValidation.getPattern())) {
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
			if (NumericFieldComposite.this.hasRegexValidation()) {
				error += "\n" + NumericFieldComposite.this.regexValidation.getMessage();
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
