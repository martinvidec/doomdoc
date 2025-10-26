package at.videc.dummy;

/**
 * Annotation to specify the validation level for a field or method.
 * This annotation demonstrates all possible annotation element types.
 *
 * @author Jane Developer
 * @version 2.0.0
 * @since 1.5.0
 * @see Validator
 */
public @interface ValidationLevel {

    /**
     * The validation level as a string.
     * @return the validation level
     */
    String value() default "NORMAL";

    /**
     * Whether validation is strict.
     * @return true if strict validation is enabled
     */
    boolean strict() default false;

    /**
     * The priority of this validation.
     * @return the priority value
     */
    int priority() default 0;

    /**
     * The validation groups to apply.
     * @return array of group names
     */
    String[] groups() default {};

    /**
     * The validator class to use.
     * @return the validator class
     */
    Class<?> validatorClass() default Object.class;
}
