/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.ui;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;

import ch.unibe.iam.scg.elexis_statistics.i18n.Messages;
import ch.unibe.iam.scg.elexis_statistics.utils.Decorators;
import ch.unibe.iam.scg.elexis_statistics.utils.RegexValidation;

/**
 * Serves as a base for implementing field composites containing a label and a
 * smartField. The smartField is an inner class, which is able to validate
 * itself, decorate itself, provide content assistance and a quick-fix.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class FieldComposite extends Composite {

	// Margin between field and decoration image in pixels.
	protected final static int DECORATION_MARGIN = 3;

	// Horizontal space between components in this composite.
	protected final static int COLUMN_HSPACING = 15;

	protected final RegexValidation regexValidation;

	protected GridLayout layout;

	// label displayed left of the smartField.
	protected final Label label;

	// actual text field.
	protected final Text text;

	protected ControlDecoration controlDecoration;

	protected SmartField smartField;

	/**
	 * @param parent
	 *            Composite
	 * @param style
	 *            int
	 * @param labelText
	 *            String
	 * @param regex
	 *            Optional <code>RegexValidation</code>, can be
	 *            <code>null</code> if not desired.
	 */
	public FieldComposite(Composite parent, int style, final String labelText, RegexValidation regex) {
		super(parent, style);

		this.regexValidation = regex; // Can be null.

		// Create label
		this.label = new Label(this, SWT.NONE);
		this.label.setText(labelText);

		// Create layout
		this.createLayout();
		this.layout.horizontalSpacing = FieldComposite.COLUMN_HSPACING;

		// Create actual text field.
		this.text = new Text(this, SWT.BORDER);
		
		// Layout data
		GridData layoutData = new GridData(GridData.GRAB_HORIZONTAL);
		layoutData.widthHint = 100;
		this.text.setLayoutData(layoutData);

		// Create SmartTextField.
		this.createSmartField();

		// Add ModifyListener to text field.
		this.text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				FieldComposite.this.handleModify(FieldComposite.this.smartField);
			}
		});

		// Create control decoration.
		this.controlDecoration = new ControlDecoration(this.text, SWT.RIGHT | SWT.CENTER);
		this.controlDecoration.setShowOnlyOnFocus(false);
		this.controlDecoration.setMarginWidth(FieldComposite.DECORATION_MARGIN);
	}

	/**
	 * Create a default 2 columns layout. Subclasses may override this in case
	 * they should need more columns or another layout.
	 */
	protected void createLayout() {
		this.layout = new GridLayout();
		this.layout.numColumns = 2;
		this.layout.marginWidth = 2;
		this.setLayout(this.layout);
	}

	/**
	 * Default implementation, subclasses may override.
	 */
	protected void createSmartField() {
		this.smartField = new SmartField();
	}

	/**
	 * @return String Contents of the <code>AbstractSmartField</code>
	 */
	public String getText() {
		return this.smartField.getContents();
	}

	/**
	 * Sets the text (contents) of the containing smart field.
	 * 
	 * @param text
	 *            Text to be set in the smart field.
	 */
	public void setText(String text) {
		this.smartField.setContents(text);
	}

	/**
	 * @return <code>true</code> if the contents of this objects smartField are
	 *         valid, <code>false</code> else.
	 */
	public boolean isValid() {
		return this.smartField.isValid();
	}

	/**
	 * Enables or disables all controls of this FieldComposite
	 * 
	 * @param enabled
	 *            true
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.text.setEnabled(enabled);
	}

	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		this.controlDecoration.dispose();
		super.dispose();
	}

	/**
	 * Returns the label for this field composite. This is the label being
	 * placed on the left hand side of the other controls in this composite.
	 * 
	 * @return label
	 */
	public Label getLabel() {
		return this.label;
	}

	/**
	 * A SmartField has a control (e.g. a text field) a decoration and optional
	 * a quick-fix menu. It is able to validate itself.
	 */
	protected class SmartField {

		protected IControlContentAdapter contentAdapter;

		protected FieldDecoration fieldDecoration;

		// optional quickFix Menu
		protected Menu quickFixMenu;

		/**
		 * TODO: DOCUMENT ME!
		 */
		public SmartField() {
			this.contentAdapter = new TextContentAdapter();
		}

		/**
		 * Is valid if not empty by default.
		 * 
		 * @return true
		 */
		public boolean isValid() {
			// empty fields are invalid by default - which means that every
			// field is required by default.
			if (this.getContents().isEmpty()) {
				return false;
			}

			// check whether we have a regex validation and if it matches the
			// fields content.
			if (FieldComposite.this.hasRegexValidation()
					&& this.getContents().matches(FieldComposite.this.regexValidation.getPattern())) {
				return false;
			}
			return true;
		}

		/**
		 * Is never in warning state by default.
		 * 
		 * @return false
		 */
		public boolean isWarning() {
			return false;
		}

		/**
		 * No quick-fix by default.
		 * 
		 * @return false
		 */
		public boolean hasQuickFix() {
			return false;
		}

		/**
		 * @return String Retrieves content of field.
		 */
		public String getContents() {
			return this.contentAdapter.getControlContents(FieldComposite.this.text);
		}

		/**
		 * @param contents
		 *            String Content to add to field.
		 */
		public void setContents(final String contents) {
			this.contentAdapter.setControlContents(FieldComposite.this.text, contents, contents.length());
		}

		public FieldDecoration getFieldDecoration() {
			return this.fieldDecoration;
		}

		public void setFieldDecoration(FieldDecoration fieldDecoration) {
			this.fieldDecoration = fieldDecoration;
		}

		/**
		 * @return decorator standard messages.
		 */
		protected String getDecorationMessage(int type) {
			switch (type) {
			case Decorators.ERROR:
				return this.getErrorMessage();
			case Decorators.QUICKFIX:
				return this.getQuickfixMessage();
			case Decorators.WARNING:
				return this.getWarningMessage();
			case Decorators.VALID:
				return this.getValidMessage();
			default:
				return "";
			}
		}

		/**
		 * @return Default decoration error message.
		 */
		protected String getErrorMessage() {
			String error = Messages.FIELD_GENERAL_ERROR;
			if (FieldComposite.this.hasRegexValidation()) {
				error += " " + FieldComposite.this.regexValidation.getMessage();
			}
			return error;
		}

		/**
		 * @return Default decoration quickfix message.
		 */
		protected String getQuickfixMessage() {
			return Messages.FIELD_GENERAL_ERROR_QUICKFIX;
		}

		/**
		 * @return Default decoration warning message.
		 */
		protected String getWarningMessage() {
			return Messages.FIELD_GENERAL_WARNING;
		}

		/**
		 * @return Default decoration valid message.
		 */
		protected String getValidMessage() {
			return Messages.FIELD_GENERAL_VALID;
		}
	}

	/**
	 * Every time the field gets modified this gets run. Checks in what state
	 * the field is an decorates accordingly.
	 * 
	 * @param smartField
	 */
	protected void handleModify(final SmartField smartField) {
		// Hide everything.
		this.hideQuickfix(smartField);
		this.hideError(smartField);
		this.hideValid(smartField);
		this.hideWarning(smartField);
		
		// Show something.
		if (!smartField.isValid()) {
			if (smartField.hasQuickFix()) {
				this.showQuickfix(smartField);
			} else {
				this.showError(smartField);
			}
		} else {
			if (smartField.isWarning()) {
				this.showWarning(smartField);
			} else {
				this.showValid(smartField);
			}
		}
	}

	/**
	 * Checks whether we have a regexValidation or not.
	 */
	protected boolean hasRegexValidation() {
		if (this.regexValidation == null) {
			return false;
		}
		return true;
	}

	/**
	 * @param smartField
	 */
	protected void showError(final SmartField smartField) {
		this.showDecoration(smartField, Decorators.ERROR, true);
	}

	/**
	 * @param smartField
	 */
	protected void hideError(final SmartField smartField) {
		this.showDecoration(smartField, Decorators.ERROR, false);
	}

	/**
	 * @param smartField
	 */
	protected void showQuickfix(final SmartField smartField) {
		this.showDecoration(smartField, Decorators.QUICKFIX, true);
	}

	/**
	 * @param smartField
	 */
	protected void hideQuickfix(final SmartField smartField) {
		this.showDecoration(smartField, Decorators.QUICKFIX, false);
	}

	/**
	 * @param smartField
	 */
	protected void showWarning(final SmartField smartField) {
		this.showDecoration(smartField, Decorators.WARNING, true);
	}

	/**
	 * @param smartField
	 */
	protected void hideWarning(final SmartField smartField) {
		this.showDecoration(smartField, Decorators.WARNING, false);
	}

	/**
	 * @param smartField
	 */
	protected void showValid(final SmartField smartField) {
		this.showDecoration(smartField, Decorators.VALID, true);
	}

	/**
	 * @param smartField
	 */
	protected void hideValid(final SmartField smartField) {
		this.showDecoration(smartField, Decorators.VALID, false);
	}

	private void showDecoration(final SmartField smartField, int type, final boolean show) {
		smartField.setFieldDecoration(Decorators.getFieldDecoration(type, smartField.getDecorationMessage(type)));
		if (show) {
			this.controlDecoration.setImage(smartField.getFieldDecoration().getImage());
			this.controlDecoration.setDescriptionText(smartField.getFieldDecoration().getDescription());
			this.controlDecoration.show();
		} else {
			this.controlDecoration.hide();
		}
	}
}
