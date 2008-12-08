package ch.unibe.iam.scg.elexis_statistics.actions;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;

import ch.elexis.actions.BackgroundJob;
import ch.elexis.actions.BackgroundJob.BackgroundJobListener;
import ch.elexis.util.SWTHelper;
import ch.unibe.iam.scg.elexis_statistics.Activator;
import ch.unibe.iam.scg.elexis_statistics.i18n.Messages;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractDataProvider;
import ch.unibe.iam.scg.elexis_statistics.model.TableFactory;
import ch.unibe.iam.scg.elexis_statistics.ui.ParametersPanel;
import ch.unibe.iam.scg.elexis_statistics.ui.ProviderInformatioPanel;
import ch.unibe.iam.scg.elexis_statistics.ui.ResultPanel;
import ch.unibe.iam.scg.elexis_statistics.ui.views.StatisticsView;

/**
 * <p>
 * This action is responsible for the whole procedure of creating a new query:
 * getting all information needed of the user, starting the query in the
 * background and updating the view in the end.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class NewStatisticsAction extends Action implements BackgroundJobListener {

	private StatisticsView view;
	private AbstractDataProvider provider;
	private ParametersPanel parameters;
	private ProviderInformatioPanel providerInformation;
	private ArrayList<IPropertyChangeListener> listeners;

	/** constant for a running job */
	public static final String JOB_RUNNING = "JOB_RUNNING";

	/** constant for a finished job */
	public static final String JOB_DONE = "JOB_DONE";

	/**
	 * Action for creating a new statistical analysis. This class serves as a
	 * controller and mediator between the main and sidebar view. It also acts
	 * as a job listener and listens to the job this actions data provider runs.
	 * 
	 * @param view
	 *            Main statistics view.
	 * @param parameters
	 *            Panel containing a provider's parameters.
	 */
	public NewStatisticsAction(StatisticsView view, ParametersPanel parameters) {
		super("Start Query", AS_PUSH_BUTTON);

		this.setToolTipText("Starts a new Query");

		this.setImageDescriptor(Activator.getImageDescriptor("icons/database_go.png"));

		// disabled by default
		this.setEnabled(false);

		this.view = view;
		this.parameters = parameters;
		this.listeners = new ArrayList<IPropertyChangeListener>();
	}

	@Override
	/*
	 * This actions main method, called when the action is run.
	 */
	public void run() {
		// user set a provider and all fields are valid
		if (this.provider != null && this.parameters.allFieldsValid()) {

			// set parameters in provider
			try {
				this.parameters.updateProvider();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// clean view
			this.view.clean();

			// add provider information
			// TODO: Refactor this, currently the provider information is being
			// handled by this class
			if (this.providerInformation == null) {
				this.providerInformation = new ProviderInformatioPanel(this.view.getParent());
			}
			this.providerInformation.updateProviderInformation(this.provider);

			// disable this action
			this.setEnabled(false);

			// register this action as the job listener for its provider
			this.provider.addListener(this);

			// set result composite and layout
			ResultPanel resultComposite = new ResultPanel(this.view.getParent(), SWT.FLAT);
			this.view.setResultComposite(resultComposite);
			this.view.getParent().layout();
			this.view.setActionsEnabled(false);

			// delegate property change event
			for (IPropertyChangeListener listener : this.listeners) {
				listener.propertyChange(new PropertyChangeEvent(this, NewStatisticsAction.JOB_RUNNING, null, null));
			}

			// run the job
			this.provider.schedule();
		} else {
			// TODO: Log? Exception?
			SWTHelper.showError(Messages.ERROR_FIELDS_NOT_VALID_TITLE, Messages.ERROR_FIELDS_NOT_VALID);
		}
	}

	/**
	 * This action is enabled as soon as the last job finishes. This method also
	 * creates and sets the result table in the result view as well as
	 * information about the parameters of the active provider in the header of
	 * the result panel.
	 * 
	 * @param job
	 *            BackgroundJob
	 */
	public void jobFinished(BackgroundJob job) {
		ResultPanel results = this.view.getResultPanel();

		// create result table
		TableFactory tableFactory = TableFactory.getInstance();
		tableFactory.createTableFromData(results, this.provider);

		results.removeLoadingMessage();
		results.layout();

		this.view.setActionsEnabled(true);
		this.setEnabled(true);

		// delegate property change event
		for (IPropertyChangeListener listener : this.listeners) {
			listener.propertyChange(new PropertyChangeEvent(this, NewStatisticsAction.JOB_DONE, null, null));
		}
	}

	/**
	 * Sets the data provider for this action.
	 * 
	 * @param provider
	 *            An data provider object.
	 */
	public void setDataProvider(final AbstractDataProvider provider) {
		this.provider = provider;
		this.view.setDataProvider(this.provider);
		this.setEnabled(true);
	}

	/**
	 * Registers a change listener with this action.
	 * 
	 * @see org.eclipse.jface.AbstractAction.addPropertyChangeListener
	 * @see org.eclipse.jface.util.PropertyChangeListener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		super.addPropertyChangeListener(listener);
		if (!this.listeners.contains(listener))
			this.listeners.add(listener);
	}

	/**
	 * De-registers a change listener with this action.
	 * 
	 * @see org.eclipse.jface.AbstractAction.removePropertyChangeListener
	 * @see org.eclipse.jface.util.PropertyChangeListener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		super.removePropertyChangeListener(listener);
		this.listeners.remove(listener);
	}

}
