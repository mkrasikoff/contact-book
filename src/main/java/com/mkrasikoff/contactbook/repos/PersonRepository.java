package com.mkrasikoff.contactbook.repos;

import com.mkrasikoff.contactbook.models.Person;

import java.util.List;

public interface PersonRepository {

    List<Person> findAll();
    List<Person> findSpecificPeoplePage(int page, int size, String sort);
    int count();
    Person findById(int id);
    void save(Person person);
    void update(Person person, int id);
    void deleteById(int id);
    void deleteAll();
    List<Person> search(String query);
}
