package ch.unibe.iam.scg.elexis_statistics.model;

/**
 * This class is used to represent any input errors in the QueryInputDialog.
 * This exception is thrown by the methods annotated with SetProperty tags in
 * the query classes. Therefore the whole input handling is done by these
 * classes and not the input dialog since the view cannot know which values are
 * valid and which are not. The value of the exception message will be displayed
 * to the user.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class SetDataException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * This exception is thrown by a query if any of the annotated methods can't
	 * handle the input string. The message is printed to the output.
	 * 
	 * @param message
	 *            error message to be displayed to the user in the UI.
	 */
	public SetDataException(String message) {
		super(message);
	}
}
