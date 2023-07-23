package com.mkrasikoff.contactbook.repos;

import com.mkrasikoff.contactbook.models.Person;
import com.mkrasikoff.contactbook.services.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.mkrasikoff.contactbook.exceptions.PersonAlreadyExistsException;
import com.mkrasikoff.contactbook.exceptions.PersonNotFoundException;
import com.mkrasikoff.contactbook.exceptions.InvalidSortParameterException;
import java.util.List;

/**
 * This repository provides methods to interact with the 'person' table in the database.
 */
@Repository
public class JdbcPersonRepository implements PersonRepository {

    private static final String QUERY_SHOW_PEOPLE_ALL = "SELECT * FROM person";
    private static final String QUERY_SHOW_PEOPLE_LIMIT = "SELECT * FROM person ORDER BY id LIMIT ?, ?";
    private static final String QUERY_COUNT_PEOPLE = "SELECT COUNT(*) FROM person";
    private static final String QUERY_SHOW_PERSON = "SELECT * FROM person WHERE id = ?";
    private static final String QUERY_SAVE_PERSON = "INSERT INTO person(name, surname, email, logoId) VALUES(?, ?, ?, ?)";
    private static final String QUERY_UPDATE_PERSON = "UPDATE person SET name = ?, surname = ?, email = ?, logoId = ? WHERE id = ?";
    private static final String QUERY_DELETE_PERSON = "DELETE FROM person WHERE id = ?";
    private static final String QUERY_DELETE_ALL_PEOPLE = "DELETE FROM person";
    private static final String QUERY_SEARCH_PERSON = "SELECT * FROM person WHERE CONCAT(name, ' ', surname) LIKE ?";
    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS person " +
            "(id INT PRIMARY KEY AUTO_INCREMENT, " +
            "name VARCHAR(30), " +
            "surname VARCHAR(30), " +
            "email VARCHAR(50)," +
            "logoId INT)";

    private JdbcTemplate jdbcTemplate;
    private GenerateService generateService;

    /**
     * Constructs the JdbcPersonRepository.
     * If the 'person' table does not exist in the database, it is created.
     * If the 'person' table is empty, 10 random Person objects are inserted.
     *
     * @param jdbcTemplate the JdbcTemplate to interact with the database
     * @param generateService the GenerateService to create random Person objects
     */
    @Autowired
    public JdbcPersonRepository(JdbcTemplate jdbcTemplate, GenerateService generateService) {
        this.jdbcTemplate = jdbcTemplate;
        this.generateService = generateService;

        jdbcTemplate.execute(QUERY_CREATE_TABLE);

        if (findAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                save(generateService.generateRandomPerson());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> findAll() {
        return jdbcTemplate.query(QUERY_SHOW_PEOPLE_ALL, new BeanPropertyRowMapper<>(Person.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> findSpecificPeoplePage(int page, int size, String sort, boolean reverse) {
        String preparedSort = prepareSortParameter(sort);
        String order = reverse ? " DESC" : " ASC";
        String searchQuery = QUERY_SHOW_PEOPLE_LIMIT.replace("id", preparedSort + order);

        int start = (page - 1) * size;
        return jdbcTemplate.query(searchQuery, new BeanPropertyRowMapper<>(Person.class), start, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int count() {
        return jdbcTemplate.queryForObject(QUERY_COUNT_PEOPLE, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person findById(int id) {
        List<Person> people = jdbcTemplate.query(QUERY_SHOW_PERSON, new BeanPropertyRowMapper<>(Person.class), id);

        return people.stream().findAny().orElseThrow(() -> new PersonNotFoundException("Person with id " + id + " not found."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Person person) {
        int id = person.getId();
        String name = person.getName();
        String surname = person.getSurname();
        String email = person.getEmail();
        int logoId = person.getLogoId();

        try {
            findById(id);
            throw new PersonAlreadyExistsException("Person with id " + id + " already exists.");
        } catch (PersonNotFoundException exc) {
            jdbcTemplate.update(QUERY_SAVE_PERSON, name, surname, email, logoId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Person person, int id) {
        String name = person.getName();
        String surname = person.getSurname();
        String email = person.getEmail();
        int logoId = person.getLogoId();

        int updatedRows = jdbcTemplate.update(QUERY_UPDATE_PERSON, name, surname, email, logoId, id);

        if(updatedRows == 0) throw new PersonNotFoundException("Person with id " + id + " not found.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(int id) {
        int deletedRows = jdbcTemplate.update(QUERY_DELETE_PERSON, id);

        if(deletedRows == 0) throw new PersonNotFoundException("Person with id " + id + " not found.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {
        jdbcTemplate.update(QUERY_DELETE_ALL_PEOPLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> search(String query) {
        String searchQuery = "%" + query + "%";
        return jdbcTemplate.query(QUERY_SEARCH_PERSON, new BeanPropertyRowMapper<>(Person.class), searchQuery);
    }

    /**
     * Prepares the sort parameter for the findSpecificPeoplePage method.
     * The sort parameter should be 'id', 'name', 'surname', or 'logoId'.
     * If the sort parameter is not one of these values, an InvalidSortParameterException is thrown.
     *
     * @param sort the sort parameter
     * @return the prepared sort parameter
     * @throws InvalidSortParameterException if the sort parameter is invalid
     */
    private String prepareSortParameter(String sort) {
        if (sort.equals("id") || sort.equals("name") || sort.equals("surname") || sort.equals("logoId")) {
            return sort;
        } else {
            throw new InvalidSortParameterException("Invalid sort parameter: " + sort);
        }
    }
}
