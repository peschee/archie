package ch.unibe.iam.scg.elexis_statistics.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.unibe.iam.scg.elexis_statistics.ui.FieldTypes;

/**
 * Marks a method as getter. Used by the view to determine which elements can be
 * changed by the user. This is a model driven design. The value of this
 * annotation is used to describe the field in the view.
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
	 * Field name. There has to be a setter method annotated with a
	 * SetProperty method with the same value to have any effect on the query.
	 * 
	 * @return String name
	 */
	public String name();

	/**
	 * A brief description about the getter method.
	 * 
	 * @return String description
	 */
	public String description() default "";

	/** 
	 * Field index. Defines the order in which the fields are displayed. 
	 * 
	 * @return Integer ordering
	 */
	public int index() default -1;

	/** Field type. What kind of field? */
	public FieldTypes fieldType() default FieldTypes.STANDARD;

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
