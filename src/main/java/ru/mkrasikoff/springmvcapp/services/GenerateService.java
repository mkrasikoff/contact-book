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
        int id = random.nextInt();
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        String email = faker.internet().emailAddress();

        return new Person(id, name, surname, email);
    }
}
