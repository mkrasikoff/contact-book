package com.mkrasikoff.contactbook.services;

import com.mkrasikoff.contactbook.models.Person;
import org.springframework.stereotype.Service;
import com.mkrasikoff.contactbook.repos.PersonRepository;
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

    public List<Person> showPeoplePage(int page, int size) {
        return personRepository.findSpecificPeoplePage(page, size);
    }

    public int countPeople() {
        return personRepository.count();
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

    public void deleteAllPeople() {
        personRepository.deleteAll();
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
