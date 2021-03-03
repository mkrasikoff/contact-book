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
        for(int i = 1; i < 10; i++) {
            people.add(new Person(i, "Person " + i));
        }
    }

    public List<Person> showAll() {
        return people;
    }

    public Person showPerson(int id) {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }
}
