package integration

import integration.configs.IntegrationTestConfig
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import ru.mkrasikoff.springmvcapp.exceptions.PersonNotFoundException
import ru.mkrasikoff.springmvcapp.models.Person
import ru.mkrasikoff.springmvcapp.repos.JdbcPersonRepository

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [IntegrationTestConfig::class])
class JdbcPersonRepositoryIntegrationTest {

    companion object {
        private const val QUERY_INSERT_PERSON = "INSERT INTO person(id, name, surname, email) VALUES(?, ?, ?, ?)"
        private const val QUERY_DELETE_PERSON = "DELETE FROM person"
        private const val MESSAGE_PERSON_NOT_FOUND = "Person with id 999 not found."
        private const val ID_NONEXISTENT_USER = 999
    }

    @Autowired
    lateinit var personRepository: JdbcPersonRepository

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    lateinit var person: Person

    @BeforeEach
    fun setup() {
        jdbcTemplate.update(QUERY_DELETE_PERSON)
        person = createPerson()
        jdbcTemplate.update(QUERY_INSERT_PERSON, person.id, person.name, person.surname, person.email)
    }

    @Test
    fun findById_givenCorrectPersonId_personReturned() {
        val id = person.id
        val (id1, name, surname, email) = personRepository.findById(id)

        assertAll("Person",
            Executable { assertEquals(id, id1, "ID should match") },
            Executable { assertEquals(person.name, name, "Name should match") },
            Executable { assertEquals(person.surname, surname, "Surname should match") },
            Executable { assertEquals(person.email, email, "Email should match") }
        )
    }

    @Test
    fun findById_givenIncorrectPersonId_thrownException() {
        val exception = assertThrows(PersonNotFoundException::class.java) {
            personRepository.findById(ID_NONEXISTENT_USER)
        }

        assertEquals(MESSAGE_PERSON_NOT_FOUND, exception.message)
    }

    @Test
    fun findAll_twoPersonsCreated_returnsAllPersons() {
        val secondPerson = Person(2, "Eva", "Smith", "eva_smith@email.com")
        jdbcTemplate.update(QUERY_INSERT_PERSON, secondPerson.id, secondPerson.name, secondPerson.surname, secondPerson.email)

        val persons = personRepository.findAll()

        assertEquals(2, persons.size)
        assertTrue(persons.contains(person))
        assertTrue(persons.contains(secondPerson))
    }

    @Test
    fun save_givenValidPerson_returnsSavedPerson() {
        val newPerson = Person(2, "Eva", "Smith", "eva_smith@email.com")

        personRepository.save(newPerson)

        val persons = personRepository.findAll()
        val savedPerson = persons.first {
            it.name == newPerson.name && it.surname == newPerson.surname && it.email == newPerson.email
        }
        assertEquals(newPerson.name, savedPerson.name, "Name should match")
        assertEquals(newPerson.surname, savedPerson.surname, "Surname should match")
        assertEquals(newPerson.email, savedPerson.email, "Email should match")
    }

    @Test
    fun update_givenExistingPerson_personUpdated() {
        person.name = "Updated name"

        personRepository.update(person, person.id)

        val (_, name) = personRepository.findById(person.id)
        assertEquals("Updated name", name)
    }

    @Test
    fun update_givenNonExistingPerson_throwsException() {
        val nonExistingPerson = Person(ID_NONEXISTENT_USER, "Non", "Existing", "non_existing@email.com")

        val exception = assertThrows(PersonNotFoundException::class.java) {
            personRepository.update(nonExistingPerson, nonExistingPerson.id)
        }

        assertEquals(exception.message, MESSAGE_PERSON_NOT_FOUND)
    }

    @Test
    fun delete_givenExistingPerson_personDeleted() {
        assertNotNull(personRepository.findById(person.id))

        personRepository.deleteById(person.id)

        assertThrows(PersonNotFoundException::class.java) {
            personRepository.findById(person.id)
        }
    }

    @Test
    fun delete_givenNonExistingPerson_throwsException() {
        val exception = assertThrows(PersonNotFoundException::class.java) {
            personRepository.deleteById(ID_NONEXISTENT_USER)
        }

        assertEquals(exception.message, MESSAGE_PERSON_NOT_FOUND)
    }

    private fun createPerson(): Person {
        return Person(1, "Adam", "Smith", "adam_smith@email.com")
    }
}
