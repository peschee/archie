/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.model;

import ch.elexis.actions.BackgroundJob;
import ch.elexis.actions.BackgroundJob.BackgroundJobListener;


/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public abstract class ChartFactory implements BackgroundJobListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.elexis.actions.BackgroundJob.BackgroundJobListener#jobFinished(ch.
	 * elexis.actions.BackgroundJob)
	 */
	@Override
	public void jobFinished(BackgroundJob job) {
		// TODO Auto-generated method stub
		// create chart output using jfreechart
		// will probably need adapter pattern for different datasets
	}

}
