package com.mkrasikoff.contactbook.services;

import com.github.javafaker.Faker;
import com.mkrasikoff.contactbook.models.Person;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GenerateService {

    Random random = new Random();
    Faker faker = new Faker();

    public Person generateRandomPerson() {
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        String email = name.toLowerCase() + "." + surname.toLowerCase() + "@email.com";
        int logoId = generateRandomLogoId();

        return new Person(name, surname, email, logoId);
    }

    private int generateRandomLogoId() {
        return random.nextInt(4) + 1;
    }
}
