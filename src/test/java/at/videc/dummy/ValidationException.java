package at.videc.dummy;

/**
 * Exception thrown when validation fails.
 * Demonstrates simple exception class.
 *
 * @author Validation Framework Team
 * @version 1.0.0
 * @since 1.0.0
 * @see Validator
 */
public class ValidationException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new validation exception with null message.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructs a new validation exception with the specified message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new validation exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new validation exception with the specified cause.
     *
     * @param cause the cause
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }
}
