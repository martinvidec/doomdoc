package at.videc.dummy;

/**
 * Enumeration for task priorities.
 * Simple enum with interface implementation.
 *
 * @author Task Management Team
 * @version 1.0.0
 * @since 1.0.0
 */
public enum Priority implements Comparable<Priority> {

    /**
     * Low priority task.
     */
    LOW,

    /**
     * Normal priority task.
     */
    NORMAL,

    /**
     * High priority task.
     */
    HIGH,

    /**
     * Critical priority task requiring immediate attention.
     */
    CRITICAL;

    /**
     * Checks if this priority is urgent.
     *
     * @return true if priority is HIGH or CRITICAL
     */
    public boolean isUrgent() {
        return this == HIGH || this == CRITICAL;
    }
}
