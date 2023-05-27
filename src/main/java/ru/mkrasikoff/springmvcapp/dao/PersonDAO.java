package ru.mkrasikoff.springmvcapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.mkrasikoff.springmvcapp.models.Person;
import java.util.List;

@Component
public class PersonDAO {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static final String QUERY_SHOW_PEOPLE = "SELECT * FROM person";
  private static final String QUERY_SHOW_PERSON = "SELECT * FROM person WHERE id = ?";
  private static final String QUERY_SAVE_PERSON = "INSERT INTO person(name, surname, email) VALUES(?, ?, ?)";
  private static final String QUERY_UPDATE_PERSON = "UPDATE person SET name = ?, surname = ?, email = ? WHERE id = ?";
  private static final String QUERY_DELETE_PERSON = "DELETE FROM person WHERE id = ?";

  public List<Person> showPeople() {
    return jdbcTemplate.query(QUERY_SHOW_PEOPLE, new BeanPropertyRowMapper<>(Person.class));
  }

  public Person showPerson(int id) {
    List<Person> people = jdbcTemplate.query(QUERY_SHOW_PERSON, new BeanPropertyRowMapper<>(Person.class), id);

    return people.stream().findAny().orElse(null);
  }

  public void savePerson(Person person) {
    String name = person.getName();
    String surname = person.getSurname();
    String email = person.getEmail();

    jdbcTemplate.update(QUERY_SAVE_PERSON, name, surname, email);
  }

  public void updatePerson(Person updatedPerson, int id) {
    String name = updatedPerson.getName();
    String surname = updatedPerson.getSurname();
    String email = updatedPerson.getEmail();

    jdbcTemplate.update(QUERY_UPDATE_PERSON, name, surname, email, id);
  }

  public void deletePerson(int id) {
    jdbcTemplate.update(QUERY_DELETE_PERSON, id);
  }
}
