package ru.mkrasikoff.springmvcapp.service;

import org.springframework.stereotype.Service;
import ru.mkrasikoff.springmvcapp.models.Person;
import ru.mkrasikoff.springmvcapp.repo.PersonRepository;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
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
}