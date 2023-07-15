package com.mkrasikoff.contactbook.repos;

import com.mkrasikoff.contactbook.models.Person;
import com.mkrasikoff.contactbook.services.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.mkrasikoff.contactbook.exceptions.PersonAlreadyExistsException;
import com.mkrasikoff.contactbook.exceptions.PersonNotFoundException;

import java.util.List;

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

    @Override
    public List<Person> findAll() {
        return jdbcTemplate.query(QUERY_SHOW_PEOPLE_ALL, new BeanPropertyRowMapper<>(Person.class));
    }

    @Override
    public List<Person> findSpecificPeoplePage(int page, int size) {
        int start = (page - 1) * size;
        return jdbcTemplate.query(QUERY_SHOW_PEOPLE_LIMIT, new BeanPropertyRowMapper<>(Person.class), start, size);
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject(QUERY_COUNT_PEOPLE, Integer.class);
    }

    @Override
    public Person findById(int id) {
        List<Person> people = jdbcTemplate.query(QUERY_SHOW_PERSON, new BeanPropertyRowMapper<>(Person.class), id);

        return people.stream().findAny().orElseThrow(() -> new PersonNotFoundException("Person with id " + id + " not found."));
    }

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

    @Override
    public void update(Person person, int id) {
        String name = person.getName();
        String surname = person.getSurname();
        String email = person.getEmail();
        int logoId = person.getLogoId();

        int updatedRows = jdbcTemplate.update(QUERY_UPDATE_PERSON, name, surname, email, logoId, id);

        if(updatedRows == 0) throw new PersonNotFoundException("Person with id " + id + " not found.");
    }

    @Override
    public void deleteById(int id) {
        int deletedRows = jdbcTemplate.update(QUERY_DELETE_PERSON, id);

        if(deletedRows == 0) throw new PersonNotFoundException("Person with id " + id + " not found.");
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(QUERY_DELETE_ALL_PEOPLE);
    }

    @Override
    public List<Person> search(String query) {
        String searchQuery = "%" + query + "%";
        return jdbcTemplate.query(QUERY_SEARCH_PERSON, new BeanPropertyRowMapper<>(Person.class), searchQuery);
    }
}
