package ch.unibe.iam.scg.elexis_statistics;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.elexis.Hub;
import ch.elexis.preferences.PreferenceConstants;
import ch.elexis.views.Starter;
import ch.unibe.iam.scg.elexis_statistics.views.SampleDiagramView;

/**
 * 
 * @author psiska
 * 
 */
public class StatisticsPerspective implements IPerspectiveFactory {

	public static final String ID = "ch.unibe.iam.scg.elexis_statistics.StatisticsPerspective";

	/**
	 * 
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.setFixed(false);
		if (Hub.localCfg.get(PreferenceConstants.SHOWSIDEBAR, "true").equals(
				"true")) {
			layout.addStandaloneView(Starter.ID, false, SWT.LEFT, 0.1f,
					editorArea);
		}
		IFolderLayout ifr = layout.createFolder("rechts", SWT.RIGHT, 1.0f,
				editorArea);
		ifr.addView(SampleDiagramView.ID);

	}
}
