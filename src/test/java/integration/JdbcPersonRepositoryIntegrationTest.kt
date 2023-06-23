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
        private const val QUERY_INSERT_PERSON = "INSERT INTO person(id, name, surname, email, logoId) VALUES(?, ?, ?, ?, ?)"
        private const val QUERY_DELETE_PEOPLE = "DELETE FROM person"
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
        jdbcTemplate.update(QUERY_DELETE_PEOPLE)
        person = createPerson()
        jdbcTemplate.update(QUERY_INSERT_PERSON, person.id, person.name, person.surname, person.email, person.logoId)
    }

    @Test
    fun findById_givenCorrectPersonId_personReturned() {
        val id = person.id
        val (id1, name, surname, email, logoId) = personRepository.findById(id)

        assertAll("Person",
            Executable { assertEquals(id, id1, "ID should match") },
            Executable { assertEquals(person.name, name, "Name should match") },
            Executable { assertEquals(person.surname, surname, "Surname should match") },
            Executable { assertEquals(person.email, email, "Email should match") },
            Executable { assertEquals(person.logoId, logoId, "LogoId should match") }
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
        val secondPerson = Person(2, "Eva", "Smith", "eva_smith@email.com", 2)
        jdbcTemplate.update(QUERY_INSERT_PERSON, secondPerson.id, secondPerson.name, secondPerson.surname, secondPerson.email, secondPerson.logoId)

        val persons = personRepository.findAll()

        assertEquals(2, persons.size)
        assertTrue(persons.contains(person))
        assertTrue(persons.contains(secondPerson))
    }

    @Test
    fun findSpecificPeoplePage_databaseWithMoreThanTenUsersGiven_returnsOnlyPartOfThem() {
        jdbcTemplate.update(QUERY_DELETE_PEOPLE)

        for (i in 1..12) {
            val person = Person(id = i, name = "Person$i", surname = "Surname$i", email = "person$i@email.com", logoId = i)
            jdbcTemplate.update(QUERY_INSERT_PERSON, person.id, person.name, person.surname, person.email, person.logoId)
        }

        val page1 = personRepository.findSpecificPeoplePage(1, 10)
        val page2 = personRepository.findSpecificPeoplePage(2, 10)

        assertAll("Pagination",
            Executable { assertEquals(10, page1.size, "Page 1 should have 10 people") },
            Executable { assertEquals(2, page2.size, "Page 2 should have 2 people") },
            Executable { assertEquals("Person11", page2[0].name, "First person on Page 2 should be 'Person11'") },
            Executable { assertEquals("Person12", page2[1].name, "Second person on Page 2 should be 'Person12'") }
        )
    }

    @Test
    fun count_whenPeopleInDatabase_returnsCorrectCount() {
        val personCount = personRepository.count()

        assertEquals(1, personCount, "Should return the correct count of people in database")
    }

    @Test
    fun save_givenValidPerson_returnsSavedPerson() {
        val newPerson = Person(2, "Eva", "Smith", "eva_smith@email.com", 2)

        personRepository.save(newPerson)

        val persons = personRepository.findAll()
        val savedPerson = persons.first {
            it.name == newPerson.name && it.surname == newPerson.surname && it.email == newPerson.email && it.logoId == newPerson.logoId
        }
        assertEquals(newPerson.name, savedPerson.name, "Name should match")
        assertEquals(newPerson.surname, savedPerson.surname, "Surname should match")
        assertEquals(newPerson.email, savedPerson.email, "Email should match")
        assertEquals(newPerson.logoId, savedPerson.logoId, "LogoId should match")
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
        val nonExistingPerson = Person(ID_NONEXISTENT_USER, "Non", "Existing", "non_existing@email.com", 1)

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

    @Test
    fun search_givenPartOfName_returnsCorrectPerson() {
        val name = "Eva"
        val person = Person(2, name, "Smith", "eva_smith@email.com", 2)
        jdbcTemplate.update(QUERY_INSERT_PERSON, person.id, person.name, person.surname, person.email, person.logoId)

        val persons = personRepository.search(name)

        assertEquals(1, persons.size)
        assertTrue(persons.contains(person))
    }

    @Test
    fun search_givenFullName_returnsCorrectPerson() {
        val name = "Eva"
        val surname = "Smith"
        val person = Person(2, name, surname, "eva_smith@email.com", 2)
        jdbcTemplate.update(QUERY_INSERT_PERSON, person.id, person.name, person.surname, person.email, person.logoId)
        val searchQuery = "$name $surname"

        val persons = personRepository.search(searchQuery)

        assertEquals(1, persons.size)
        assertTrue(persons.contains(person))
    }

    @Test
    fun search_givenPartOfSurname_returnsCorrectPerson() {
        val name = "Eva"
        val surname = "Smith"
        val person = Person(2, name, surname, "eva_smith@email.com", 2)
        jdbcTemplate.update(QUERY_INSERT_PERSON, person.id, person.name, person.surname, person.email, person.logoId)
        val searchQuery = "$name Smi"

        val persons = personRepository.search(searchQuery)

        assertEquals(1, persons.size)
        assertTrue(persons.contains(person))
    }

    @Test
    fun search_givenNonexistentName_returnsEmptyList() {
        val searchQuery = "NonExistent"

        val persons = personRepository.search(searchQuery)

        assertTrue(persons.isEmpty())
    }

    @Test
    fun deleteAll_givenDatabaseWithPeople_peopleDeleted() {
        val newPerson = Person(2, "Eva", "Smith", "eva_smith@email.com", 2)
        personRepository.save(newPerson)

        personRepository.deleteAll()

        assertTrue(personRepository.findAll().isEmpty())
    }

    @Test
    fun deleteAll_methodCalledTwoTimes_databaseIsEmptyWithoutException() {
        val newPerson = Person(2, "Eva", "Smith", "eva_smith@email.com", 2)
        personRepository.save(newPerson)

        personRepository.deleteAll()
        personRepository.deleteAll()

        assertTrue(personRepository.findAll().isEmpty())
    }

    private fun createPerson(): Person {
        return Person(1, "Adam", "Smith", "adam_smith@email.com", 1)
    }
}
