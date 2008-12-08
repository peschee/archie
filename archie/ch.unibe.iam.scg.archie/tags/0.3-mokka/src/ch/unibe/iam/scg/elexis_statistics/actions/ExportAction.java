package ch.unibe.iam.scg.elexis_statistics.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import ch.elexis.util.SWTHelper;
import ch.unibe.iam.scg.elexis_statistics.Activator;
import ch.unibe.iam.scg.elexis_statistics.export.CSVWriter;
import ch.unibe.iam.scg.elexis_statistics.i18n.Messages;
import ch.unibe.iam.scg.elexis_statistics.ui.views.StatisticsView;
import ch.unibe.iam.scg.elexis_statistics.utils.StringHelper;

/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ExportAction extends Action {

	private StatisticsView view;

	/**
	 * Default extensions for cvs exports.
	 */
	private static String DEFAULT_EXTENSION = "csv";

	/**
	 * Determines whether the files are allowed to have more than one _ spacers.
	 */
	private static boolean SINGLE_SPACED_FILES = false;

	/** constructor */
	public ExportAction() {
		super();

		this.setText(Messages.EXPORT_TITLE);
		this.setToolTipText(Messages.EXPORT_DESCRIPTION);
		this.setImageDescriptor(Activator.getImageDescriptor("icons/page_excel.png"));

		// disabled by default
		this.setEnabled(false);
	}

	/**
	 * 
	 * @param view
	 */
	public ExportAction(StatisticsView view) {
		this();
		this.view = view;
	}

	@Override
	public void run() {
		// gset a file chooser
		FileDialog chooser = new FileDialog(this.view.getSite().getShell(), SWT.SAVE);

		// set default extension for the exported file
		chooser.setFilterExtensions(new String[] { "*." + ExportAction.DEFAULT_EXTENSION });

		// get a default name based on the current date
		String name = this.getNameSuggestion().toLowerCase();
		chooser.setFileName(name);

		String fileName = chooser.open();
		if (fileName != null) {
			this.saveFile(fileName);
		}
	}

	/**
	 * Saves a CSV list to the given filename.
	 * 
	 * @param fileName
	 *            Filename to save the CSV export to.
	 */
	private void saveFile(String fileName) {
		try {
			CSVWriter.writeFile(this.view.getDataProvider(), fileName);
		} catch (IOException e) {
			// TODO LOG
			System.err.println(e.getLocalizedMessage());
			SWTHelper.showError(Messages.ERROR_WRITING_FILE_TITLE, Messages.ERROR_WRITING_FILE);
		}
	}

	/**
	 * Suggests a filename to the user based on the cleaned up name of the data
	 * provider we're looking at and the today's date being appened to that
	 * name.
	 * 
	 * @return Cleaned up filename suggestion.
	 * @see StringHelper
	 */
	private String getNameSuggestion() {
		String name = StringHelper.removeIllegalCharacters(this.view.getDataProvider().getName(),
				ExportAction.SINGLE_SPACED_FILES);

		// append today's date
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		name += "_" + format.format(Calendar.getInstance().getTime());
		return name;
	}
}
