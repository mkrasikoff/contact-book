import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.mkrasikoff.springmvcapp.models.Person
import ru.mkrasikoff.springmvcapp.repo.PersonRepository
import ru.mkrasikoff.springmvcapp.service.PersonService
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
    }
    private lateinit var personRepository: PersonRepository
    private lateinit var personService: PersonService
    private var person: Person = Person(
        id = PERSON_ID,
        name = PERSON_NAME,
        surname = PERSON_SURNAME,
        email = PERSON_EMAIL
    )
    private var people: List<Person> = listOf(
        Person(
            id = PERSON_ID,
            name = PERSON_NAME,
            surname = PERSON_SURNAME,
            email = PERSON_EMAIL
        ),
        Person(
            id = PERSON_ID_2,
            name = PERSON_NAME_2,
            surname = PERSON_SURNAME_2,
            email = PERSON_EMAIL_2
        )
    )

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

        personService.savePerson(person)

        verify {
            personRepository.save(person)
        }
    }

    @Test
    fun showPerson_personExists_personReturned() {
        every {
            personRepository.findById(1)
        } returns person

        val foundPerson = personService.showPerson(1)

        verify {
            personRepository.findById(1)
        }
        assertEquals(person, foundPerson)
    }

    @Test
    fun showPerson_personNotExists_nullReturned() {
        every {
            personRepository.findById(1)
        } returns null

        val foundPerson = personService.showPerson(1)

        verify {
            personRepository.findById(1)

        }
        assertNull(foundPerson)
    }

    @Test
    fun showPeople_peopleExist_peopleReturned() {
        every {
            personRepository.findAll()
        } returns people

        val foundPeople = personService.showPeople()

        verify {
            personRepository.findAll()
        }
        assertEquals(people, foundPeople)
    }

    @Test
    fun showPeople_peopleNotExist_nullReturned() {
        every {
            personRepository.findAll()
        } returns null

        val foundPeople = personService.showPeople()

        verify {
            personRepository.findAll()
        }
        assertNull(foundPeople)
    }
}
