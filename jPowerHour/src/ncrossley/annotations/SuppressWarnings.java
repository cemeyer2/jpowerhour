package ncrossley.annotations;

import java.lang.annotation.*;

// Adapted from FindBugs and standard Java annotations;
// No copyright by Nick Crossley

/**
 * The SuppressWarnings annotation may be used to suppress compiler and Findbugs
 * warnings.  The standard SuppressWarnings annotation, from java.lang.annotation,
 * is not retained in the class files, and so cannot be used by Findbugs.
 * This version of the annotation is retained in the class files.
 * It also has an optional 'justification' string, where the developer can
 * explain why this warning should be suppressed in some instance.
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD,
		 ElementType.PARAMETER, ElementType.CONSTRUCTOR,
		 ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)
public @interface SuppressWarnings
{
	/**
	 * The set of warnings that are to be suppressed by Findbugs in the
	 * annotated element.  Duplicate names are permitted.  The second and
	 * successive occurrences of a name are ignored.  The presence of
	 * unrecognized warning names is <i>not</i> an error.
	 * @return an array of short Findbugs error codes, such as {"Dm",SBSC"}.
	 */
	String[] value() default {};

	/**
	 * A justification for suppressing some specific instance of a warning.
	 * @return an explanatory message.
	 */
	String justification() default "";
}
