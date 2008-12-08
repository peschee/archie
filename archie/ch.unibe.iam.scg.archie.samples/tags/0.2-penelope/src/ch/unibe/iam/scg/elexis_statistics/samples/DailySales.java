package ch.unibe.iam.scg.elexis_statistics.samples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import ch.elexis.Hub;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Query;
import ch.unibe.iam.scg.elexis_statistics.IStatSource;

public class DailySales implements IStatSource {

	private TimeSeriesCollection dataSet;

	public DailySales() {
		this.dataSet = new TimeSeriesCollection();
	}

	@Override
	public XYDataset getDataSet() {
		Query<Konsultation> query = new Query<Konsultation>(Konsultation.class);

		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.getTime());

		Map<Date, Double> saleDate = new TreeMap<Date, Double>();

		for (int i = 50; i > 0; i--) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String dateString = format.format(calendar.getTime());
			System.out.println(dateString);

			query.clear();
			query.add("Datum", "=", dateString);
			query.add("Mandant", "=", Hub.actMandant.getId());
			
			List<Konsultation> consults = query.execute();

			Double totalSales = 0.0;
			for (Iterator<Konsultation> iterator = consults.iterator(); iterator.hasNext();) {
				Konsultation konsultation = iterator.next();
				totalSales += konsultation.getUmsatz();
			}

			saleDate.put(calendar.getTime(), totalSales/100);

			calendar.add(Calendar.DAY_OF_WEEK, -1);
		}

		Set<Map.Entry<Date, Double>> entries = saleDate.entrySet();
		TimeSeries timeSeries = new TimeSeries("Tagesumsätze", Day.class);
		
		Iterator<Map.Entry<Date, Double>> it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry<Date, Double> entry = (Map.Entry<Date, Double>) it.next();
			timeSeries.add(new Day(entry.getKey()), entry.getValue());
		}
		this.dataSet.addSeries(timeSeries);

		return this.dataSet;
	}

	@Override
	public String getDescription() {
		return "Statistik der Tagesumsätze.";
	}

	@Override
	public String getName() {
		return "Tagesumsätze";
	}

	@Override
	public JFreeChart getChart() {
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Tagesumsätze", 
				"Tag", 
				"Umsatz", 
				this.getDataSet(), 
				true, 
				true, 
				false
		);
		XYPlot plot = (XYPlot) chart.getPlot();
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("E, dd.MM.yyyy"));
		return chart;
	}

}
