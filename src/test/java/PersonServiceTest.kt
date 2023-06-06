import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.mkrasikoff.springmvcapp.models.Person
import ru.mkrasikoff.springmvcapp.repo.PersonRepository
import ru.mkrasikoff.springmvcapp.service.PersonService

class PersonServiceTest {

    private lateinit var personRepository: PersonRepository
    private lateinit var personService: PersonService

    @BeforeEach
    fun setUp() {
        personRepository = mockk()
        personService = PersonService(personRepository)
    }

    @Test
    fun savePerson_whenPersonIsValid_personSuccessfullySaved() {

        val person = Person(
            id = 1,
            name = "Adam",
            surname = "Smith",
            email = "adam_smith@email.com"
        )
        every {
            personRepository.save(any())
        } returns Unit

        personService.savePerson(person)

        verify {
            personRepository.save(person)
        }
    }
}
