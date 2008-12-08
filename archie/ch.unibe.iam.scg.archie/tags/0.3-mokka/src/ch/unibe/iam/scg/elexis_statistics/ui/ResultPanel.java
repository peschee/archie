package ch.unibe.iam.scg.elexis_statistics.ui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import ch.elexis.Desk;
import ch.unibe.iam.scg.elexis_statistics.Activator;
import ch.unibe.iam.scg.elexis_statistics.i18n.Messages;

/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ResultPanel extends Composite {

	/** This label will be displayed as long as the results are not calculated. */
	private GraphicalMessage message;

	/**
	 * @param parent
	 * @param style
	 */
	public ResultPanel(final Composite parent, final int style) {
		super(parent, style);

		// although we're using a TableColumnLayout later in the TableFactory
		// we still need this to be able to show the loading message during
		// computations
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		
		// prepare layout data for this result panel
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;

		// set layout
		this.setLayoutData(gridData);
		this.setLayout(layout);
		this.setBackground(Desk.theColorRegistry.get(Desk.COL_WHITE));

		this.message = new GraphicalMessage(this, Activator.getDefault().getImageRegistry().get(Activator.IMG_COFFEE),
				Messages.WORKING);
	}

	/**
	 * Removes the loading message
	 */
	public void removeLoadingMessage() {
		this.message.dispose();
	}
}
