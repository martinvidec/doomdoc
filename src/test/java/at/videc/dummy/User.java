package at.videc.dummy;

import at.videc.BOMB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user in the system.
 * Demonstrates comprehensive class with all features: fields, constructors, methods,
 * annotations, generics, inheritance, and extensive JavaDoc.
 *
 * <p>A user can have multiple roles and belongs to groups. Users are validated
 * according to business rules and can be persisted to a repository.
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * User user = new User("john.doe", "john@example.com");
 * user.setFirstName("John");
 * user.setLastName("Doe");
 * user.addRole("ADMIN");
 * user.validate();
 * }</pre>
 *
 * @author User Management Team
 * @version 3.2.1
 * @since 1.0.0
 * @see Entity
 * @see Repository
 */
@BOMB("UserEntity")
@ValidationLevel(value = "STRICT", strict = true, priority = 10)
public class User extends Entity<Long> {

    /**
     * Minimum username length.
     */
    public static final int MIN_USERNAME_LENGTH = 3;

    /**
     * Maximum username length.
     */
    public static final int MAX_USERNAME_LENGTH = 50;

    /**
     * Pattern for valid email addresses.
     */
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    /**
     * Counter for total users created.
     */
    private static int userCount = 0;

    /**
     * The username (unique identifier).
     */
    @Required
    @ValidationLevel(strict = true)
    private String username;

    /**
     * The user's email address.
     */
    @Required
    private String email;

    /**
     * The user's first name.
     */
    private String firstName;

    /**
     * The user's last name.
     */
    private String lastName;

    /**
     * Whether this user account is active.
     */
    private boolean active;

    /**
     * The user's current status.
     */
    private Status status;

    /**
     * List of roles assigned to this user.
     */
    private final List<String> roles;

    /**
     * User's age (optional).
     */
    private Integer age;

    /**
     * Default constructor.
     * Creates an inactive user with INACTIVE status.
     */
    public User() {
        super();
        this.active = false;
        this.status = Status.INACTIVE;
        this.roles = new ArrayList<>();
        userCount++;
    }

    /**
     * Constructs a user with username and email.
     *
     * @param username the username (must be between 3 and 50 characters)
     * @param email the email address (must be valid format)
     * @throws IllegalArgumentException if username or email is invalid
     * @see #validateUsername(String)
     * @see #validateEmail(String)
     */
    public User(String username, String email) {
        this();
        validateUsername(username);
        validateEmail(email);
        this.username = username;
        this.email = email;
    }

    /**
     * Constructs a user with all basic properties.
     *
     * @param id the user ID
     * @param username the username
     * @param email the email address
     * @param firstName the first name
     * @param lastName the last name
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public User(Long id, String username, String email, String firstName, String lastName) {
        this(username, email);
        setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     * @throws IllegalArgumentException if username is invalid
     */
    public void setUsername(String username) {
        validateUsername(username);
        this.username = username;
        touch();
    }

    /**
     * Gets the email address.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the new email
     * @throws IllegalArgumentException if email is invalid
     */
    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
        touch();
    }

    /**
     * Gets the first name.
     *
     * @return the first name, may be null
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        touch();
    }

    /**
     * Gets the last name.
     *
     * @return the last name, may be null
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
        touch();
    }

    /**
     * Gets the full name (first name + last name).
     *
     * @return the full name, or username if names are not set
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }

    /**
     * Checks if the user is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status.
     *
     * @param active the new active status
     */
    public void setActive(boolean active) {
        this.active = active;
        this.status = active ? Status.ACTIVE : Status.INACTIVE;
        touch();
    }

    /**
     * Gets the user's status.
     *
     * @return the current status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the user's status.
     *
     * @param status the new status
     * @throws IllegalArgumentException if status is null
     */
    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        this.status = status;
        this.active = status.isOperational();
        touch();
    }

    /**
     * Gets the user's age.
     *
     * @return the age, or null if not set
     * @since 2.0.0
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets the user's age.
     *
     * @param age the age (must be positive)
     * @throws IllegalArgumentException if age is negative
     * @since 2.0.0
     */
    public void setAge(Integer age) {
        if (age != null && age < 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
        this.age = age;
        touch();
    }

    /**
     * Gets an unmodifiable view of the user's roles.
     *
     * @return list of roles
     */
    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    /**
     * Adds a role to this user.
     *
     * @param role the role to add
     * @throws IllegalArgumentException if role is null or empty
     */
    public void addRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role must not be empty");
        }
        if (!roles.contains(role)) {
            roles.add(role);
            touch();
        }
    }

    /**
     * Removes a role from this user.
     *
     * @param role the role to remove
     * @return true if the role was removed
     */
    public boolean removeRole(String role) {
        boolean removed = roles.remove(role);
        if (removed) {
            touch();
        }
        return removed;
    }

    /**
     * Checks if user has a specific role.
     *
     * @param role the role to check
     * @return true if user has the role
     */
    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    /**
     * Validates the user entity.
     *
     * @throws ValidationException if validation fails
     */
    @Override
    public void validate() throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        validateUsername(username);
        validateEmail(email);
    }

    /**
     * Validates a username.
     *
     * @param username the username to validate
     * @throws IllegalArgumentException if username is invalid
     */
    private static void validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username must not be null");
        }
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username too short (min " + MIN_USERNAME_LENGTH + " chars)");
        }
        if (username.length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username too long (max " + MAX_USERNAME_LENGTH + " chars)");
        }
    }

    /**
     * Validates an email address.
     *
     * @param email the email to validate
     * @throws IllegalArgumentException if email is invalid
     */
    private static void validateEmail(String email) {
        if (email == null || !email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * Gets the total number of users created.
     *
     * @return the user count
     */
    public static int getUserCount() {
        return userCount;
    }

    /**
     * Resets the user counter.
     * For testing purposes only.
     *
     * @deprecated Use proper test fixtures instead
     */
    @Deprecated
    public static void resetUserCount() {
        userCount = 0;
    }

    /**
     * Compares this user with another object for equality.
     *
     * @param o the object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
               Objects.equals(username, user.username);
    }

    /**
     * Generates a hash code for this user.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), username);
    }

    /**
     * Returns a string representation of this user.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "User{" +
               "id=" + getId() +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", active=" + active +
               ", status=" + status +
               '}';
    }
}
