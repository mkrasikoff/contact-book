package ru.mkrasikoff.springmvcapp.repo;

import ru.mkrasikoff.springmvcapp.models.Person;
import java.util.List;

public interface PersonRepository {

    List<Person> findAll();
    Person findById(int id);
    void save(Person person);
    void update(Person person, int id);
    void deleteById(int id);
}
