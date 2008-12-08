package ch.unibe.iam.scg.elexis_statistics.views;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.unibe.iam.scg.elexis_statistics.IStatSource;

public class SampleDiagramView extends ViewPart {
	
	public static final String ID = "ch.unibe.iam.scg.elexis_statistics.views.SampleDiagramView";
	
	public SampleDiagramView() {
		super();
	}
	
	// Sample Dataset
	private static JFreeChart createDataset() {
		
		//iterate through extensions
		StringBuffer buffer = new StringBuffer();
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg.getConfigurationElementsFor("ch.unibe.iam.scg.elexis_statictics.datasource");
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement element = extensions[i];
			try {
				IStatSource statSource = (IStatSource) element.createExecutableExtension("class");
				buffer.append(statSource.getName() + "\n");
				return SampleDiagramView.createChart(statSource.getDataSet(), statSource.getName());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		System.out.println("Installed extensions" + buffer.toString());
		
		return null;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset the data set.
	 * @return A chart.
	 */
	private static JFreeChart createChart(Dataset dataset, String name) {

		JFreeChart chart = ChartFactory.createBarChart(
				"Geschlecht Patienten", // chart title
				"Category", // domain axis label
				"Value", // range axis label
				(DefaultCategoryDataset)dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);
		
		//set the name, just to check where the dataset is coming from
		chart.setTitle(name);


		return chart;
	}

	public void createPartControl(Composite parent) {
		JFreeChart chart = SampleDiagramView.createDataset();
		final ChartComposite frame = new ChartComposite(parent, SWT.NONE,
				chart, true);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}

}