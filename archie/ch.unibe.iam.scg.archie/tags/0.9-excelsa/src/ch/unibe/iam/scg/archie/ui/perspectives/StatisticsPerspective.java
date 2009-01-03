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
package ch.unibe.iam.scg.archie.ui.perspectives;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.progress.IProgressConstants;

import ch.elexis.Hub;
import ch.elexis.preferences.PreferenceConstants;
import ch.elexis.views.Starter;
import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.ui.views.ChartView;
import ch.unibe.iam.scg.archie.ui.views.Dashboard;
import ch.unibe.iam.scg.archie.ui.views.StatisticsSidebarView;
import ch.unibe.iam.scg.archie.ui.views.StatisticsView;

/**
 * <p>The main Archie perspective. Here we layout the views that compose the GUI for archie.</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class StatisticsPerspective implements IPerspectiveFactory {

	/**
	 * ID of this perspective.
	 */
	public static final String ID = ArchieActivator.PLUGIN_ID + ".ui.perspectives.StatisticsPerspective";

	/**
	 * Creates the initial perspective layout.
	 * 
	 * @param layout
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		layout.setEditorAreaVisible(false);
		layout.setFixed(false);

		// Add elexis sidebar
		if (Hub.localCfg.get(PreferenceConstants.SHOWSIDEBAR, "true").equals("true")) { //$NON-NLS-1$
			layout.addStandaloneView(Starter.ID, false, SWT.LEFT, 0.1f, editorArea);
		}

		IFolderLayout main = layout.createFolder("main", IPageLayout.LEFT, 1.0f, editorArea);
		IFolderLayout sidebar = layout.createFolder("right", IPageLayout.RIGHT, 0.6f, "main");
		
		// Main area
		main.addView(Dashboard.ID);
		main.addView(StatisticsView.ID);
		main.addPlaceholder(ChartView.ID);
		main.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);

		// Sidebar
		sidebar.addView(StatisticsSidebarView.ID);
		
		layout.getViewLayout(StatisticsView.ID).setCloseable(false);
		layout.getViewLayout(StatisticsSidebarView.ID).setCloseable(false);
	}
}
