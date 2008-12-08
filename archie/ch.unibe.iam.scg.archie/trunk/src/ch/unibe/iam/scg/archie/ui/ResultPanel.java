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
package ch.unibe.iam.scg.archie.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import ch.elexis.Desk;
import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.i18n.Messages;

/**
 * TODO: DOCUMENT ME!
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ResultPanel extends Composite {

	private Group messageContainer;

	private GridData layoutData;

	private GridLayout layout;

	/**
	 * @param parent
	 * @param style
	 */
	public ResultPanel(final Composite parent, final int style) {
		super(parent, style);

		// although we're using a TableColumnLayout later in the TableFactory
		// we still need this to be able to show the loading message during
		// computations
		this.layout = new GridLayout();
		this.layout.marginHeight = 0;
		this.layout.marginWidth = 5;

		// prepare layout data for this result panel
		this.layoutData = new GridData();
		this.layoutData.verticalAlignment = GridData.FILL;
		this.layoutData.horizontalAlignment = GridData.FILL;
		this.layoutData.grabExcessVerticalSpace = true;
		this.layoutData.grabExcessHorizontalSpace = true;

		// set layout
		this.setLayoutData(this.layoutData);
		this.setLayout(this.layout);

		// create container for graphical message
		this.createMessageContainer();

		// add the intial message to the container
		new GraphicalMessage(this.messageContainer,
				ArchieActivator.getDefault().getImageRegistry().get(ArchieActivator.IMG_COFFEE), Messages.WORKING);
	}

	/**
	 * 
	 */
	private void createMessageContainer() {
		this.messageContainer = new Group(this, SWT.NONE);
		this.messageContainer.setLayout(this.layout);
		this.messageContainer.setLayoutData(this.layoutData);
		this.messageContainer.setBackground(Desk.getColor(Desk.COL_WHITE));
	}

	/**
	 * Removes the loading message
	 */
	public void removeLoadingMessage() {
		this.messageContainer.dispose();
	}

	/**
	 * 
	 */
	public void showEmptyMessage() {
		this.createMessageContainer();
		new GraphicalMessage(this.messageContainer, ArchieActivator.getDefault().getImageRegistry().get(ArchieActivator.IMG_INFO), Messages.RESULT_EMPTY);
	}
}
