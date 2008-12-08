package ch.unibe.iam.scg.elexis_statistics.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.WorkbenchException;

import ch.unibe.iam.scg.elexis_statistics.ui.perspectives.StatisticsPerspective;
import ch.unibe.iam.scg.elexis_statistics.ui.views.StatisticsView;

/**
 * This action opens the statistic view.
 * 
 * @see IWorkbenchWindowActionDelegate
 * @see StatisticsView
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class OpenStatisticsPerspective implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public OpenStatisticsPerspective() {
		// empty constructor
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		try {
			this.window.getWorkbench().showPerspective(StatisticsPerspective.ID, this.window);
		} catch (WorkbenchException e) {
			MessageDialog.openInformation(this.window.getShell(), "Elexis Statistics Plug-in",
					"Error while opening the statistics perspective.");
		}
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
		// do nothing
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}