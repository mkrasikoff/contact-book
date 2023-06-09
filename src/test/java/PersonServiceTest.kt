import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.mkrasikoff.springmvcapp.exceptions.PersonAlreadyExistsException
import ru.mkrasikoff.springmvcapp.exceptions.PersonNotFoundException
import ru.mkrasikoff.springmvcapp.models.Person
import ru.mkrasikoff.springmvcapp.repos.PersonRepository
import ru.mkrasikoff.springmvcapp.services.PersonService
import kotlin.test.assertEquals

class PersonServiceTest {

    companion object {
        const val PERSON_ID = 1
        const val PERSON_NAME = "Adam"
        const val PERSON_SURNAME = "Smith"
        const val PERSON_EMAIL = "adam_smith@email.com"

        const val PERSON_ID_2 = 2
        const val PERSON_NAME_2 = "Eva"
        const val PERSON_SURNAME_2 = "Smith"
        const val PERSON_EMAIL_2 = "eva_smith@email.com"

        var PERSON: Person = Person(
            id = PERSON_ID,
            name = PERSON_NAME,
            surname = PERSON_SURNAME,
            email = PERSON_EMAIL
        )
        var PERSON_2: Person = Person(
        id = PERSON_ID_2,
        name = PERSON_NAME_2,
        surname = PERSON_SURNAME_2,
        email = PERSON_EMAIL_2
        )

        var PEOPLE: List<Person> = listOf(PERSON, PERSON_2)

        const val ERROR_MESSAGE = "Some error message."
    }
    private lateinit var personRepository: PersonRepository
    private lateinit var personService: PersonService

    @BeforeEach
    fun setUp() {
        personRepository = mockk()
        personService = PersonService(personRepository)
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
    fun updatePerson_personInfoIsGiven_personUpdated() {
        val updatedPerson = Person(id = 3,
            name = "Patrick",
            surname = "Smith",
            email = "patrick_smith@email.com"
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
            email = "patrick_smith@email.com"
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
}
