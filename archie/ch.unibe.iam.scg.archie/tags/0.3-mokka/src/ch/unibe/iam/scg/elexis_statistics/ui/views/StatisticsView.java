package ch.unibe.iam.scg.elexis_statistics.ui.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import ch.elexis.Desk;
import ch.unibe.iam.scg.elexis_statistics.Activator;
import ch.unibe.iam.scg.elexis_statistics.actions.ExportAction;
import ch.unibe.iam.scg.elexis_statistics.i18n.Messages;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractDataProvider;
import ch.unibe.iam.scg.elexis_statistics.ui.GraphicalMessage;
import ch.unibe.iam.scg.elexis_statistics.ui.ResultPanel;

/**
 * This class contains all methods needed to display the output created by any
 * query.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class StatisticsView extends ViewPart {

	public static final String ID = "ch.unibe.iam.scg.elexis_statistics.views.StatisticsView";

	private Composite parent;

	private ResultPanel resultPanel;

	private GraphicalMessage message;

	private ExportAction exportAction;

	private AbstractDataProvider provider;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		// remove margins and vertical spacing
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		
		// set layout and background color
		this.parent.setLayout(layout);
		this.parent.setBackground(Desk.theColorRegistry.get(Desk.COL_WHITE));

		// set initial message
		this.message = new GraphicalMessage(this.parent, Activator.getDefault().getImageRegistry().get(
				Activator.IMG_IMPORTANT), Messages.NO_PLUGIN_SELECTED);

		// add and contribute actions for this view
		this.addActions();
	}

	/**
	 * Add actions to this view.
	 */
	private void addActions() {
		this.exportAction = new ExportAction(this);
		IToolBarManager manager = this.getViewSite().getActionBars().getToolBarManager();
		manager.add(this.exportAction);
	}

	/**
	 * Removes the initial message which is being shown before any statistics
	 * have been run.
	 */
	public void removeInitializeMessage() {
		this.message.dispose();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		// viewer.getControl().setFocus();
	}

	/**
	 * Returns the parent of this view. This composite is used to populate the
	 * view with other components.
	 * 
	 * @return The composite parent of this view.
	 */
	public Composite getParent() {
		return this.parent;
	}

	/**
	 * Cleans the main result view from all active components.
	 */
	public void clean() {
		if (this.message != null) {
			this.message.dispose();
		}
		if (this.resultPanel != null) {
			this.resultPanel.dispose();
		}
	}

	/**
	 * Sets the result panel for this view.
	 * 
	 * @param composite
	 *            Composite containing the results of a query.
	 */
	public void setResultComposite(ResultPanel composite) {
		this.resultPanel = composite;
	}

	/**
	 * 
	 * @return
	 */
	public ResultPanel getResultPanel() {
		return this.resultPanel;
	}

	/**
	 * Sets the enabled state for actions in this view.
	 * 
	 * @param enabled True if actions should be enabled, false for disabled.
	 * @see String org.eclipse.jface.action.IAction.ENABLED
	 */
	public void setActionsEnabled(boolean enabled) {
		this.exportAction.setEnabled(enabled);
	}

	/**
	 * Sets the data provider that delivers the data for main result view.
	 * 
	 * @param provider
	 *            Data provider delivering results to this main result view.
	 */
	public void setDataProvider(AbstractDataProvider provider) {
		assert (provider != null);
		this.provider = provider;
	}

	/**
	 * Returns the data provider from this view.
	 * 
	 * @return
	 */
	public AbstractDataProvider getDataProvider() {
		return this.provider;
	}
}