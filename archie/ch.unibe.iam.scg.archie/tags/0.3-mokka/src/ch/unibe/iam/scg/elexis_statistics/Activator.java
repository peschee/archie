package ch.unibe.iam.scg.elexis_statistics;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle and holds an image
 * registry for images used throughout the plugin.
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch.unibe.iam.scg.elexis_statistics";

	// Images
	public static final String IMG_NEW_QUERY = "query"; //$NON-NLS-1$
	public static final String IMG_COFFEE = "coffee"; //$NON-NLS-1$
	public static final String IMG_IMPORTANT = "important"; //$NON-NLS-1$
	public static final String IMG_BUTTON_CALENDAR = "buttoCalendar"; //$NON-NLS-1$
	public static final String IMG_DEC_VALID = "decorationValid"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
		Activator.plugin = this;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.plugin = null;
		super.stop(context);
	}

	/**
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return Activator.plugin;
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

	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		super.initializeImageRegistry(registry);

		// put images into the image registry for this plugin
		registry.put(IMG_NEW_QUERY, Activator.getImageDescriptor("icons/database_go.png"));
		registry.put(IMG_COFFEE, Activator.getImageDescriptor("icons/kteatime.png"));
		registry.put(IMG_IMPORTANT, Activator.getImageDescriptor("icons/important.png"));
		registry.put(IMG_BUTTON_CALENDAR, Activator.getImageDescriptor("icons/calendar.png"));
		registry.put(IMG_DEC_VALID, Activator.getImageDescriptor("icons/tick.png"));
	}
}
