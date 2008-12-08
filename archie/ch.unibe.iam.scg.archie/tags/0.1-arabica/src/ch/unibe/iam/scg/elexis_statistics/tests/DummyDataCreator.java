package ch.unibe.iam.scg.elexis_statistics.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.rgw.tools.StringTool;

public class DummyDataCreator {

	private List<String> forenames, lastnames, sex, years, months, days;

	public DummyDataCreator() {
		this.populateLists();
	}

	public static void main(String[] args) {
		DummyDataCreator creator = new DummyDataCreator();

		for (int i = 0; i < 200; i++) {
			String first = creator.getRandomForename();
			String last = creator.getRandomLastname();
			String birthday = creator.getRandomYear() + creator.getRandomMonth() + creator.getRandomDay();
			String sex = creator.getRandomSex();
			System.out.println("INSERT INTO `kontakt` " 
					+ "(`ID`,`deleted`,`istOrganisation`,`istPerson`,`istPatient`,`istAnwender`,`istMandant`,`istLabor`,`Land`,`Geburtsdatum`,`Geschlecht`,`TITEL`,`Bezeichnung1`,`Bezeichnung2`,`Bezeichnung3`,`Strasse`,`Plz`,`Ort`,`Telefon1`,`Telefon2`,`Fax`,`NatelNr`,`EMail`,`Website`,`Gruppe`,`PatientNr`,`Anschrift`,`Bemerkung`,`Diagnosen`,`PersAnamnese`,`SysAnamnese`,`FamAnamnese`,`Risiken`,`Allergien`,`ExtInfo`)" 
					+ " VALUES ("
					+ "'" + StringTool.unique("prso") + "',"
					+ "'0','0','1','1','0','0','0',NULL,"
					+ "'" + birthday + "',"
					+ "'" + sex + "',NULL,"
					+ "'" + last + "',"
					+ "'" + first
			+ "',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','','',NULL,NULL,'');");
		}
	}

	private String getRandomForename() {
		Collections.shuffle(this.forenames);
		return this.forenames.get(this.forenames.size()-1);
	}

	private String getRandomLastname() {
		Collections.shuffle(this.lastnames);
		return this.lastnames.get(this.lastnames.size()-1);
	}

	private String getRandomSex() {
		Collections.shuffle(this.sex);
		return this.sex.get(this.sex.size()-1);
	}

	private String getRandomYear() {
		Collections.shuffle(this.years);
		return this.years.get(this.years.size()-1);
	}

	private String getRandomMonth() {
		Collections.shuffle(this.months);
		return this.months.get(this.months.size()-1);
	}

	private String getRandomDay() {
		Collections.shuffle(this.days);
		return this.days.get(this.days.size()-1);
	}

	private void populateLists() {
		// add forenames
		this.forenames = new ArrayList<String>();
		this.forenames.add("Paul");
		this.forenames.add("Hans");
		this.forenames.add("Johan");
		this.forenames.add("Patrick");
		this.forenames.add("Peter");
		this.forenames.add("Dennis");
		this.forenames.add("Simon");
		this.forenames.add("Paula");
		this.forenames.add("Petra");
		this.forenames.add("Simone");
		this.forenames.add("Silvia");
		this.forenames.add("Gabrielle");
		this.forenames.add("Matrina");

		// add lastnames
		this.lastnames = new ArrayList<String>();
		this.lastnames.add("Meier");
		this.lastnames.add("Mutz");
		this.lastnames.add("Gerber");
		this.lastnames.add("Müller");
		this.lastnames.add("Iwanowitsch");
		this.lastnames.add("Stronko");
		this.lastnames.add("Peker");
		this.lastnames.add("Kubina");

		// add sex
		this.sex = new ArrayList<String>();
		this.sex.add("m");
		this.sex.add("w");

		// add years
		this.years = new ArrayList<String>();
		for (int i = 1920; i < 1980; i++) {
			this.years.add(new Integer(i).toString());
		}

		// add months
		this.months = new ArrayList<String>();
		for (int i = 1; i <= 12; i++) {
			if(i < 10) {
				this.months.add("0" + new Integer(i).toString());	
			} else {
				this.months.add(new Integer(i).toString());
			}
		}

		// add days
		this.days = new ArrayList<String>();
		for (int i = 1; i <= 31; i++) {
			if(i < 10) {
				this.days.add("0" + new Integer(i).toString());
			} else {
				this.days.add(new Integer(i).toString());
			}
			
		}
	}
}
