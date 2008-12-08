package ch.unibe.iam.scg.elexis_statistics.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ch.unibe.iam.scg.elexis_statistics.model.AbstractDataProvider;
import ch.unibe.iam.scg.elexis_statistics.model.DataSet;
import ch.unibe.iam.scg.elexis_statistics.ui.DateFieldComposite;
import ch.unibe.iam.scg.elexis_statistics.utils.ProviderHelper;

/**
 * A simple CSV writer that takes care of exporting data from a given provider
 * into a CSV file.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class CSVWriter {

	/** Default delimiter for the CSV data */
	private static String DELIMITER = ";";

	/**
	 * Writes contents of a data provider into the given file in a CSV-format.
	 * 
	 * @param provider
	 *            A data provider.
	 * @param fileName
	 *            A filename to write to.
	 * @return File File containing a data provider's content.
	 * @throws IOException
	 */
	public static File writeFile(final AbstractDataProvider provider, final String fileName) throws IOException {
		File file = new File(fileName);
		FileWriter writer = new FileWriter(file);

		// retrieve DataSet
		DataSet data = provider.getDataSet();

		// write header - provider information
		CSVWriter.writeProviderInformation(writer, provider);

		// write headings
		CSVWriter.writeColumnHeadings(writer, data.getHeadings());

		// write rows from data
		for (Object[] objects : data) {
			CSVWriter.writeRow(writer, objects);
		}

		writer.close();
		return file;
	}

	/**
	 * Writes the heading for each column into the given file.
	 * 
	 * @param writer
	 *            A FileWriter object.
	 * @param headings
	 *            List of column headings to write.
	 * @throws IOException
	 */
	private static void writeColumnHeadings(FileWriter writer, List<String> headings) throws IOException {
		// write column headings
		Object[] objects = headings.toArray();
		CSVWriter.writeRow(writer, objects);
	}

	/**
	 * Writes the provider parameter information based on the given provider.
	 * This also adds the current date to the file being written, so that we
	 * know when the statistics were exported.
	 * 
	 * @param writer
	 *            A FileWriter object.
	 * @param provider
	 *            A data provider.
	 * @throws IOException
	 */
	private static void writeProviderInformation(FileWriter writer, final AbstractDataProvider provider)
			throws IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFieldComposite.VALID_DATE_FORMAT);

		// provider title
		writer.write(provider.getName());
		writer.write("\n");
		writer.write(dateFormat.format(Calendar.getInstance().getTime()));
		writer.write("\n");
		writer.write("\n");

		// write parameters
		Map<String, String> getters = ProviderHelper.getGetterMap(provider, true);
		for (Object name : getters.keySet().toArray()) {
			writer.write(name + " = " + getters.get(name));
			writer.write("\n");
		}
		writer.write("\n");
		writer.write("\n");
	}

	/**
	 * Writes a row of given data to the output file.
	 * 
	 * @param writer
	 *            A FileWriter object.
	 * @param objects
	 *            An array of objects containing the data to write.
	 * @throws IOException
	 */
	private static void writeRow(FileWriter writer, Object[] objects) throws IOException {
		StringBuffer buf = new StringBuffer();
		for (Object obj : objects) {
			buf.append(obj.toString());
			buf.append(CSVWriter.DELIMITER);
		}
		buf.deleteCharAt(buf.length() - 1); // last delimiter not needed
		buf.append("\n");
		writer.write(buf.toString());
	}
}
