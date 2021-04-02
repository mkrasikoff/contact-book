package ru.mkrasikoff.springmvcapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mkrasikoff.springmvcapp.dao.PersonDAO;
import ru.mkrasikoff.springmvcapp.models.Person;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonDAO personDAO;

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

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "people/newPerson";
    }

    @PostMapping
    public String createPerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return "people/newPerson";
        personDAO.save(person);
        return "redirect:/people/show";
    }

    @GetMapping("/edit")
    public String editPeople(Model model) {
        model.addAttribute("people", personDAO.showAll());
        return "people/editPeople";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person",personDAO.showPerson(id));
        return "people/editPerson";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) return "people/editPerson";
        personDAO.update(person, id);
        return "redirect:/people/show";
    }

    @GetMapping("/delete")
    public String deletePeople(Model model) {
        model.addAttribute("people", personDAO.showAll());
        return "people/deletePeople";
    }

    @GetMapping("/{id}/delete")
    public String deletePerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person",personDAO.showPerson(id));
        return "people/deletePerson";
    }

    @DeleteMapping("/{id}")
    public String removePerson(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people/show";
    }
}
