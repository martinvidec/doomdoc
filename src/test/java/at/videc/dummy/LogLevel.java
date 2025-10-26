package at.videc.dummy;

/**
 * Enumeration for logging levels.
 * Demonstrates enum implementing multiple interfaces.
 *
 * @author Logging Framework Team
 * @version 3.1.0
 * @since 1.0.0
 */
public enum LogLevel implements Comparable<LogLevel>, Serializable {

    /**
     * Trace level - most detailed logging.
     */
    TRACE(0, "TRACE"),

    /**
     * Debug level - detailed information for debugging.
     */
    DEBUG(1, "DEBUG"),

    /**
     * Info level - general informational messages.
     */
    INFO(2, "INFO"),

    /**
     * Warn level - warning messages.
     */
    WARN(3, "WARN"),

    /**
     * Error level - error messages.
     */
    ERROR(4, "ERROR"),

    /**
     * Fatal level - critical errors.
     */
    FATAL(5, "FATAL");

    private final int severity;
    private final String label;

    /**
     * Constructs a LogLevel.
     *
     * @param severity numeric severity (higher = more severe)
     * @param label string label for display
     */
    LogLevel(int severity, String label) {
        this.severity = severity;
        this.label = label;
    }

    /**
     * Gets the severity level.
     *
     * @return the severity as an integer
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * Gets the display label.
     *
     * @return the label string
     */
    public String getLabel() {
        return label;
    }

    /**
     * Checks if this level includes another level.
     *
     * @param other the other log level
     * @return true if this level's severity is greater than or equal to the other
     */
    public boolean includes(LogLevel other) {
        return this.severity >= other.severity;
    }
}
