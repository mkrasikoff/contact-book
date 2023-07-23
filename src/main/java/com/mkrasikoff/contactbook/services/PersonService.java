package com.mkrasikoff.contactbook.services;

import com.mkrasikoff.contactbook.models.Person;
import org.springframework.stereotype.Service;
import com.mkrasikoff.contactbook.repos.PersonRepository;
import java.util.List;

/**
 * The PersonService class provides the business logic for the Person entity operations.
 * This class interacts with the repository layer and contains the implementation of methods for operations
 * such as saving, updating, deleting, and fetching data from the database.
 */
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final GenerateService generateService;

    /**
     * Constructs a new PersonService with the given repository and generateService.
     * @param personRepository the repository to be used by this service.
     * @param generateService the service to be used for generating random Person objects.
     */
    public PersonService(PersonRepository personRepository, GenerateService generateService) {
        this.personRepository = personRepository;
        this.generateService = generateService;
    }

    /**
     * Returns a list of all Person objects in the repository.
     * @return a List of Person objects.
     */
    public List<Person> showPeople() {
        return personRepository.findAll();
    }

    /**
     * Returns a page of Person objects, sorted according to the given parameters.
     * @param page the page number.
     * @param size the number of Person objects per page.
     * @param sort the field to sort by.
     * @param reverse whether to reverse the sorting order.
     * @return a List of Person objects.
     */
    public List<Person> showPeoplePage(int page, int size, String sort, boolean reverse) {
        return personRepository.findSpecificPeoplePage(page, size, sort, reverse);
    }

    /**
     * Returns the total count of Person objects in the repository.
     * @return the count of Person objects.
     */
    public int countPeople() {
        return personRepository.count();
    }

    /**
     * Returns the Person object with the given ID.
     * @param id the ID of the Person object to retrieve.
     * @return the Person object with the given ID.
     */
    public Person showPerson(int id) {
        return personRepository.findById(id);
    }

    /**
     * Saves the given Person object to the repository.
     * @param person the Person object to save.
     */
    public void savePerson(Person person) {
        personRepository.save(person);
    }

    /**
     * Updates the Person object with the given ID using the provided Person object.
     * @param person the new Person object to replace the old one.
     * @param id the ID of the Person object to update.
     */
    public void updatePerson(Person person, int id) {
        personRepository.update(person, id);
    }

    /**
     * Deletes the Person object with the given ID from the repository.
     * @param id the ID of the Person object to delete.
     */
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    /**
     * Deletes all Person objects from the repository.
     */
    public void deleteAllPeople() {
        personRepository.deleteAll();
    }

    /**
     * Returns a list of Person objects that match the given search query.
     * @param query the search query.
     * @return a List of matching Person objects.
     */
    public List<Person> search(String query) {
        return personRepository.search(query);
    }

    /**
     * Creates and saves 10 random Person objects to the repository.
     */
    public void createRandomPeople() {
        for (int i = 1; i <= 10; i++) {
            savePerson(generateService.generateRandomPerson());
        }
    }
}
