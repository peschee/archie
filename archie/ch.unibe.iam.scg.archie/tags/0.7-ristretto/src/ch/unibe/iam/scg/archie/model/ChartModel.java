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

package ch.unibe.iam.scg.archie.model;

import ch.unibe.iam.scg.archie.utils.ArrayUtils;

/**
 * <p>
 * Represents a model of a chart. Contains information on how to render a chart.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ChartModel {

	/**
	 * Constant for pie chart types, 1.
	 */
	public static final int CHART_PIE = 1;

	/**
	 * Constant for bar chart types, 2. Bar charts can also be handled as line
	 * charts as they both are created from a category dataset. There's a switch
	 * in the bar chart type that can be activated for line charts.
	 */
	public static final int CHART_BAR = 2;

	/**
	 * This switch can be activated for bar charts which makes them render as
	 * line charts.
	 */
	private boolean isLineChart;

	private String chartName;
	private DataSet dataSet;

	private int[] rows;
	private int[] columns;

	private int keysIndex;
	private int valuesIndex;
	private int rowTitleColumnIndex; // used in bar & line charts
	private int chartType;

	private boolean threeDimensional;

	/**
	 * 
	 */
	public ChartModel() {
		// Initialize with invalid, dummy data
		this.dataSet = null;
		this.chartName = null;

		this.rows = null;
		this.columns = null;

		this.keysIndex = -1;
		this.valuesIndex = -1;
		this.rowTitleColumnIndex = 0;
		this.chartType = -1;

		this.isLineChart = false;
		this.threeDimensional = false;
	}

	// ///////////////////////////////////////////////////////////////////////////
	// GETTERS / SETTERS
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * @param chartType
	 */
	public void setChartType(int chartType) {
		this.chartType = chartType;
	}

	/**
	 * 
	 * @return int chartType
	 */
	public int getChartType() {
		return this.chartType;
	}

	/**
	 * @return String ChartName
	 */
	public String getChartName() {
		return chartName;
	}

	/**
	 * 
	 * @param chartName
	 */
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	/**
	 * @return DataSet
	 */
	public DataSet getDataSet() {
		return dataSet;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasDataSet() {
		return this.dataSet != null;
	}

	/**
	 * @param dataSet
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * @return int keysIndex
	 */
	public int getKeysIndex() {
		return keysIndex;
	}

	/**
	 * @param keysIndex
	 */
	public void setKeysIndex(int keysIndex) {
		this.keysIndex = keysIndex;
	}

	/**
	 * @return int valuesIndex
	 */
	public int getValuesIndex() {
		return valuesIndex;
	}

	/**
	 * @param valuesIndex
	 */
	public void setValuesIndex(int valuesIndex) {
		this.valuesIndex = valuesIndex;
	}

	/**
	 * @param rows
	 */
	public void setRows(int[] rows) {
		this.rows = rows;
	}

	/**
	 * @return rows
	 */
	public int[] getRows() {
		return this.rows;
	}

	/**
	 * @param columns
	 */
	public void setColumns(int[] columns) {
		this.columns = columns;
	}

	/**
	 * @return rows
	 */
	public int[] getColumns() {
		return this.columns;
	}

	/**
	 * 
	 * @return threeDimensional
	 */
	public boolean isThreeDimensional() {
		return this.threeDimensional;
	}

	/**
	 * 
	 * @param isThreeDimensional
	 */
	public void setThreeDimensional(boolean isThreeDimensional) {
		this.threeDimensional = isThreeDimensional;
	}

	/**
	 * 
	 * @param columnIndex
	 */
	public void setRowTitleColumnIndex(int columnIndex) {
		this.rowTitleColumnIndex = columnIndex;
	}

	/**
	 * 
	 * @return
	 */
	public int getRowTitleColumnIndex() {
		return this.rowTitleColumnIndex;
	}

	/**
	 * 
	 * @param isLineChart
	 */
	public void setLineChart(boolean isLineChart) {
		this.isLineChart = isLineChart;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLineChart() {
		return this.isLineChart;
	}

	// ///////////////////////////////////////////////////////////////////////////
	// OVERRIDE METHODS
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder output = new StringBuilder();
		output.append("**********************\n");
		output.append("Chart Type: " + this.chartType + "\n");
		output.append("Chart Name: " + this.chartName + "\n");
		output.append("Keys Index: " + this.keysIndex + "\n");
		output.append("Values Index: " + this.valuesIndex + "\n");
		output.append("Rows: " + ArrayUtils.toString(this.rows) + "\n");
		output.append("Columns: " + ArrayUtils.toString(this.columns) + "\n");
		output.append("Category Column: " + this.rowTitleColumnIndex + "\n");
		output.append("Line Chart: " + this.isLineChart + "\n");
		output.append("3D: " + this.threeDimensional + "\n");
		output.append("\n" + this.dataSet.toString());
		output.append("**********************\n");
		return output.toString();
	}

	// ///////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * @return true if this model is valid
	 */
	public boolean isValid() {
		return this.hasValidChartType()
				&& (this.isValidPieChart() || this.isValidBarChart() || this.isValidLineChart());
	}

	/**
	 * 
	 * @return
	 */
	private boolean isValidPieChart() {
		return this.chartName != null && this.dataSet != null && this.chartType == ChartModel.CHART_PIE
				&& this.keysIndex != -1 && this.valuesIndex != -1 && this.rows != null && this.rows.length > 0;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isValidBarChart() {
		return this.chartName != null && this.dataSet != null && this.chartType == ChartModel.CHART_BAR
				&& this.rowTitleColumnIndex >= 0 && this.columns != null && this.columns.length > 0
				&& this.rows != null && this.rows.length > 0 && !this.isLineChart;
	}

	/**
	 * Valid line charts have the same definitions as bar charts, as they're
	 * only a variation of those.
	 * 
	 * @return
	 */
	private boolean isValidLineChart() {
		return this.chartName != null && this.dataSet != null && this.chartType == ChartModel.CHART_BAR
				&& this.rowTitleColumnIndex >= 0 && this.columns != null && this.columns.length > 0
				&& this.rows != null && this.rows.length > 0 && this.isLineChart;
	}

	/**
	 * @return true, if this model has a valid chartType defined.
	 */
	public boolean hasValidChartType() {
		return this.chartType == ChartModel.CHART_BAR || this.chartType == ChartModel.CHART_PIE;
	}
}
