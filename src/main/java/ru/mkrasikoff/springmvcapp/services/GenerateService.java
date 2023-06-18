package ru.mkrasikoff.springmvcapp.services;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;
import ru.mkrasikoff.springmvcapp.models.Person;
import java.util.Random;

@Component
public class GenerateService {

    Random random = new Random();
    Faker faker = new Faker();

    public Person generateRandomPerson() {
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        String email = faker.internet().emailAddress();
        int logoId = generateRandomLogoId();

        return new Person(name, surname, email, logoId);
    }

    private int generateRandomLogoId() {
        return random.nextInt(4) + 1;
    }
}
