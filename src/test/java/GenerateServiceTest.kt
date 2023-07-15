import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.mkrasikoff.contactbook.services.GenerateService

class GenerateServiceTest {

    private lateinit var generateService: GenerateService

    @BeforeEach
    fun setup() {
        generateService = GenerateService()
    }

    @Test
    fun generateRandomPerson_methodIsCalled_personReturned() {
        val person = generateService.generateRandomPerson()

        assertNotNull(person, "Person is null")
        assertNotNull(person.name, "Name is null")
        assertNotNull(person.surname, "Surname is null")
        assertNotNull(person.email, "Email is null")
        assertNotNull(person.logoId, "LogoId is null")
        assertFalse(person.name!!.isEmpty(), "Name is empty")
        assertFalse(person.surname!!.isEmpty(), "Surname is empty")
        assertFalse(person.email!!.isEmpty(), "Email is empty")
        assertTrue(person.logoId in 1..4, "Generated logoId is not within the expected range")
    }
}
