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

/**
 * <p>Main package, holds classes that are needed to activate this plugin.</p>
 */
package ch.unibe.iam.scg.archie;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.elexis.Hub;
import ch.elexis.preferences.SettingsPreferenceStore;
import ch.elexis.util.Log;

/**
 * <p>The activator class controls the plug-in life cycle and holds an image
 * registry for images used throughout the PLUGIN.</p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ArchieActivator extends AbstractUIPlugin {
	
	/** The shared instance */
	private static ArchieActivator PLUGIN;

	/** The plug-in ID */
	public static final String PLUGIN_ID = "ch.unibe.iam.scg.archie"; //$NON-NLS-1$
	
	/** Human readable PLUGIN name. */
	public static final String PLUGIN_NAME = "Archie"; //$NON-NLS-1$

	// Images
	public static final String IMG_NEW_QUERY = "query"; //$NON-NLS-1$
	public static final String IMG_COFFEE = "coffee"; //$NON-NLS-1$
	public static final String IMG_IMPORTANT = "important"; //$NON-NLS-1$
	public static final String IMG_WARNING = "warningt"; //$NON-NLS-1$
	public static final String IMG_ERROR = "error"; //$NON-NLS-1$
	public static final String IMG_INFO = "info"; //$NON-NLS-1$
	public static final String IMG_CANCEL = "cancel"; //$NON-NLS-1$
	public static final String IMG_BUTTON_CALENDAR = "buttoCalendar"; //$NON-NLS-1$
	public static final String IMG_DEC_VALID = "decorationValid"; //$NON-NLS-1$
	public static final String IMG_CHART_PIE_BIG = "chartPieBig"; //$NON-NLS-1$
	public static final String IMG_CHART_BAR_BIG = "chartBarBig"; //$NON-NLS-1$
	
	/** Preference store for this PLUGIN. */
	private static IPreferenceStore PREFERENCE_STORE = null;
	
	/** Log for this plugin. */
	public static final Log LOG = Log.get(ArchieActivator.PLUGIN_NAME);

	/** The constructor */
	public ArchieActivator() {
		ArchieActivator.PLUGIN = this;
		//TODO: i18n.
		ArchieActivator.LOG.log("Archie plugin started", Log.SYNCMARK);
	}

	/** {@inheritDoc} */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/** {@inheritDoc} */
	@Override
	public void stop(BundleContext context) throws Exception {
		ArchieActivator.PLUGIN = null;
		super.stop(context);
	}

	/**
	 * @return the shared instance
	 */
	public static ArchieActivator getDefault() {
		return ArchieActivator.PLUGIN;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Returns an image from this PLUGIN's image registry based on the given descriptor string.
	 * @param descriptor Image descriptor string.
	 * @return Image under that given descriptor from the registry.
	 */
	public static Image getImage(String descriptor) {
		return ArchieActivator.getDefault().getImageRegistry().get(descriptor);
	}
	
	/** {@inheritDoc} */
	@Override
	public IPreferenceStore getPreferenceStore() {
		if(ArchieActivator.PREFERENCE_STORE == null) {
			ArchieActivator.PREFERENCE_STORE = new SettingsPreferenceStore(Hub.globalCfg);
		}
		return ArchieActivator.PREFERENCE_STORE;
	}
	
	/** {@inheritDoc} */
	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		super.initializeImageRegistry(registry);

		// put images into the image registry for this PLUGIN
		registry.put(IMG_NEW_QUERY, ArchieActivator.getImageDescriptor("icons/database_go.png"));
		registry.put(IMG_COFFEE, ArchieActivator.getImageDescriptor("icons/kteatime.png"));
		registry.put(IMG_IMPORTANT, ArchieActivator.getImageDescriptor("icons/important.png"));
		registry.put(IMG_WARNING, ArchieActivator.getImageDescriptor("icons/warning.png"));
		registry.put(IMG_ERROR, ArchieActivator.getImageDescriptor("icons/error.png"));
		registry.put(IMG_INFO, ArchieActivator.getImageDescriptor("icons/info.png"));
		registry.put(IMG_CANCEL, ArchieActivator.getImageDescriptor("icons/cancel.png"));
		registry.put(IMG_BUTTON_CALENDAR, ArchieActivator.getImageDescriptor("icons/calendar.png"));
		registry.put(IMG_DEC_VALID, ArchieActivator.getImageDescriptor("icons/tick.png"));
		registry.put(IMG_CHART_PIE_BIG, ArchieActivator.getImageDescriptor("icons/chart_pie_big.png"));
		registry.put(IMG_CHART_BAR_BIG, ArchieActivator.getImageDescriptor("icons/chart_bar_big.png"));
	}
}