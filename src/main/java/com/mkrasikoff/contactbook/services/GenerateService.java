package com.mkrasikoff.contactbook.services;

import com.github.javafaker.Faker;
import com.mkrasikoff.contactbook.models.Person;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * This service provides methods to generate random Person objects.
 */
@Component
public class GenerateService {

    Random random = new Random();
    Faker faker = new Faker();

    /**
     * Generates a random Person object.
     * The name, surname, and email of the Person are generated using the Faker library.
     * The logoId of the Person is a random integer between 1 and 4 (inclusive).
     * @return the generated Person object.
     */
    public Person generateRandomPerson() {
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        String email = name.toLowerCase() + "." + surname.toLowerCase() + "@email.com";
        int logoId = generateRandomLogoId();

        return new Person(name, surname, email, logoId);
    }

    /**
     * Generates a random integer between 1 and 4 (inclusive).
     * This integer is used as the logoId for the generated Person objects.
     * @return the generated integer.
     */
    private int generateRandomLogoId() {
        return random.nextInt(4) + 1;
    }
}
