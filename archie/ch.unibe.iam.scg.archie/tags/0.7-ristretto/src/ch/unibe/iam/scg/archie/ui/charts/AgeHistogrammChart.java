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
package ch.unibe.iam.scg.archie.ui.charts;

import java.awt.Color;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultKeyedValues2DDataset;

import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.preferences.PreferenceConstants;

/**
 * <p>TODO: DOCUMENT ME!</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class AgeHistogrammChart extends AbstractChartComposite {
	
	private static final String CHART_TITLE = "Age Histogram";
	
	/**
	 * @param parent
	 * @param style 
	 */
	public AgeHistogrammChart(Composite parent, int style) {
		super(parent, style);
		
	}

	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.ui.charts.AbstractChartComposite#initializeChart()
	 */
	@Override
	protected JFreeChart initializeChart() {
		this.chart = ChartFactory.createStackedBarChart(
				AgeHistogrammChart.CHART_TITLE, 
				"Age", // domain axis label
				"Patients", // range axis label
				(DefaultKeyedValues2DDataset) this.creator.getDataset(), // data
				PlotOrientation.HORIZONTAL, 
				true, // include legend
				true, // tooltips
				false // urls
		);
		
		CategoryPlot plot = (CategoryPlot) this.chart.getPlot();
		CategoryAxis axis = (CategoryAxis) plot.getDomainAxis();
		axis.setTickLabelsVisible(false);
		
		// Set chart background color to it's parents background
		this.chart.setBackgroundPaint(new Color(this.parent.getBackground().getRed(),
				this.parent.getBackground().getGreen(), this.parent.getBackground().getBlue()));
		
		return this.chart;
	}

	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.ui.charts.AbstractChartComposite#initializeCreator()
	 */
	@Override
	protected AbstractDatasetCreator initializeCreator() {
		return new AgeHistogrammDatasetCreator(AgeHistogrammChart.CHART_TITLE);
	}
	
	/** (non-Javadoc)
	 * @see ch.unibe.iam.scg.archie.ui.charts.AbstractChartComposite#refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
		
		// Set cohort size according to preferences
		IPreferenceStore preferences = ArchieActivator.getDefault().getPreferenceStore();
		if(preferences.getInt(PreferenceConstants.P_COHORT_SIZE) > 0) {
			((AgeHistogrammDatasetCreator) this.creator).setCohortSize(preferences.getInt(PreferenceConstants.P_COHORT_SIZE));
		}
	}
}
