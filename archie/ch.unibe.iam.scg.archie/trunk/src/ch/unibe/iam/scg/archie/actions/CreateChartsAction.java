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
public class CreateChartsAction extends Action {
	
	private Dashboard dashboard;
	
	public CreateChartsAction(Dashboard dashboard) {
		this.dashboard = dashboard;
		
		this.setToolTipText("Create Charts");
		this.setImageDescriptor(ArchieActivator.getImageDescriptor("icons/control.png"));
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void run() {
		this.dashboard.createCharts();
	}
}