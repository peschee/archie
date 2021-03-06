/*******************************************************************************
 * Copyright (c) 2008 Dennis Schenk, Peter Siska.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dennis Schenk - initial implementation
 *     Peter Siska	 - initial implementation
 *******************************************************************************/
package ch.unibe.iam.scg.archie.actions;

import org.eclipse.jface.action.Action;

import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.ui.views.Dashboard;

/**
 * <p>TODO: DOCUMENT ME!</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class CreateChartsAction extends Action {
	
	private Dashboard dashboard;
	
	/**
	 * @param dashboard
	 */
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