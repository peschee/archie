package ch.unibe.iam.scg.elexis_statistics.users;

import java.util.TreeMap;

import org.jfree.data.category.DefaultCategoryDataset;

import ch.elexis.Hub;
import ch.elexis.actions.AbstractDataLoaderJob;
import ch.elexis.data.Patient;
import ch.unibe.iam.scg.elexis_statistics.IStatSource;

public class UserStats implements IStatSource {

	public UserStats() {

	}

	@Override
	public String getDescription() {
		return "User Statistics";
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	public DefaultCategoryDataset getDataSet() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		//ArrayList<String> columns = new ArrayList<String>();
		
		int m = 0;
		int w = 0;;
		int x = 0;;
		
		AbstractDataLoaderJob loader = (AbstractDataLoaderJob) Hub.jobPool
		.getJob("PatientenListe");
		
		Object[] patients = (Object[]) loader.getData();
		
		for (int i = 0; i < patients.length; i++) {
			Patient patient = (Patient) patients[i];
			
			String sex = patient.getGeschlecht();
			
			//dirty
			if(sex.equals("m")) {
				m++;
			}
			if (sex.equals("w")) {
				w++;
			}
			else {
				x++;
			}

		}
		dataset.addValue(m, "Männlich", "Geschlecht");
		dataset.addValue(w, "Weiblich", "Geschlecht");
		dataset.addValue(x, "Falsch oder nicht definiert", "Geschlecht");
		
		return dataset;
	}
	
	public DefaultCategoryDataset getAnotherDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
		AbstractDataLoaderJob loader = (AbstractDataLoaderJob) Hub.jobPool
		.getJob("PatientenListe");
		
		TreeMap<Integer, Integer> columns = new TreeMap<Integer, Integer>();
		
		Object[] patients = (Object[]) loader.getData();
		
		for (int i = 0; i < patients.length; i++) {
			Patient patient = (Patient) patients[i];
			
			int age = Integer.valueOf(patient.getAlter()).intValue();
			
			int newNumber = 0;
			
			System.out.println(age);
			if (columns.get(age) != null) {
				newNumber = columns.get(age) + 1;
			}
			columns.put(age, newNumber);
	
		}
		
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i) != null) {
				dataset.addValue(i, columns.get(i), "Alter");
			}
		}
		return dataset;
	}
}
