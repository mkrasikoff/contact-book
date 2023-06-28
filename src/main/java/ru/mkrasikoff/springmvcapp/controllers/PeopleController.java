package ru.mkrasikoff.springmvcapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mkrasikoff.springmvcapp.exceptions.PersonNotFoundException;
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
     * Display a paginated list of all people.
     *
     * This endpoint handles GET requests to show a page of people. The number of people shown per page
     * and the page number can be specified with parameters. If these parameters are not provided,
     * they default to 1 for the page number and 10 for the number of people per page.
     *
     * @param page An integer specifying the page number. Defaults to 1 if not provided.
     * @param size An integer specifying the number of people to display per page. Defaults to 10 if not provided.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping
    public String getPeople(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        List<Person> people = personService.showPeoplePage(page, size);
        model.addAttribute("people", people);

        int count = personService.countPeople();
        int pages = (count + size - 1) / size;

        model.addAttribute("pages", pages);
        model.addAttribute("page", page);

        return "people/showPeople";
    }

    /**
     * Display a single person based on his ID.
     *
     * @param id The ID of the person to display.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     *
     * If the person with the provided ID does not exist,
     * it catches a PersonNotFoundException and redirects to the people list page.
     */
    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("person", personService.showPerson(id));
        } catch (PersonNotFoundException exc) {
            return "redirect:/people";
        }
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
     * Display a paginated list of all people for updating.
     *
     * This endpoint handles GET requests to show a page of people for updating. The number of people shown per page
     * and the page number can be specified with parameters. If these parameters are not provided,
     * they default to 1 for the page number and 10 for the number of people per page.
     *
     * @param page An integer specifying the page number. Defaults to 1 if not provided.
     * @param size An integer specifying the number of people to display per page. Defaults to 10 if not provided.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/edit")
    public String editAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        List<Person> people = personService.showPeoplePage(page, size);
        model.addAttribute("people", people);

        int count = personService.countPeople();
        int pages = (count + size - 1) / size;

        model.addAttribute("pages", pages);
        model.addAttribute("page", page);

        return "people/editPeople";
    }

    /**
     * Display the form for updating a single person based on his ID.
     *
     * @param id The ID of the person to update.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     *
     * If the person with the provided ID does not exist,
     * it catches a PersonNotFoundException and redirects to the people list page.
     */
    @GetMapping("/{id}/edit")
    public String editOne(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("person", personService.showPerson(id));
        } catch (PersonNotFoundException exc) {
            return "redirect:/people";
        }
        return "people/editPerson";
    }

    /**
     * Handle the submission of the form to update a person.
     *
     * @param person The person to update.
     * @param bindingResult The result of the form binding.
     * @param id The ID of the person to update.
     * @return The view to display.
     *
     * If the person with the provided ID does not exist,
     * it catches a PersonNotFoundException and redirects to the people list page.
     */
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) return "people/editPerson";
        try {
            personService.updatePerson(person, id);
        } catch (PersonNotFoundException exc) {
            return "redirect:/people";
        }
        return "redirect:/people";
    }

    /**
     * Display a paginated list of all people for deleting.
     *
     * This endpoint handles GET requests to show a page of people for deleting. The number of people shown per page
     * and the page number can be specified with parameters. If these parameters are not provided,
     * they default to 1 for the page number and 10 for the number of people per page.
     *
     * @param page An integer specifying the page number. Defaults to 1 if not provided.
     * @param size An integer specifying the number of people to display per page. Defaults to 10 if not provided.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/delete")
    public String getDeletablePeople(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     Model model) {
        List<Person> people = personService.showPeoplePage(page, size);
        model.addAttribute("people", people);

        int count = personService.countPeople();
        int pages = (count + size - 1) / size;

        model.addAttribute("pages", pages);
        model.addAttribute("page", page);

        return "people/deletePeople";
    }

    /**
     * Display the confirmation page for deleting a person.
     *
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     *
     * If the person with the provided ID does not exist,
     * it catches a PersonNotFoundException and redirects to the people list page.
     */
    @GetMapping("/{id}/delete")
    public String getDeletablePerson(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("person",personService.showPerson(id));
        } catch (PersonNotFoundException exc) {
            return "redirect:/people";
        }
        return "people/deletePersonConfirm";
    }

    /**
     * Handle the confirmation to delete a person.
     *
     * @param id The ID of the person to delete.
     * @return The view to display.
     *
     * If the person with the provided ID does not exist,
     * it catches a PersonNotFoundException and redirects to the people list page.
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            personService.deletePerson(id);
        } catch (PersonNotFoundException exc) {
            return "redirect:/people";
        }
        return "redirect:/people";
    }

    /**
     * Display people based on a search query.
     *
     * @param query The search query.
     * @param mode A string specifying the mode (show / edit / delete). Defaults to show if not provided.
     * @param model The Model object to bind data to the view.
     * @return The view to display.
     */
    @GetMapping("/search")
    public String search(@RequestParam String query,
                         @RequestParam(defaultValue = "show") String mode,
                         Model model) {
        List<Person> persons = personService.search(query);
        model.addAttribute("people", persons);

        if(mode.equals("edit")) {
            return "people/editPeople";
        }
        else if(mode.equals("delete")) {
            return "people/deletePeople";
        }
        else {
            return "people/showPeople";
        }
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
