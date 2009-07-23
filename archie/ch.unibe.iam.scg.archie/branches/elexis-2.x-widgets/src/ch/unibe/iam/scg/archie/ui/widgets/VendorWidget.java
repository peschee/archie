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
package ch.unibe.iam.scg.archie.ui.widgets;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.widgets.Composite;

import ch.unibe.iam.scg.archie.model.RegexValidation;

/**
 * <p>
 * Wrapper class for custom <b>vendor specific</b> widgets. <i>PLEASE USE THIS
 * ONLY IF YOU REALLY KNOW WHAT YOU'RE DOING</i>. Wrapps around an abstract
 * widget class which is instanciated based on the given
 * <code>vendorClass</code> parameter in the constructor.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
public class VendorWidget extends AbstractWidget {

	private AbstractWidget widget;

	/**
	 * @param parent
	 * @param style
	 * @param labelText
	 */
	@SuppressWarnings("unchecked")
	public VendorWidget(Composite parent, int style, final String labelText, RegexValidation regex, Class<?> vendorClass) {
		super(parent, style, labelText);

		// instantiate vendor class
		Class<AbstractWidget> abstractWidgetClass = (Class<AbstractWidget>) vendorClass;
		try {
			this.widget = abstractWidgetClass.getConstructor(
					new Class[] { Composite.class, int.class, String.class, RegexValidation.class }).newInstance(
					parent, style, labelText);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Object getValue() {
		return this.widget.getValue();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public boolean isValid() {
		return this.widget.isValid();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void setDescription(String description) {
		this.widget.setDescription(description);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void setValue(Object value) {
		this.widget.setValue(value);
	}
}