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
package ch.unibe.iam.scg.archie.acl;

import ch.elexis.Hub;
import ch.elexis.admin.IACLContributor;
import ch.unibe.iam.scg.archie.ArchieActivator;
import ch.unibe.iam.scg.archie.i18n.Messages;

/**
 * Handles the access to Archie based on access control lists defined in Elexis
 * properties.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ArchieACL implements IACLContributor {

	/**
	 * Access control string that will be displayed in the ACL.
	 */
	public static final String USE_ARCHIE = ArchieActivator.PLUGIN_NAME + " " + Messages.ACL_ACCESS;

	/**
	 * Returns the ACL for this plugin.
	 * 
	 * @return String[]
	 */
	public String[] getACL() {
		return new String[] { ArchieACL.USE_ARCHIE };
	}

	/** {@inheritDoc} */
	public String[] reject(final String[] acl) {
		return null;
	}

	/**
	 * Static function to check whether the currently active user has access to
	 * archie or not.
	 * 
	 * @return boolean True if the current user can access archie, false else.
	 */
	public static boolean userHasAccess() {
		return Hub.actUser != null && Hub.actMandant != null && Hub.acl.request(ArchieACL.USE_ARCHIE);
	}

}
