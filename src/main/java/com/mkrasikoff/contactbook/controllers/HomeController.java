package com.mkrasikoff.contactbook.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This is a controller class that handles HTTP requests related to the home page of the application.
 */
@Controller
public class HomeController {

    /**
     * Handles GET requests to the root ("/") URL of the application.
     *
     * @return The view of the home page (page with Show / New / Edit / Delete person functionality).
     */
    @GetMapping("/")
    public String welcomePage() {
        return "/main/homePage";
    }
}
