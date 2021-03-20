package ru.mkrasikoff.springmvcapp.dao;

import org.springframework.stereotype.Component;
import ru.mkrasikoff.springmvcapp.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private List<Person> people;

    {
        people = new ArrayList<>();
        for(int i = 1; i < 3; i++) {
            people.add(new Person(i, "Person" , "" + i, "" + i + "@gmail.com"));
        }
    }

    public List<Person> showAll() {
        return people;
    }

    public Person showPerson(int id) {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person) {
        person.setId(people.size() + 1);
        people.add(person);
    }

    public void delete(int id) {
        people.remove(id);
    }

    public void update(Person updatedPerson, int id) {
        Person personToBeUpdated = showPerson(id);

        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setSurname(updatedPerson.getSurname());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
    }
}
