package ru.mkrasikoff.springmvcapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mkrasikoff.springmvcapp.models.Person;
import ru.mkrasikoff.springmvcapp.services.PersonService;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;

    public PeopleController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Display a list of all people.
     *
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping
    public String getPeople(Model model) {
        model.addAttribute("people", personService.showPeople());
        return "people/showPeople";
    }

    /**
     * Display a single person based on his ID.
     *
     * @param id The ID of the person to display.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.showPerson(id));
        return "people/showPerson";
    }

    /**
     * Provide the form to create a new person.
     *
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/create")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "people/newPerson";
    }

    /**
     * Handle the submission of the form to create a new person.
     *
     * @param person The person to create.
     * @param bindingResult The result of the form binding.
     * @return The view to display.
     */
    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return "people/newPerson";
        personService.savePerson(person);
        return "redirect:/people";
    }

    /**
     * Display the form for updating all people.
     *
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/edit")
    public String editAll(Model model) {
        model.addAttribute("people", personService.showPeople());
        return "people/editPeople";
    }

    /**
     * Display the form for updating a single person based on his ID.
     *
     * @param id The ID of the person to update.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/{id}/edit")
    public String editOne(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.showPerson(id));
        return "people/editPerson";
    }

    /**
     * Handle the submission of the form to update a person.
     *
     * @param person The person to update.
     * @param bindingResult The result of the form binding.
     * @param id The ID of the person to update.
     * @return The view to display.
     */
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) return "people/editPerson";
        personService.updatePerson(person, id);
        return "redirect:/people";
    }

    /**
     * Display the form for all deletable people.
     *
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/delete")
    public String getDeletablePeople(Model model) {
        model.addAttribute("people", personService.showPeople());
        return "people/deletePeople";
    }

    /**
     * Display the confirmation page for deleting a person.
     *
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/{id}/delete")
    public String getDeletablePerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person",personService.showPerson(id));
        return "people/deletePersonConfirm";
    }

    /**
     * Handle the confirmation to delete a person.
     *
     * @param id The ID of the person to delete.
     * @return The view to display.
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.deletePerson(id);
        return "redirect:/people";
    }

    /**
     * Display people based on a search query.
     *
     * @param query The search query.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        List<Person> persons = personService.search(query);
        model.addAttribute("people", persons);
        return "people/showPeople";
    }

    /**
     * Generate a list of random people.
     *
     * @return The view to display.
     */
    @PostMapping("/generate")
    public String generateRandomPeople() {
        personService.createRandomPeople();
        return "redirect:/people";
    }

    /**
     * Display the confirmation page for deleting all people.
     *
     * @return The view to display.
     */
    @GetMapping("/deleteAll/confirm")
    public String confirmDeleteAll() {
        return "people/deletePeopleConfirm";
    }

    /**
     * Handle the confirmation to delete all people.
     *
     * @return The view to display next.
     */
    @DeleteMapping("/deleteAll")
    public String deleteAll() {
        personService.deleteAllPeople();
        return "redirect:/people";
    }
}
