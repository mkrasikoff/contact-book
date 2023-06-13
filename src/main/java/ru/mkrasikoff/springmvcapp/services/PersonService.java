package ru.mkrasikoff.springmvcapp.services;

import org.springframework.stereotype.Service;
import ru.mkrasikoff.springmvcapp.models.Person;
import ru.mkrasikoff.springmvcapp.repos.PersonRepository;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final GenerateService generateService;

    public PersonService(PersonRepository personRepository, GenerateService generateService) {
        this.personRepository = personRepository;
        this.generateService = generateService;
    }

    public List<Person> showPeople() {
        return personRepository.findAll();
    }

    public Person showPerson(int id) {
        return personRepository.findById(id);
    }

    public void savePerson(Person person) {
        personRepository.save(person);
    }

    public void updatePerson(Person person, int id) {
        personRepository.update(person, id);
    }

    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    public List<Person> search(String query) {
        return personRepository.search(query);
    }

    public void createRandomPeople() {
        for (int i = 1; i <= 10; i++) {
            savePerson(generateService.generateRandomPerson());
        }
    }
}
