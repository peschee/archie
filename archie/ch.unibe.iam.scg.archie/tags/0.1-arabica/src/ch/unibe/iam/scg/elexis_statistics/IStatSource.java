package ch.unibe.iam.scg.elexis_statistics;

import org.jfree.data.general.Dataset;

/**
 * Test interface for getting data sources from classes.
 * @author psiska
 */
public interface IStatSource {
	
	public String getName();
	
	public String getDescription();
	
	public Dataset getDataSet();
}
