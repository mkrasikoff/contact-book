package ru.mkrasikoff.springmvcapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mkrasikoff.springmvcapp.models.Person;
import java.util.List;
import java.util.Random;

@Component
public class PersonDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String QUERY_SHOW_PEOPLE = "SELECT * FROM person";
    private String QUERY_SHOW_PERSON = "SELECT * FROM person WHERE id = ?";
    private String QUERY_SAVE_PERSON = "INSERT INTO person VALUES(?, ?, ?, ?)";
    private String QUERY_UPDATE_PERSON = "UPDATE person SET name = ?, surname = ?, email = ? WHERE id = ?";
    private String QUERY_DELETE_PERSON = "DELETE FROM person WHERE id = ?";

    public List<Person> showPeople() {
        return jdbcTemplate.query(QUERY_SHOW_PEOPLE,
                new BeanPropertyRowMapper<>(Person.class));
    }

    public Person showPerson(int id) {
        List<Person> people = jdbcTemplate.query(QUERY_SHOW_PERSON,
                new BeanPropertyRowMapper<>(Person.class),
                new Object[] {id});
        return people.stream().findAny().orElse(null);
    }

    public void savePerson(Person person) {
        Random random = new Random();
        jdbcTemplate.update(QUERY_SAVE_PERSON,
                random.nextInt(1024),
                person.getName(), person.getSurname(),
                person.getEmail());
    }

    public void updatePerson(Person updatedPerson, int id) {
        jdbcTemplate.update(QUERY_UPDATE_PERSON,
                updatedPerson.getName(),
                updatedPerson.getSurname(),
                updatedPerson.getEmail(),
                id);
    }

    public void deletePerson(int id) {
        jdbcTemplate.update(QUERY_DELETE_PERSON,
                id);
    }
}
