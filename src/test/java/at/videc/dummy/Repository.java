package at.videc.dummy;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for data access operations.
 * Demonstrates multiple type parameters with bounds.
 *
 * @param <T> the entity type, must extend Entity
 * @param <ID> the identifier type, must extend Comparable
 * @author Repository Team
 * @version 2.1.0
 * @since 1.5.0
 */
public interface Repository<T extends Entity, ID extends Comparable<ID>> {

    /**
     * Finds an entity by its identifier.
     *
     * @param id the entity identifier
     * @return an Optional containing the entity if found
     * @throws IllegalArgumentException if id is null
     */
    Optional<T> findById(ID id);

    /**
     * Saves an entity.
     *
     * @param entity the entity to save
     * @return the saved entity
     * @throws IllegalArgumentException if entity is null
     */
    T save(T entity);

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the entity identifier
     * @return true if the entity was deleted, false otherwise
     */
    boolean deleteById(ID id);

    /**
     * Finds all entities.
     *
     * @return list of all entities
     */
    List<T> findAll();

    /**
     * Counts the total number of entities.
     *
     * @return the count
     */
    long count();
}
