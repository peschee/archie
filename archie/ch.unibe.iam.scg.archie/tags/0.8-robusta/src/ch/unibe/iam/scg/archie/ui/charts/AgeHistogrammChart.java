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
 * <p>
 * TODO: DOCUMENT ME!
 * </p>
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

	/**
	 * @see ch.unibe.iam.scg.archie.ui.charts.AbstractChartComposite#initializeChart()
	 */
	@Override
	protected JFreeChart initializeChart() {
		JFreeChart chart = ChartFactory.createStackedBarChart(AgeHistogrammChart.CHART_TITLE, "Age", // domain
				// axis
				// label
				"Patients", // range axis label
				(DefaultKeyedValues2DDataset) this.creator.getDataset(), // data
				PlotOrientation.HORIZONTAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// set tooltip renderer
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.getRenderer().setBaseToolTipGenerator(new HistogramTooltipGenerator());
		
		// hide tick labels
		CategoryAxis axis = (CategoryAxis) plot.getDomainAxis();
		axis.setTickLabelsVisible(false);

		// Set chart background color to it's parents background
		chart.setBackgroundPaint(new Color(this.parent.getBackground().getRed(), this.parent.getBackground()
				.getGreen(), this.parent.getBackground().getBlue()));

		return chart;
	}

	/**
	 * @see ch.unibe.iam.scg.archie.ui.charts.AbstractChartComposite#initializeCreator()
	 */
	@Override
	protected AbstractDatasetCreator initializeCreator() {
		return new AgeHistogrammDatasetCreator(AgeHistogrammChart.CHART_TITLE, this.getCohortSize());
	}

	/**
	 * @see ch.unibe.iam.scg.archie.ui.charts.AbstractChartComposite#refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
		((AgeHistogrammDatasetCreator) this.creator).setCohortSize(this.getCohortSize());
	}

	/**
	 * Set cohort size according to preferences, if set, else return default
	 * value.
	 */
	private int getCohortSize() {

		IPreferenceStore preferences = ArchieActivator.getDefault().getPreferenceStore();

		if (preferences.getInt(PreferenceConstants.P_COHORT_SIZE) > 0) {
			return preferences.getInt(PreferenceConstants.P_COHORT_SIZE);
		}
		return PreferenceConstants.DEFAULT_COHORT_SIZE;
	}
}