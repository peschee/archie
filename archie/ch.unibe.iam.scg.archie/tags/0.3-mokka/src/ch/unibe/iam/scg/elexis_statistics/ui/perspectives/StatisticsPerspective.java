package ch.unibe.iam.scg.elexis_statistics.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.elexis.Hub;
import ch.elexis.preferences.PreferenceConstants;
import ch.elexis.views.Starter;
import ch.unibe.iam.scg.elexis_statistics.ui.views.StatisticsSidebarView;
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
public class StatisticsPerspective implements IPerspectiveFactory {

	public static final String ID = "ch.unibe.iam.scg.elexis_statistics.StatisticsPerspective";

	/**
	 * Creates the initial perspective layout.
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		layout.setEditorAreaVisible(false);
		layout.setFixed(false);

		// add elexis sidebar
		if (Hub.localCfg.get(PreferenceConstants.SHOWSIDEBAR, "true").equals("true")) {
			layout.addStandaloneView(Starter.ID, false, IPageLayout.LEFT, 0.1f, editorArea);
		}

		IFolderLayout main = layout.createFolder("main", IPageLayout.LEFT, 1.0f, editorArea);
		IFolderLayout sidebar = layout.createFolder("right", IPageLayout.RIGHT, 0.6f, "main");

		main.addView(StatisticsView.ID);
		sidebar.addView(StatisticsSidebarView.ID);
	}
}
