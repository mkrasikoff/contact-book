package ru.mkrasikoff.springmvcapp.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.mkrasikoff.springmvcapp.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();

        person.setId(resultSet.getInt("id"));
        person.setName(resultSet.getString("name"));
        person.setSurname(resultSet.getString("surname"));
        person.setEmail(resultSet.getString("email"));

        return person;
    }
}
