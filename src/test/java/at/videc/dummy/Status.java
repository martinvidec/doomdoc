package at.videc.dummy;

/**
 * Enumeration representing various system states.
 * Demonstrates enum with constants, fields, constructors, and methods.
 *
 * @author System Team
 * @version 2.0.0
 * @since 1.0.0
 */
public enum Status {

    /**
     * System is active and running normally.
     */
    ACTIVE("Active", 1, true),

    /**
     * System is inactive but can be activated.
     */
    INACTIVE("Inactive", 0, false),

    /**
     * System is in maintenance mode.
     */
    @Deprecated
    MAINTENANCE("Maintenance", 2, true),

    /**
     * System has encountered an error.
     */
    ERROR("Error", -1, false),

    /**
     * System is shutting down.
     */
    SHUTDOWN("Shutdown", -2, false);

    /**
     * Human-readable display name.
     */
    private final String displayName;

    /**
     * Numeric code for this status.
     */
    private final int code;

    /**
     * Whether this is an operational status.
     */
    private final boolean operational;

    /**
     * Constructs a new Status enum constant.
     *
     * @param displayName the display name
     * @param code the numeric code
     * @param operational whether this status is operational
     */
    Status(String displayName, int code, boolean operational) {
        this.displayName = displayName;
        this.code = code;
        this.operational = operational;
    }

    /**
     * Gets the display name of this status.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the numeric code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Checks if this status is operational.
     *
     * @return true if operational, false otherwise
     */
    public boolean isOperational() {
        return operational;
    }

    /**
     * Checks if this is an error status.
     *
     * @return true if this represents an error
     * @since 1.5.0
     */
    public boolean isError() {
        return this == ERROR;
    }

    /**
     * Converts a code to a Status.
     *
     * @param code the numeric code
     * @return the corresponding Status, or null if not found
     * @throws IllegalArgumentException if code is out of valid range
     */
    public static Status fromCode(int code) {
        for (Status status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }

    /**
     * Gets the default status.
     *
     * @return the default status (INACTIVE)
     * @since 2.0.0
     */
    public static Status getDefault() {
        return INACTIVE;
    }
}
