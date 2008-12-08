/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import ch.unibe.iam.scg.elexis_statistics.Activator;
import ch.unibe.iam.scg.elexis_statistics.i18n.Messages;
import ch.unibe.iam.scg.elexis_statistics.utils.RegexValidation;

/**
 * Implements <code>AbstractFieldComposite</code> with a SmartDateField.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class DateFieldComposite extends FieldComposite {

	private DateTime datePicker;

	private Shell datePickerShell;

	public final static String VALID_DATE_FORMAT = "dd.MM.yyyy";

	/**
	 * @param parent
	 * @param style
	 * @param labelText
	 */
	public DateFieldComposite(Composite parent, int style, String labelText, RegexValidation regex) {
		super(parent, style, labelText, regex);

		// Add datePicker Popup Button (as Label)
		Label datePickerPopupButton = new Label(this, SWT.FLAT);
		Image image = Activator.getDefault().getImageRegistry().get(Activator.IMG_BUTTON_CALENDAR);
		datePickerPopupButton.setImage(image);
		datePickerPopupButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDown(MouseEvent event) {
				DateFieldComposite.this.popUpCalendar();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Nothing here. Move along.
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// Nothing here. Move along.
			}
		});

		// Init Datepicker Popup Shell
		this.datePickerShell = new Shell(this.getDisplay(), SWT.APPLICATION_MODAL);

		GridLayout shellLayout = new GridLayout();
		shellLayout.numColumns = 1;
		this.datePickerShell.setLayout(shellLayout);

		// Add Datepicker
		this.datePicker = new DateTime(this.datePickerShell, SWT.CALENDAR);

		// Pack Datepicker Popup Shell
		this.datePickerShell.pack();

		// Allow to close datePicker shell with ESC or ENTER
		this.datePickerShell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_RETURN:
					DateFieldComposite.this.popDownCalendar();
					event.detail = SWT.TRAVERSE_NONE;
					event.doit = false;
					break;
				}
			}
		});

		this.datePicker.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				DateFieldComposite.this.popDownCalendar();
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO: This could probably be done in a more efficient manner...
				SimpleDateFormat sdf = new SimpleDateFormat(DateFieldComposite.VALID_DATE_FORMAT);
				GregorianCalendar cal = new GregorianCalendar(DateFieldComposite.this.datePicker.getYear(),
						DateFieldComposite.this.datePicker.getMonth(), DateFieldComposite.this.datePicker.getDay());
				Date date = new Date(cal.getTimeInMillis());
				DateFieldComposite.this.smartField.setContents(sdf.format(date));
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// Nothing here. Move along.
			}
		});
	}
	


	/**
	 * Custom layout creation as the date picker has three columns.
	 */
	protected void createLayout() {
		// Create layout.
		this.layout = new GridLayout();
		this.layout.numColumns = 3;
		this.layout.marginWidth = 2;
		this.setLayout(this.layout);
	}
	
	/**
	 * Create a <code>SmartNumericField</code>
	 */
	protected void createSmartField() {
		this.smartField = new SmartDateField();		
	}

	protected void popUpCalendar() {
		// TODO: make sure the shell never gets displayed outside of the screen.
		Point pt = this.getDisplay().getCursorLocation();
		this.datePickerShell.setLocation(pt.x - this.datePicker.getSize().x, pt.y);
		this.datePickerShell.setVisible(true);
		this.datePickerShell.setFocus();

	}

	protected void popDownCalendar() {
		this.datePickerShell.setVisible(false);
	}

	/**
	 * TODO: DOCUMENT ME!
	 * 
	 * @author dschenk
	 * 
	 */
	private class SmartDateField extends SmartField {

		public SmartDateField() {
			super();
		}

		protected String getErrorMessage() {
			String format = DateFieldComposite.VALID_DATE_FORMAT;
			String error = NLS.bind(Messages.ERROR_DATE_FORMAT, format.toUpperCase());
			if(DateFieldComposite.this.hasRegexValidation()) {
				error += " " + DateFieldComposite.this.regexValidation.getMessage();
			}
			return error;
		}

		@Override
		public boolean isValid() {
			// An empty field is never valid.
			if (this.getContents().isEmpty()) {
				return false;
			}

			SimpleDateFormat sdf = new SimpleDateFormat(DateFieldComposite.VALID_DATE_FORMAT);
			Date testDate = null;

			// If the format of the string provided doesn't match the format we
			// declared in SimpleDateFormat() we will get an exception
			try {
				testDate = sdf.parse(this.getContents());
			} catch (ParseException e) {
				return false;
			}

			// Dateformat.parse will accept any date as long as it's in the
			// format we defined, it simply rolls dates over, for example,
			// december 32 becomes jan 1 and december 0 becomes november 30.
			// This statement will make sure that once the string has been
			// checked for proper formatting the date is still the date
			// that was entered, if it's not, we assume that the date is invalid
			if (!sdf.format(testDate).equals(this.getContents())) {
				return false;
			}
			
			// Check for possible regex validation for dates
			if (DateFieldComposite.this.hasRegexValidation() && !this.getContents().matches(DateFieldComposite.this.regexValidation.getPattern())) {
				return false;
			}
			
			return true;
		}
	}
}
