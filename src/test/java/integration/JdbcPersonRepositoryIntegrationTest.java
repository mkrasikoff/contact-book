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
import ru.mkrasikoff.springmvcapp.exceptions.PersonNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationTestConfig.class)
public class JdbcPersonRepositoryIntegrationTest {

    private static final String QUERY_INSERT_PERSON = "INSERT INTO person(id, name, surname, email) VALUES(?, ?, ?, ?)";
    private static final String QUERY_DELETE_PERSON = "DELETE FROM person";
    private static final String MESSAGE_PERSON_NOT_FOUND = "Person with id 999 not found.";
    private static final int ID_NONEXISTENT_USER = 999;

    private Person person;

    @Autowired
    private JdbcPersonRepository personRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update(QUERY_DELETE_PERSON);
        person = createPerson();
        jdbcTemplate.update(QUERY_INSERT_PERSON, person.getId(), person.getName(), person.getSurname(), person.getEmail());
    }

    @Test
    public void findById_givenCorrectPersonId_personReturned() {
        int id = person.getId();

        Person found = personRepository.findById(id);

        assertAll("Person",
                () -> assertEquals(id, found.getId(), "ID should match"),
                () -> assertEquals(person.getName(), found.getName(), "Name should match"),
                () -> assertEquals(person.getSurname(), found.getSurname(), "Surname should match"),
                () -> assertEquals(person.getEmail(), found.getEmail(), "Email should match")
        );
    }

    @Test
    public void findById_givenIncorrectPersonId_thrownException() {
        PersonNotFoundException exception = assertThrows(PersonNotFoundException.class, () -> personRepository.findById(ID_NONEXISTENT_USER));

        assertEquals(MESSAGE_PERSON_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void findAll_twoPersonsCreated_returnsAllPersons() {
        Person secondPerson = new Person(2, "Eva", "Smith", "eva_smith@email.com");
        jdbcTemplate.update(QUERY_INSERT_PERSON, secondPerson.getId(), secondPerson.getName(), secondPerson.getSurname(), secondPerson.getEmail());

        List<Person> persons = personRepository.findAll();

        assertEquals(2, persons.size());
        assertTrue(persons.contains(person));
        assertTrue(persons.contains(secondPerson));
    }

    @Test
    public void save_givenValidPerson_returnsSavedPerson() {
        Person newPerson = new Person(2, "Eva", "Smith", "eva_smith@email.com");

        personRepository.save(newPerson);
        List<Person> persons = personRepository.findAll();
        Person savedPerson = persons.stream()
                .filter(p -> p.getName().equals(newPerson.getName())
                        && p.getSurname().equals(newPerson.getSurname())
                        && p.getEmail().equals(newPerson.getEmail()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Saved person not found"));

        assertEquals(newPerson.getName(), savedPerson.getName(), "Names should match");
        assertEquals(newPerson.getSurname(), savedPerson.getSurname(), "Surnames should match");
        assertEquals(newPerson.getEmail(), savedPerson.getEmail(), "Emails should match");
    }

    @Test
    public void update_givenExistingPerson_personUpdated() {
        person.setName("Updated name");
        personRepository.update(person, person.getId());
        Person updatedPerson = personRepository.findById(person.getId());

        assertEquals("Updated name", updatedPerson.getName());
    }

    @Test
    public void update_givenNonExistingPerson_throwsException() {
        Person nonExistingPerson = new Person(ID_NONEXISTENT_USER, "Non", "Existing", "non_existing@email.com");

        PersonNotFoundException exception = assertThrows(PersonNotFoundException.class, () -> personRepository.update(nonExistingPerson, nonExistingPerson.getId()));

        assertEquals(exception.getMessage(), MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void delete_givenExistingPerson_personDeleted() {
        assertNotNull(personRepository.findById(person.getId()));

        personRepository.deleteById(person.getId());

        assertThrows(PersonNotFoundException.class, () -> personRepository.findById(person.getId()));
    }

    @Test
    public void delete_givenNonExistingPerson_throwsException() {
        PersonNotFoundException exception = assertThrows(PersonNotFoundException.class, () -> personRepository.deleteById(ID_NONEXISTENT_USER));

        assertEquals(exception.getMessage(), MESSAGE_PERSON_NOT_FOUND);
    }

    private Person createPerson() {
        return new Person(1, "Adam", "Smith", "adam_smith@email.com");
    }
}
