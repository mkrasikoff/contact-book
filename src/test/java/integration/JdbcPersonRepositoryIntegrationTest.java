package integration;

import integration.configs.IntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mkrasikoff.springmvcapp.models.Person;
import ru.mkrasikoff.springmvcapp.repos.JdbcPersonRepository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationTestConfig.class)
public class JdbcPersonRepositoryIntegrationTest {

    private static final String QUERY_INSERT_PERSON = "INSERT INTO person(id, name, surname, email) VALUES(?, ?, ?, ?)";
    private static final String QUERY_DELETE_PERSON = "DELETE FROM person";

    @Autowired
    private JdbcPersonRepository personRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearPersonTable() {
        jdbcTemplate.update(QUERY_DELETE_PERSON);
    }

    @Test
    public void givenPersonId_whenFindById_thenReturnCorrectPerson() {
        Person person = createPerson();
        int id = person.getId();
        jdbcTemplate.update(QUERY_INSERT_PERSON,
                person.getId(), person.getName(), person.getSurname(), person.getEmail());

        Person found = personRepository.findById(id);

        assertAll("Person",
                () -> assertEquals(person.getId(), found.getId(), "ID should match"),
                () -> assertEquals(person.getName(), found.getName(), "Name should match"),
                () -> assertEquals(person.getSurname(), found.getSurname(), "Surname should match"),
                () -> assertEquals(person.getEmail(), found.getEmail(), "Email should match")
        );
    }

    private Person createPerson() {
        return new Person(1, "Adam", "Smith", "adam_smith@email.com");
    }
}
