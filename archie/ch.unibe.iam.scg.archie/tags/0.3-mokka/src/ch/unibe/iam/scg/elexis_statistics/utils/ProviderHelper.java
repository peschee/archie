/**
 * 
 */
package ch.unibe.iam.scg.elexis_statistics.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import ch.unibe.iam.scg.elexis_statistics.annotations.GetProperty;
import ch.unibe.iam.scg.elexis_statistics.annotations.SetProperty;
import ch.unibe.iam.scg.elexis_statistics.model.AbstractDataProvider;

/**
 * <p>
 * Utility class providing helper functions for data providers. This class
 * provides convenience methods for annotation retrieval and sorting. This class
 * also handles the retrieval and setting of annotation methods and values for a
 * given provider.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class ProviderHelper {

	/** magic constant for getter methods */
	private static final int GETTER = 0;

	/** magic constant for setter methods */
	private static final int SETTER = 1;

	/**
	 * Returns a map of getter methods for providers mapped from the method's
	 * name to it's value.
	 * 
	 * @param provider
	 *            AbstractDataProvider to retrieve the methods from.
	 * @param sorted
	 *            If true, the methods will be sorted according to their index.
	 * @return Map with getter method names and values.
	 */
	public static Map<String, String> getGetterMap(final AbstractDataProvider provider, final boolean sorted) {
		LinkedHashMap<String, String> getterMap = new LinkedHashMap<String, String>();

		for (Method method : ProviderHelper.getGetterMethods(provider, sorted)) {
			GetProperty getter = method.getAnnotation(GetProperty.class);
			getterMap.put(getter.name(), ProviderHelper.getValue(method, provider));
		}

		return getterMap;
	}

	/**
	 * Convenient method to retrieve all getters from a given provider.
	 * 
	 * @param provider
	 *            AbstractDataProvider to retrieve the methods from.
	 * @param sorted
	 *            If true, the methods will be sorted according to their index.
	 * @return List of getter methods for a given provider.
	 */
	public static ArrayList<Method> getGetterMethods(final AbstractDataProvider provider, final boolean sorted) {
		return ProviderHelper.getMethods(provider, sorted, ProviderHelper.GETTER);
	}

	/**
	 * Convenient method to retrieve all setters from a given provider.
	 * 
	 * @param provider
	 *            AbstractDataProvider to retrieve the methods from.
	 * @param sorted
	 *            If true, the methods will be sorted according to their index.
	 * @return List of setter methods for a given provider.
	 */
	public static ArrayList<Method> getSetterMethods(final AbstractDataProvider provider, final boolean sorted) {
		return ProviderHelper.getMethods(provider, sorted, ProviderHelper.SETTER);
	}

	/**
	 * Retrieves the value of a given method for a given provider by invoking
	 * the method on the given provider.
	 * 
	 * @param method
	 *            Method to invoke.
	 * @param provider
	 *            Provider where the method will be invoked.
	 * @return Value of the method invoked.
	 */
	public static String getValue(final Method method, final AbstractDataProvider provider) {
		try {
			return (String) method.invoke(provider);
		} catch (Exception e) {
			// TODO: log
		}
		return "error";
	}

	/**
	 * Sets the value of a given method for a given provider by invoking the
	 * method on the given provider.
	 * 
	 * @param method
	 *            Method to invoke.
	 * @param provider
	 *            Provider where the method will be invoked.
	 * @param value
	 *            Value to use for the method invokation.
	 * @throws Exception
	 *             TODO: Document.
	 */
	public static void setValue(final AbstractDataProvider provider, final Method method, final String value)
			throws Exception {
		try {
			method.invoke(provider, value);
		} catch (Exception e) {
			throw (Exception) e.getCause();
		}
	}

	/**
	 * Internal method for retrieving a list of methods for a given provider.
	 * 
	 * @param provider
	 *            AbstractDataProvider to retrieve the methods from.
	 * @param sorted
	 *            If true, the methods will be sorted according to their index.
	 * @param which
	 *            What method type, either getter or setter, to retrieve.
	 * @return List of either getter or setter methods for a given provider.
	 */
	private static ArrayList<Method> getMethods(final AbstractDataProvider provider, final boolean sorted,
			final int which) {
		ArrayList<Method> methods = new ArrayList<Method>();

		// get all provider methods and only keep the getter annotations
		for (Method method : provider.getClass().getMethods()) {
			if (which == ProviderHelper.GETTER && method.isAnnotationPresent(GetProperty.class)) {
				methods.add(method);
			} else if (which == ProviderHelper.SETTER && method.isAnnotationPresent(SetProperty.class)) {
				methods.add(method);
			}
		}

		// sort if applicable
		if (sorted) {
			ProviderHelper.sortMethodList(methods);
		}

		return methods;
	}

	/**
	 * Sorts the methods according to the index of the property annotation.
	 * 
	 * @param methodList
	 *            a list containing only methods having a Set/GetProperty
	 *            annotation.
	 */
	private static void sortMethodList(ArrayList<Method> methodList) {
		Collections.sort(methodList, new Comparator<Method>() {

			public int compare(Method o1, Method o2) {
				int index1 = 0;
				int index2 = 0;
				if (o1.isAnnotationPresent(GetProperty.class)) {
					GetProperty anno1 = o1.getAnnotation(GetProperty.class);
					GetProperty anno2 = o2.getAnnotation(GetProperty.class);
					index1 = anno1.index();
					index2 = anno2.index();
				} else { // has to have a SetProperty annotation
					SetProperty anno1 = o1.getAnnotation(SetProperty.class);
					SetProperty anno2 = o2.getAnnotation(SetProperty.class);
					index1 = anno1.index();
					index2 = anno2.index();
				}

				return index1 - index2;
			}
		});
	}
}