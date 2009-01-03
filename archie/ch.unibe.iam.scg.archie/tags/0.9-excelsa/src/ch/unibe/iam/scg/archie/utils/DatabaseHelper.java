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
package ch.unibe.iam.scg.archie.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.elexis.data.Konsultation;
import ch.elexis.data.Patient;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.elexis.data.Rechnung;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.JdbcLink.Stm;

/**
 * <p>
 * Database helper class. Contains global database convenience methods for easy
 * access to general statistical data.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class DatabaseHelper {

	/**
	 * Returns the total number of patients in the system.
	 * 
	 * @return int number of patients in the system.
	 */
	public static int getNumberOfPatients() {
		Query<Patient> query = new Query<Patient>(Patient.class);
		return query.size();
	}

	/**
	 * Returns the total number of consultations in the system.
	 * 
	 * @return int number of consultations in the system.
	 */
	public static int getNumberOfConsultations() {
		Query<Konsultation> query = new Query<Konsultation>(Konsultation.class);
		return query.size();
	}

	/**
	 * Return the number of patients in the system that have the given gender.
	 * 
	 * @param gender
	 *            Patient's gender.
	 * @see ch.elexis.data.Person
	 * @return Number of patients with that gender, 0 if nothing found.
	 */
	public static int getNumberGenderPatients(String gender) {
		JdbcLink link = PersistentObject.getConnection();
		Stm statement = link.getStatement();
		ResultSet result = statement
				.query("SELECT Geschlecht, COUNT(ID) AS total FROM KONTAKT WHERE istPatient = '1' AND geschlecht = '"
						+ Patient.MALE + "' OR Geschlecht = '" + Patient.FEMALE
						+ "' AND deleted = '0' GROUP BY Geschlecht");
		try {
			while (result != null && result.next()) {
				if (result.getString("Geschlecht").equals(gender)) {
					return result.getInt("total");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			link.releaseStatement(statement);
		}
		return 0;
	}

	/**
	 * Returns the total number of invoices in the system.
	 * 
	 * @return Total number of invoices in the system
	 */
	public static int getTotalNumberOfInvoices() {
		Query<Rechnung> query = new Query<Rechnung>(Rechnung.class);
		return query.size();
	}

	/**
	 * Returns the number of invoices in the system with the given status.
	 * 
	 * @param status
	 *            Invoice status.
	 * @return Total number of invoices with the given status.
	 * @see ch.elexis.data.Rechnung
	 */
	public static int getNumberOfInvoices(int status) {
		JdbcLink link = PersistentObject.getConnection();
		Stm statement = link.getStatement();
		ResultSet result = statement
				.query("SELECT COUNT(id) AS total FROM RECHNUNGEN WHERE deleted = '0' AND RnStatus = '" + status + "'");
		try {
			if (result != null && result.next()) {
				return result.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			link.releaseStatement(statement);
		}

		return 0;
	}
}
