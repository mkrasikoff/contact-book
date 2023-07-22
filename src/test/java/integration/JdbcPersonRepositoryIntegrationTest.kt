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
import com.mkrasikoff.contactbook.exceptions.PersonNotFoundException
import com.mkrasikoff.contactbook.exceptions.InvalidSortParameterException
import com.mkrasikoff.contactbook.models.Person
import com.mkrasikoff.contactbook.repos.JdbcPersonRepository

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
        person = createPersonAdam()
        insertPerson(person)
    }

    @Test
    fun findById_givenCorrectPersonId_personReturned() {
        val id = person.id
        val foundPerson = personRepository.findById(id)

        assertPerson(person, foundPerson)
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
        val secondPerson = createPersonEva()
        insertPerson(secondPerson)

        val persons = personRepository.findAll()

        assertEquals(2, persons.size)
        assertTrue(persons.contains(person))
        assertTrue(persons.contains(secondPerson))
    }

    @Test
    fun findSpecificPeoplePage_databaseWithMoreThanTenUsersGiven_returnsOnlyPartOfThem() {
        jdbcTemplate.update(QUERY_DELETE_PEOPLE)
        val insertedPeople = (1..12).map {
            Person(id = it, name = "Person$it", surname = "Surname$it", email = "person$it@email.com", logoId = it).apply { insertPerson(this) }
        }

        val page1 = personRepository.findSpecificPeoplePage(1, 10, "id", false)
        val page2 = personRepository.findSpecificPeoplePage(2, 10, "id", false)

        assertAll("Pagination",
            Executable { assertEquals(10, page1.size, "Page 1 should have 10 people") },
            Executable { assertEquals(2, page2.size, "Page 2 should have 2 people") },
            Executable {
                val allReturnedPeople = page1 + page2
                assertTrue(insertedPeople.all { person -> allReturnedPeople.any { it.id == person.id } }, "All inserted people should be returned across the two pages")
            }
        )
    }

    @Test
    fun findSpecificPeoplePage_sortByDifferentFields_returnsCorrectlySortedResults() {
        jdbcTemplate.update(QUERY_DELETE_PEOPLE)
        val insertedPeople = (1..12).map {
            Person(id = it, name = "Person$it", surname = "Surname$it", email = "person$it@email.com", logoId = it).apply { insertPerson(this) }
        }

        val pageSortedById = personRepository.findSpecificPeoplePage(1, 12, "id", false)
        val pageSortedByName = personRepository.findSpecificPeoplePage(1, 12, "name", false)
        val pageSortedBySurname = personRepository.findSpecificPeoplePage(1, 12, "surname", false)
        val pageSortedByLogoId = personRepository.findSpecificPeoplePage(1, 12, "logoId", false)

        assertAll("Sorting",
            Executable { assertEquals(insertedPeople.sortedBy { it.id }, pageSortedById, "Page should be sorted by id") },
            Executable { assertEquals(insertedPeople.sortedBy { it.name }, pageSortedByName, "Page should be sorted by name") },
            Executable { assertEquals(insertedPeople.sortedBy { it.surname }, pageSortedBySurname, "Page should be sorted by surname") },
            Executable { assertEquals(insertedPeople.sortedBy { it.logoId }, pageSortedByLogoId, "Page should be sorted by logoId") }
        )
    }

    @Test
    fun findSpecificPeoplePage_givenInvalidSortParameter_throwsInvalidSortParameterException() {
        val exception = assertThrows(InvalidSortParameterException::class.java) {
            personRepository.findSpecificPeoplePage(1, 10, "invalidSortParameter", false)
        }

        assertEquals("Invalid sort parameter: invalidSortParameter", exception.message)
    }

    @Test
    fun findSpecificPeoplePage_backOrdering_returnsCorrectBackOrderedResults() {
        jdbcTemplate.update(QUERY_DELETE_PEOPLE)
        val insertedPeople = (1..12).map {
            Person(id = it, name = "Person$it", surname = "Surname$it", email = "person$it@email.com", logoId = it).apply { insertPerson(this) }
        }

        val pageSortedById = personRepository.findSpecificPeoplePage(1, 12, "id", true)
        val pageSortedByName = personRepository.findSpecificPeoplePage(1, 12, "name", true)
        val pageSortedBySurname = personRepository.findSpecificPeoplePage(1, 12, "surname", true)
        val pageSortedByLogoId = personRepository.findSpecificPeoplePage(1, 12, "logoId", true)

        assertAll("Sorting",
            Executable { assertEquals(insertedPeople.sortedByDescending { it.id }, pageSortedById, "Page should be back ordered by id") },
            Executable { assertEquals(insertedPeople.sortedByDescending { it.name }, pageSortedByName, "Page should be back ordered by name") },
            Executable { assertEquals(insertedPeople.sortedByDescending { it.surname }, pageSortedBySurname, "Page should be back ordered by surname") },
            Executable { assertEquals(insertedPeople.sortedByDescending { it.logoId }, pageSortedByLogoId, "Page should be back ordered by logoId") }
        )
    }

    @Test
    fun findSpecificPeoplePage_backOrderingEmptyDb_returnsEmptyList() {
        jdbcTemplate.update(QUERY_DELETE_PEOPLE)

        val page = personRepository.findSpecificPeoplePage(1, 5, "id", true)
        assertTrue(page.isEmpty(), "Page should be empty as there are no people in the database")
    }

    @Test
    fun findSpecificPeoplePage_backOrderingMixedCaseColumnName_returnsCorrectlyOrderedResults() {
        jdbcTemplate.update(QUERY_DELETE_PEOPLE)
        val insertedPeople = (1..12).map {
            Person(id = it, name = "Person$it", surname = "Surname$it", email = "person$it@email.com", logoId = it).apply { insertPerson(this) }
        }

        val pageSortedByName = personRepository.findSpecificPeoplePage(1, 12, "name", true)
        assertEquals(insertedPeople.sortedByDescending { it.name }, pageSortedByName, "Page should be back ordered by name, case insensitive")
    }

    @Test
    fun count_whenPeopleInDatabase_returnsCorrectCount() {
        val personCount = personRepository.count()

        assertEquals(1, personCount, "Should return the correct count of people in database")
    }

    @Test
    fun save_givenValidPerson_returnsSavedPerson() {
        val newPerson = createPersonEva()

        personRepository.save(newPerson)

        val persons = personRepository.findAll()
        val savedPerson = persons.first {
            it.name == newPerson.name && it.surname == newPerson.surname && it.email == newPerson.email && it.logoId == newPerson.logoId
        }
        assertPerson(newPerson, savedPerson)
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
        val person = createPersonEva()
        insertPerson(person)

        val persons = personRepository.search(person.name)

        assertEquals(1, persons.size)
        assertTrue(persons.contains(person))
    }

    @Test
    fun search_givenFullName_returnsCorrectPerson() {
        val person = createPersonEva()
        insertPerson(person)
        val searchQuery = "${person.name} ${person.surname}"

        val persons = personRepository.search(searchQuery)

        assertEquals(1, persons.size)
        assertTrue(persons.contains(person))
    }

    @Test
    fun search_givenPartOfSurname_returnsCorrectPerson() {
        val person = createPersonEva()
        insertPerson(person)
        val searchQuery = "${person.name} Smi"

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
        val newPerson = createPersonEva()
        personRepository.save(newPerson)

        personRepository.deleteAll()

        assertTrue(personRepository.findAll().isEmpty())
    }

    @Test
    fun deleteAll_methodCalledTwoTimes_databaseIsEmptyWithoutException() {
        val newPerson = createPersonEva()
        personRepository.save(newPerson)

        personRepository.deleteAll()
        personRepository.deleteAll()

        assertTrue(personRepository.findAll().isEmpty())
    }

    private fun createPersonAdam(): Person {
        return Person(1, "Adam", "Smith", "adam_smith@email.com", 1)
    }

    private fun createPersonEva(): Person {
        return Person(2, "Eva", "Smith", "eva_smith@email.com", 2)
    }

    private fun insertPerson(person: Person) {
        jdbcTemplate.update(QUERY_INSERT_PERSON, person.id, person.name, person.surname, person.email, person.logoId)
    }

    private fun assertPerson(expected: Person, actual: Person) {
        assertAll("Person",
            Executable { assertEquals(expected.name, actual.name, "Name should match") },
            Executable { assertEquals(expected.surname, actual.surname, "Surname should match") },
            Executable { assertEquals(expected.email, actual.email, "Email should match") },
            Executable { assertEquals(expected.logoId, actual.logoId, "LogoId should match") }
        )
    }
}
