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

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * <p>TODO: DOCUMENT ME!</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class TableManager {

	/**
	 * Instance of this table manager. There's always only one thorugh the
	 * entire lifecycle of this application.
	 */
	private static TableManager INSTANCE;

	/**
	 * The currently managed data table.
	 */
	private Table table;
	

	/**
	 * Private constructor.
	 */
	private TableManager() {
		this.table = null;
	}
	
	/**
	 * Returns an instance of this table manager.
	 * 
	 * @return An instance of this table manager.
	 */
	public static TableManager getInstance() {
		if (TableManager.INSTANCE == null) {
			TableManager.INSTANCE = new TableManager();
		}
		return TableManager.INSTANCE;
	}
	
	/**
	 * Sets the table for this manager.
	 * 
	 * @param table
	 *            A SWT table reference from the result panel.
	 */
	public void setTable(Table table) {
		this.table = table;
	}
	
	/**
	 * @return sorting direction
	 */
	public int getSortDirection() {
		int direction = -1;
		if(this.hasTable()) {
			return this.table.getSortDirection();
		}
		return direction;
	}
	
	/**
	 * @return TableColumn which is used for sorting at the moment.
	 */
	public TableColumn getSortColumn() {
		TableColumn column = null;
		if(this.hasTable()) {
			return this.table.getSortColumn();			
		}
		return column;
	}
	
	/**
	 * @return whether we have a table or not.
	 */
	public boolean hasTable() {
		return this.table != null;
	}

}
