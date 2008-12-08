/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.ui;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import ch.elexis.Desk;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractDataProvider;
import ch.unibe.iam.scg.elexis_statistics.utils.ProviderHelper;

/**
 * Displays information about a provider that is currently selected. The class
 * generates a label list based on the set of names and values of provider
 * parameters.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ProviderInformatioPanel extends Composite {

	private AbstractDataProvider provider;

	private Map<String, String> parameterList;
	
	private Group parameterGroup;
	
	private Composite parameterContainer;
	
	/**
	 * Default constructor.
	 * 
	 * @param parent Parent composite.
	 * @param style SWT style for this composite.
	 */
	public ProviderInformatioPanel(final Composite parent) {
		super(parent, SWT.NONE);

		GridLayout parentlayout = new GridLayout();
		parentlayout.marginBottom = 5;
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.grabExcessHorizontalSpace = true;
		
		this.setLayout(parentlayout);
		this.setLayoutData(layoutData);
		
		GridLayout groupLayout = new GridLayout();	
		this.parameterGroup = new Group(this, SWT.NONE);
		this.parameterGroup.setText("Current Statistics");
		this.parameterGroup.setLayout(groupLayout);
		this.parameterGroup.setLayoutData(layoutData);
		
		GridLayout containerLayout = new GridLayout();
		containerLayout.numColumns = 2;
		containerLayout.marginWidth = 2;
		
		this.parameterContainer = new Composite(this.parameterGroup, SWT.FLAT);
		this.parameterContainer.setLayout(containerLayout);
		this.parameterContainer.setLayoutData(layoutData);
	}

	/**
	 * Updates provider information based on the title and parameter list provided.
	 * 
	 * @param providerName Name of the provider.
	 * @param parameterList Set of names and values of the provider parameters.
	 */
	public void updateProviderInformation(final AbstractDataProvider provider) {
		this.provider = provider;
		this.parameterList = ProviderHelper.getGetterMap(provider, true);

		// dispose all children
		for (Control child : this.parameterContainer.getChildren()) {
			child.dispose();
		}

		this.createLabels();
		this.parameterContainer.layout();
	}

	/**
	 * Create labels based on the provider name and its parameters.
	 */
	private void createLabels() {
		// set title label
		Label titleLabel = new Label(this.parameterContainer, SWT.WRAP);
		titleLabel.setText(this.provider.getName());
		titleLabel.setToolTipText(this.provider.getDescription());

		// spans two columns
		GridData layoutData = new GridData();
		layoutData.horizontalSpan = 2;
		layoutData.grabExcessHorizontalSpace = true;
		titleLabel.setLayoutData(layoutData);
		titleLabel.setFont(Desk.theFontRegistry.getBold("Arial"));
		
		// generate labels for parameter names and values
		for (String name : this.parameterList.keySet()) {
			Label nameLabel = new Label(this.parameterContainer, SWT.NONE);
			nameLabel.setText(name + ":");

			Label valueLabel = new Label(this.parameterContainer, SWT.NONE);
			valueLabel.setText(this.parameterList.get(name));
		}
	}
}
