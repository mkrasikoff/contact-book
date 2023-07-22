import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import com.mkrasikoff.contactbook.exceptions.InvalidSortParameterException
import com.mkrasikoff.contactbook.exceptions.PersonAlreadyExistsException
import com.mkrasikoff.contactbook.exceptions.PersonNotFoundException
import com.mkrasikoff.contactbook.models.Person
import com.mkrasikoff.contactbook.repos.PersonRepository
import com.mkrasikoff.contactbook.services.GenerateService
import com.mkrasikoff.contactbook.services.PersonService
import kotlin.test.assertEquals

class PersonServiceTest {

    companion object {
        const val PERSON_ID = 1
        const val PERSON_NAME = "Adam"
        const val PERSON_SURNAME = "Smith"
        const val PERSON_EMAIL = "adam_smith@email.com"
        const val PERSON_LOGO_ID = 1

        const val PERSON_ID_2 = 2
        const val PERSON_NAME_2 = "Eva"
        const val PERSON_SURNAME_2 = "Smith"
        const val PERSON_EMAIL_2 = "eva_smith@email.com"
        const val PERSON_LOGO_ID_2 = 2

        var PERSON: Person = Person(
            id = PERSON_ID,
            name = PERSON_NAME,
            surname = PERSON_SURNAME,
            email = PERSON_EMAIL,
            logoId = PERSON_LOGO_ID
        )
        var PERSON_2: Person = Person(
            id = PERSON_ID_2,
            name = PERSON_NAME_2,
            surname = PERSON_SURNAME_2,
            email = PERSON_EMAIL_2,
            logoId = PERSON_LOGO_ID_2
        )

        var PEOPLE: List<Person> = listOf(PERSON, PERSON_2)

        const val ERROR_MESSAGE = "Some error message."
    }
    private lateinit var personRepository: PersonRepository
    private lateinit var personService: PersonService
    private lateinit var generateService: GenerateService

    @BeforeEach
    fun setUp() {
        personRepository = mockk()
        generateService = mockk()
        personService = PersonService(personRepository, generateService)
    }

    @Test
    fun savePerson_whenPersonIsValid_personSaved() {
        every {
            personRepository.save(any())
        } returns Unit

        personService.savePerson(PERSON)

        verify {
            personRepository.save(PERSON)
        }
    }

    @Test
    fun savePerson_whenPersonWithThisIdAlreadyExists_exceptionThrown() {
        every {
            personRepository.save(any())
        } throws PersonAlreadyExistsException(ERROR_MESSAGE)

        assertThrows<PersonAlreadyExistsException>() {
            personService.savePerson(PERSON)
        }
    }

    @Test
    fun showPerson_personExists_personReturned() {
        every {
            personRepository.findById(any())
        } returns PERSON

        val foundPerson = personService.showPerson(PERSON_ID)

        verify {
            personRepository.findById(PERSON_ID)
        }
        assertEquals(PERSON, foundPerson)
    }

    @Test
    fun showPerson_personDoesNotExists_exceptionThrown() {
        every {
            personRepository.findById(any())
        } throws PersonNotFoundException(ERROR_MESSAGE)

        assertThrows<PersonNotFoundException> {
            personService.showPerson(PERSON_ID)
        }
    }

    @Test
    fun showPeople_peopleExist_peopleReturned() {
        every {
            personRepository.findAll()
        } returns PEOPLE

        val foundPeople = personService.showPeople()

        verify {
            personRepository.findAll()
        }
        assertEquals(PEOPLE, foundPeople)
    }

    @Test
    fun showPeople_peopleDoesNotExist_emptyListReturned() {
        every {
            personRepository.findAll()
        } returns listOf()

        val foundPeople = personService.showPeople()

        verify {
            personRepository.findAll()
        }
        assertEquals(listOf<Person>(), foundPeople)
    }

    @Test
    fun showPeoplePage_peopleExist_peopleReturned() {
        val page = 1
        val size = 10
        val sort = "id"
        val reverse = false
        every {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        } returns PEOPLE

        val foundPeople = personService.showPeoplePage(page, size, sort, reverse)

        verify {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        }
        assertEquals(PEOPLE, foundPeople)
    }

    @Test
    fun showPeoplePage_peopleDoesNotExist_emptyListReturned() {
        val page = 1
        val size = 10
        val sort = "id"
        val reverse = false
        every {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        } returns listOf()

        val foundPeople = personService.showPeoplePage(page, size, sort, reverse)

        verify {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        }
        assertEquals(listOf<Person>(), foundPeople)
    }

    @Test
    fun showPeoplePage_sortedByName_peopleReturnedInCorrectOrder() {
        val page = 1
        val size = 10
        val sort = "name"
        val reverse = false
        val sortedPeople = PEOPLE.sortedBy { it.name }
        every {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        } returns sortedPeople

        val foundPeople = personService.showPeoplePage(page, size, sort, reverse)

        verify {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        }
        assertEquals(sortedPeople, foundPeople)
    }

    @Test
    fun showPeoplePage_sortedByNameBackwards_peopleReturnedInCorrectOrder() {
        val page = 1
        val size = 10
        val sort = "name"
        val reverse = true
        val sortedPeople = PEOPLE.sortedByDescending { it.name }
        every {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        } returns sortedPeople

        val foundPeople = personService.showPeoplePage(page, size, sort, reverse)

        verify {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        }
        assertEquals(sortedPeople, foundPeople)
    }

    @Test
    fun showPeoplePage_sortedBySurname_peopleReturnedInCorrectOrder() {
        val page = 1
        val size = 10
        val sort = "surname"
        val reverse = false
        val sortedPeople = PEOPLE.sortedBy { it.surname }
        every {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        } returns sortedPeople

        val foundPeople = personService.showPeoplePage(page, size, sort, reverse)

        verify {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        }
        assertEquals(sortedPeople, foundPeople)
    }

    @Test
    fun showPeoplePage_sortedBySurnameBackwards_peopleReturnedInCorrectOrder() {
        val page = 1
        val size = 10
        val sort = "surname"
        val reverse = true
        val sortedPeople = PEOPLE.sortedByDescending { it.surname }
        every {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        } returns sortedPeople

        val foundPeople = personService.showPeoplePage(page, size, sort, reverse)

        verify {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        }
        assertEquals(sortedPeople, foundPeople)
    }

    @Test
    fun showPeoplePage_invalidSortParameter_exceptionThrown() {
        val page = 1
        val size = 10
        val sort = "invalidSortParameter"
        val reverse = false
        every {
            personRepository.findSpecificPeoplePage(page, size, sort, reverse)
        } throws InvalidSortParameterException(ERROR_MESSAGE)

        assertThrows<InvalidSortParameterException> {
            personService.showPeoplePage(page, size, sort, reverse)
        }
    }

    @Test
    fun countPeople_peopleExist_correctCountReturned() {
        val count = PEOPLE.size
        every {
            personRepository.count()
        } returns count

        val peopleCount = personService.countPeople()

        verify {
            personRepository.count()
        }
        assertEquals(count, peopleCount)
    }

    @Test
    fun countPeople_peopleDoesNotExist_zeroReturned() {
        every {
            personRepository.count()
        } returns 0

        val peopleCount = personService.countPeople()

        verify {
            personRepository.count()
        }
        assertEquals(0, peopleCount)
    }

    @Test
    fun updatePerson_personInfoIsGiven_personUpdated() {
        val updatedPerson = Person(id = 3,
            name = "Patrick",
            surname = "Smith",
            email = "patrick_smith@email.com",
            logoId = 3
        )
        every {
            personRepository.update(any(), any())
        } returns Unit

        personService.updatePerson(updatedPerson, PERSON_ID)

        verify {
            personRepository.update(updatedPerson, PERSON_ID)
        }
    }

    @Test
    fun updatePerson_personDoesNotExist_exceptionThrown() {
        val updatedPerson = Person(
            id = 3,
            name = "Patrick",
            surname = "Smith",
            email = "patrick_smith@email.com",
            logoId = 3
        )
        every {
            personRepository.update(any(), any())
        } throws PersonNotFoundException(ERROR_MESSAGE)

        assertThrows<PersonNotFoundException> {
            personService.updatePerson(updatedPerson, PERSON_ID)
        }
    }

    @Test
    fun deletePerson_personExisted_personDeleted() {
        every {
            personRepository.deleteById(any())
        } returns Unit

        personService.deletePerson(PERSON_ID)

        verify {
            personRepository.deleteById(PERSON_ID)
        }
    }

    @Test
    fun deletePerson_personDidNotExist_exceptionThrown() {
        every {
            personRepository.deleteById(any())
        } throws PersonNotFoundException(ERROR_MESSAGE)

        assertThrows<PersonNotFoundException> {
            personService.deletePerson(PERSON_ID)
        }
    }

    @Test
    fun createRandomPeople_functionIsCalled_peopleCreated() {
        every {
            generateService.generateRandomPerson()
        } returns PERSON
        every {
            personRepository.save(any())
        } returns Unit

        personService.createRandomPeople()

        verify(exactly = 10) { generateService.generateRandomPerson() }
        verify(exactly = 10) { personRepository.save(any<Person>()) }
    }

    @Test
    fun deleteAllPeople_givenDatabaseWithPeople_peopleDeleted() {
        every {
            personRepository.deleteAll()
        } returns Unit

        personService.deleteAllPeople()

        verify {
            personRepository.deleteAll()
        }
    }

    @Test
    fun search_peopleExistWithQuery_peopleReturned() {
        val query = "Smith"
        every {
            personRepository.search(query)
        } returns PEOPLE

        val foundPeople = personService.search(query)

        verify {
            personRepository.search(query)
        }
        assertEquals(PEOPLE, foundPeople)
    }

    @Test
    fun search_peopleDoesNotExistWithQuery_emptyListReturned() {
        val query = "Patrick"
        every {
            personRepository.search(query)
        } returns listOf()

        val foundPeople = personService.search(query)

        verify {
            personRepository.search(query)
        }
        assertEquals(listOf<Person>(), foundPeople)
    }
}
