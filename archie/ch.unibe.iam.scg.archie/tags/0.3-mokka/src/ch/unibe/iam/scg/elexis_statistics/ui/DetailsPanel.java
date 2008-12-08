package ch.unibe.iam.scg.elexis_statistics.ui;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import ch.unibe.iam.scg.elexis_statistics.actions.NewStatisticsAction;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractDataProvider;
import ch.unibe.iam.scg.elexis_statistics.ui.views.StatisticsView;

/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class DetailsPanel extends Composite {

	private AbstractDataProvider provider;

	private Text description;

	private ParametersPanel parameters;

	private ActionContributionItem startButton;

	private NewStatisticsAction action;

	/**
	 * 
	 * @param parent
	 * @param style
	 */
	public DetailsPanel(Composite parent, int style) {
		super(parent, style);

		// set layout
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 5;

		this.setLayout(layout);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Add the statistics description
		this.description = new Text(this, SWT.MULTI | SWT.WRAP);
		this.description.setEditable(false);
		this.description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.description.setBackground(parent.getBackground());

		// add parameters
		this.parameters = new ParametersPanel(this, SWT.NONE);
		this.parameters.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// find our main view
		@SuppressWarnings("unused")
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		StatisticsView view = (StatisticsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(StatisticsView.ID);

		// add the new query action and a button for it
		this.action = new NewStatisticsAction(view, this.parameters);

		this.startButton = new ActionContributionItem(this.action);
		this.startButton.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		this.startButton.fill(this);

		// Set initial state
		this.reset();
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param provider
	 */
	public DetailsPanel(Composite parent, int style, AbstractDataProvider provider) {
		this(parent, style);
		this.setDataProvider(provider);
	}

	/**
	 * TODO: Document me hard!
	 * 
	 * @param provider
	 */
	public void setDataProvider(AbstractDataProvider provider) {
		// set data providers, both for the action and for this class
		this.provider = provider;

		this.action.setDataProvider(this.provider);

		this.description.setText(this.provider.getDescription());
		this.description.pack(true);

		this.update();
	}

	@Override
	public void update() {
		if (this.provider != null) {
			this.parameters.updateParameterList(this.provider);
			this.layout();
		} else {
			this.reset();
		}
	}

	/**
	 * Resets the details panel and it's components to a start state, same as if
	 * no statistic were selected yet.
	 */
	public void reset() {
		this.description.setText("Please select a statistic plugin.");
		this.action.setEnabled(false);

		// dispose parameter
		for (Control child : this.parameters.getChildren()) {
			child.dispose();
		}

		this.layout();
	}

	/**
	 * Sets all children enabled according to the boolean passed to this
	 * function.
	 * 
	 * @param enabled
	 *            True if children should be enabled, false else.
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.description.setEnabled(enabled);
		this.parameters.setEnabled(enabled);
	}

	/**
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		this.action.addPropertyChangeListener(listener);
	}
}