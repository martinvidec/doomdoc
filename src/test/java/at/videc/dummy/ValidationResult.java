package at.videc.dummy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Result of a validation operation.
 * Demonstrates final class with factory methods.
 *
 * @author Validation Framework Team
 * @version 2.0.0
 * @since 1.0.0
 * @see Validator
 */
public final class ValidationResult {

    /**
     * Whether the validation was successful.
     */
    private final boolean valid;

    /**
     * List of validation error messages.
     */
    private final List<String> errors;

    /**
     * Constructs a validation result.
     *
     * @param valid whether validation passed
     * @param errors list of error messages
     */
    private ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = new ArrayList<>(errors);
    }

    /**
     * Checks if validation was successful.
     *
     * @return true if valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Gets the validation errors.
     *
     * @return unmodifiable list of error messages
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Gets the number of errors.
     *
     * @return error count
     */
    public int getErrorCount() {
        return errors.size();
    }

    /**
     * Creates a valid result with no errors.
     *
     * @return a valid validation result
     */
    public static ValidationResult valid() {
        return new ValidationResult(true, Collections.emptyList());
    }

    /**
     * Creates an invalid result with a single error.
     *
     * @param error the error message
     * @return an invalid validation result
     */
    public static ValidationResult invalid(String error) {
        return new ValidationResult(false, Collections.singletonList(error));
    }

    /**
     * Creates an invalid result with multiple errors.
     *
     * @param errors the error messages
     * @return an invalid validation result
     */
    public static ValidationResult invalid(List<String> errors) {
        return new ValidationResult(false, errors);
    }

    /**
     * Returns a string representation of this result.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        if (valid) {
            return "ValidationResult{valid=true}";
        }
        return "ValidationResult{valid=false, errors=" + errors + "}";
    }
}
