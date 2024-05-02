package io.github.bigcookie233.simpleshop.controllers;

import io.github.bigcookie233.simpleshop.services.ConfigurablePropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private ConfigurablePropertyService configurablePropertyService;

    public AdminController(ConfigurablePropertyService  configurablePropertyService) {
        this.configurablePropertyService = configurablePropertyService;
    }

    @GetMapping("admin/")
    public void Admin(Model model) {

    }
}
