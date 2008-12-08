/**
 * 
 */
package ch.unibe.iam.scg.archie.actions;

import org.eclipse.jface.action.Action;

import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.ui.views.Dashboard;

/**
 * <p>TODO: DOCUMENT ME!</p>
 * 
 * $Id:$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev: 295 $
 */
public class RefreshChartsAction extends Action {
	
	private Dashboard dashboard;
	
	public RefreshChartsAction(Dashboard dashboard) {
		this.dashboard = dashboard;
		
		this.setToolTipText("Refresh Charts");
		this.setImageDescriptor(ArchieActivator.getImageDescriptor("icons/arrow_circle_double.png"));
		
		this.setEnabled(false);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void run() {
		this.dashboard.redrawCharts();
	}
}