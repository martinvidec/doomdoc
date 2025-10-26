package at.videc.dummy;

/**
 * Abstract base class for all entities in the system.
 * Demonstrates abstract class with generic type parameter.
 *
 * @param <ID> the type of the entity identifier
 * @author Domain Model Team
 * @version 2.5.0
 * @since 1.0.0
 * @see Repository
 */
public abstract class Entity<ID extends Comparable<ID>> implements Serializable {

    /**
     * Default timeout for entity operations in milliseconds.
     */
    public static final long DEFAULT_TIMEOUT = 30000L;

    /**
     * Maximum retries for failed operations.
     */
    protected static final int MAX_RETRIES = 3;

    /**
     * The unique identifier for this entity.
     */
    private ID id;

    /**
     * The version number for optimistic locking.
     */
    private long version;

    /**
     * Timestamp when this entity was created.
     */
    private final long createdAt;

    /**
     * Timestamp when this entity was last modified.
     */
    private long modifiedAt;

    /**
     * Default constructor.
     * Initializes creation timestamp.
     */
    protected Entity() {
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = this.createdAt;
    }

    /**
     * Constructor with identifier.
     *
     * @param id the entity identifier
     * @throws IllegalArgumentException if id is null
     */
    protected Entity(ID id) {
        this();
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        this.id = id;
    }

    /**
     * Gets the entity identifier.
     *
     * @return the identifier, may be null for new entities
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets the entity identifier.
     *
     * @param id the new identifier
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Gets the version number.
     *
     * @return the version
     */
    public long getVersion() {
        return version;
    }

    /**
     * Sets the version number.
     *
     * @param version the new version
     */
    protected void setVersion(long version) {
        this.version = version;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return timestamp in milliseconds
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the last modification timestamp.
     *
     * @return timestamp in milliseconds
     */
    public long getModifiedAt() {
        return modifiedAt;
    }

    /**
     * Updates the modification timestamp to current time.
     */
    protected void touch() {
        this.modifiedAt = System.currentTimeMillis();
    }

    /**
     * Checks if this entity is new (not yet persisted).
     *
     * @return true if the entity has no ID
     */
    public boolean isNew() {
        return id == null;
    }

    /**
     * Validates the entity state.
     *
     * @throws ValidationException if validation fails
     * @see Validator
     */
    public abstract void validate() throws ValidationException;

    /**
     * Creates a shallow copy of this entity.
     *
     * @return a clone of this entity
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
