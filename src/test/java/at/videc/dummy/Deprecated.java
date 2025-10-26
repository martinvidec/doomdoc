package at.videc.dummy;

/**
 * Marks an element as deprecated.
 *
 * @deprecated This annotation is deprecated. Use {@link ValidationLevel} instead.
 * @author Legacy Developer
 * @version 1.0.0
 * @since 0.1.0
 */
@java.lang.Deprecated
public @interface Deprecated {

    /**
     * The reason for deprecation.
     * @return the deprecation reason
     */
    String reason() default "No longer maintained";

    /**
     * The version when this was deprecated.
     * @return the version string
     */
    String since() default "1.0.0";
}
