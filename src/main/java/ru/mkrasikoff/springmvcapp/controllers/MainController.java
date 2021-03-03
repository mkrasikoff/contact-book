package ru.mkrasikoff.springmvcapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.mkrasikoff.springmvcapp.dao.PersonDAO;

@Controller
public class MainController {

    @Autowired
    private PersonDAO personDAO;

    @GetMapping("/")
    public String welcomePage(Model model) {
        return "main/welcome";
    }

    @GetMapping("/show")
    public String showPeople(Model model) {
        model.addAttribute("people", personDAO.showAll());
        return "people/showPeople";
    }

    @GetMapping("/show/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.showPerson(id));
        return "people/showPerson";
    }
}
