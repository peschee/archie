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
package ch.unibe.iam.scg.archie.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.unibe.iam.scg.archie.ui.widgets.WidgetTypes;

/**
 * <p>
 * Marks a method as getter. Used by the view to determine which elements can be
 * changed by the user. This is a model driven design. The value of this
 * annotation is used to describe the field in the view.
 * </p>
 * 
 * $Id$
 * 
 * @author Peter Siska
 * @author Dennis Schenk
 * @version $Rev$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetProperty {

	/**
	 * Property name. There has to be a setter method annotated with a
	 * SetProperty method with the same value to have any effect on the data
	 * provider. This name will also be the title of the corresponding widget
	 * shown in the UI.
	 * 
	 * @return String name
	 */
	public String name();

	/**
	 * A brief description about the getter method. This is used for tooltips in
	 * the UI.
	 * 
	 * @return String A description.
	 */
	public String description() default "";

	/**
	 * Property index. Defines the order in which this property is displayed and
	 * processed.
	 * 
	 * @return Integer Order in which the fields are displayed and processed.
	 */
	public int index() default -1;

	/** Widget type. What kind of widget? */
	public WidgetTypes widgetType() default WidgetTypes.TEXT;

	/**
	 * A regular expression pattern string to be performed as validations on the
	 * given method.
	 * 
	 * @see java.util.regex.Pattern
	 * @return String Regular expression pattern.
	 */
	public String validationRegex() default "";

	/**
	 * A validation error message that will be displayed upon unsuccessful
	 * validation of the input for the method containing this annotation.
	 * 
	 * @return String Validation error message.
	 */
	public String validationMessage() default "";
}
