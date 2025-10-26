package at.videc.dummy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A generic container class demonstrating advanced generic usage.
 * Shows multiple type parameters, bounded wildcards, and generic methods.
 *
 * @param <T> the primary type of elements in this container
 * @param <M> the metadata type associated with elements, must extend Comparable
 * @author Generic Programming Team
 * @version 2.5.0
 * @since 2.0.0
 */
public class GenericContainer<T, M extends Comparable<M>> {

    /**
     * The elements in this container.
     */
    private final List<T> elements;

    /**
     * The metadata for each element.
     */
    private final List<M> metadata;

    /**
     * Constructs an empty container.
     */
    public GenericContainer() {
        this.elements = new ArrayList<>();
        this.metadata = new ArrayList<>();
    }

    /**
     * Adds an element with metadata.
     *
     * @param element the element to add
     * @param meta the associated metadata
     * @throws IllegalArgumentException if element or meta is null
     */
    public void add(T element, M meta) {
        if (element == null || meta == null) {
            throw new IllegalArgumentException("Element and metadata must not be null");
        }
        elements.add(element);
        metadata.add(meta);
    }

    /**
     * Gets an element by index.
     *
     * @param index the index
     * @return Optional containing the element if found
     */
    public Optional<T> get(int index) {
        if (index >= 0 && index < elements.size()) {
            return Optional.of(elements.get(index));
        }
        return Optional.empty();
    }

    /**
     * Gets metadata by index.
     *
     * @param index the index
     * @return Optional containing the metadata if found
     */
    public Optional<M> getMetadata(int index) {
        if (index >= 0 && index < metadata.size()) {
            return Optional.of(metadata.get(index));
        }
        return Optional.empty();
    }

    /**
     * Filters elements by a predicate.
     *
     * @param predicate the filter predicate
     * @return list of matching elements
     */
    public List<T> filter(Predicate<T> predicate) {
        return elements.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Maps elements to another type.
     * Demonstrates generic method with different type parameter.
     *
     * @param <R> the result type
     * @param mapper the mapping function
     * @return container with mapped elements
     */
    public <R> GenericContainer<R, M> map(java.util.function.Function<T, R> mapper) {
        GenericContainer<R, M> result = new GenericContainer<>();
        for (int i = 0; i < elements.size(); i++) {
            result.add(mapper.apply(elements.get(i)), metadata.get(i));
        }
        return result;
    }

    /**
     * Merges this container with another container.
     * Demonstrates bounded wildcard usage.
     *
     * @param other the other container (can contain subtype of T)
     */
    public void merge(GenericContainer<? extends T, ? extends M> other) {
        for (int i = 0; i < other.size(); i++) {
            final int index = i;
            other.get(index).ifPresent(elem ->
                other.getMetadata(index).ifPresent(meta ->
                    this.add(elem, meta)));
        }
    }

    /**
     * Gets the size of this container.
     *
     * @return the number of elements
     */
    public int size() {
        return elements.size();
    }

    /**
     * Checks if the container is empty.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Clears all elements and metadata.
     */
    public void clear() {
        elements.clear();
        metadata.clear();
    }

    /**
     * Creates a container from var-args.
     * Demonstrates safe var-args usage.
     *
     * @param <T> the element type
     * @param <M> the metadata type
     * @param elements the elements to add
     * @return a new container with the elements (using default metadata)
     */
    @SafeVarargs
    public static <T, M extends Comparable<M>> GenericContainer<T, M> of(T... elements) {
        GenericContainer<T, M> container = new GenericContainer<>();
        // Note: This is a simplified example - actual implementation would need metadata
        return container;
    }
}
