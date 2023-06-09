package ru.mkrasikoff.springmvcapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mkrasikoff.springmvcapp.models.Person;
import ru.mkrasikoff.springmvcapp.services.PersonService;
import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;

    public PeopleController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/show")
    public String showPeople(Model model) {
        model.addAttribute("people", personService.showPeople());
        return "people/showPeople";
    }

    @GetMapping("/show/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.showPerson(id));
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
        personService.savePerson(person);
        return "redirect:/people/show";
    }

    @GetMapping("/edit")
    public String editPeople(Model model) {
        model.addAttribute("people", personService.showPeople());
        return "people/editPeople";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(Model model,
                             @PathVariable("id") int id) {
        model.addAttribute("person", personService.showPerson(id));
        return "people/editPerson";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) return "people/editPerson";
        personService.updatePerson(person, id);
        return "redirect:/people/show";
    }

    @GetMapping("/delete")
    public String deletePeople(Model model) {
        model.addAttribute("people", personService.showPeople());
        return "people/deletePeople";
    }

    @GetMapping("/{id}/delete")
    public String deletePerson(Model model,
                               @PathVariable("id") int id) {
        model.addAttribute("person",personService.showPerson(id));
        return "people/deletePerson";
    }

    @DeleteMapping("/{id}")
    public String removePerson(@PathVariable("id") int id) {
        personService.deletePerson(id);
        return "redirect:/people/show";
    }
}
