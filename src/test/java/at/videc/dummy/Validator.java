package at.videc.dummy;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for validating data objects.
 * Demonstrates interface with generics, default methods, static methods, and constants.
 *
 * <p>This interface provides a comprehensive validation framework with the following features:
 * <ul>
 *   <li>Generic type parameter for flexible validation</li>
 *   <li>Default validation methods with fallback behavior</li>
 *   <li>Static factory methods for common validators</li>
 * </ul>
 *
 * @param <T> the type of object to validate
 * @author Jane Developer
 * @version 3.0.0
 * @since 1.0.0
 * @see ValidationLevel
 * @see ValidationResult
 */
public interface Validator<T> extends Closeable {

    /**
     * Default validation level constant.
     */
    int DEFAULT_LEVEL = 1;

    /**
     * Maximum validation attempts.
     */
    int MAX_ATTEMPTS = 3;

    /**
     * Validation timeout in milliseconds.
     */
    long TIMEOUT_MS = 5000L;

    /**
     * Validates the given object.
     *
     * @param object the object to validate
     * @return the validation result
     * @throws ValidationException if validation fails critically
     * @throws IllegalArgumentException if object is null
     * @see #validateWithLevel(Object, int)
     */
    ValidationResult validate(T object) throws ValidationException;

    /**
     * Validates an object with a specific validation level.
     * Default implementation delegates to {@link #validate(Object)}.
     *
     * @param object the object to validate
     * @param level the validation level (higher = stricter)
     * @return the validation result
     * @throws ValidationException if validation fails
     * @since 2.0.0
     */
    default ValidationResult validateWithLevel(T object, int level) throws ValidationException {
        return validate(object);
    }

    /**
     * Checks if the validator supports a specific type.
     *
     * @param type the class type to check
     * @return true if this validator supports the type
     */
    default boolean supports(Class<?> type) {
        return true;
    }

    /**
     * Resets the validator state to initial conditions.
     * Default implementation does nothing.
     *
     * @since 2.5.0
     */
    default void reset() {
        // Default: no-op
    }

    /**
     * Closes this validator and releases resources.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    void close() throws IOException;

    /**
     * Creates a no-op validator that always returns valid.
     *
     * @param <T> the type parameter
     * @return a validator that accepts everything
     * @since 3.0.0
     */
    static <T> Validator<T> noOp() {
        return new Validator<T>() {
            @Override
            public ValidationResult validate(T object) {
                return ValidationResult.valid();
            }

            @Override
            public void close() {
                // No resources to close
            }
        };
    }

    /**
     * Creates a strict validator.
     *
     * @param <T> the type parameter
     * @return a strict validator instance
     * @since 3.0.0
     */
    static <T> Validator<T> strict() {
        return new Validator<T>() {
            @Override
            public ValidationResult validate(T object) throws ValidationException {
                if (object == null) {
                    throw new ValidationException("Object must not be null");
                }
                return ValidationResult.valid();
            }

            @Override
            public void close() {
                // No resources to close
            }
        };
    }
}
