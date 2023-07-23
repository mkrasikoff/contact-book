package com.mkrasikoff.contactbook.repos;

import com.mkrasikoff.contactbook.models.Person;
import java.util.List;


/**
 * This interface defines the operations for managing the Person entities in a repository.
 */
public interface PersonRepository {

    /**
     * Finds all Person entities in the repository.
     *
     * @return a list of all Person entities
     */
    List<Person> findAll();

    /**
     * Retrieves a specific page of Person entities in the repository sorted by a specified attribute.
     *
     * @param page the number of the page to retrieve
     * @param size the number of entities per page
     * @param sort the attribute to sort by
     * @param reverse if true, sorts in descending order
     * @return a list of Person entities for the specified page and sort order
     */
    List<Person> findSpecificPeoplePage(int page, int size, String sort, boolean reverse);

    /**
     * Counts all Person entities in the repository.
     *
     * @return the number of Person entities
     */
    int count();

    /**
     * Finds a Person entity by its ID.
     *
     * @param id the ID of the Person entity to retrieve
     * @return the Person entity with the specified ID
     */
    Person findById(int id);

    /**
     * Saves a Person entity to the repository.
     *
     * @param person the Person entity to save
     */
    void save(Person person);

    /**
     * Updates a Person entity in the repository.
     *
     * @param person the updated Person entity
     * @param id the ID of the Person entity to update
     */
    void update(Person person, int id);

    /**
     * Deletes a Person entity by its ID.
     *
     * @param id the ID of the Person entity to delete
     */
    void deleteById(int id);

    /**
     * Deletes all Person entities in the repository.
     */
    void deleteAll();

    /**
     * Searches for Person entities by a query.
     *
     * @param query the search query
     * @return a list of Person entities matching the search query
     */
    List<Person> search(String query);
}
