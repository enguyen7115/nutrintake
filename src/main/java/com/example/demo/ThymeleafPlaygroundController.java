package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafPlaygroundController {

    @GetMapping("/play")
    public String play(Model model) {

        model.addAttribute("name", "Zoe");
        model.addAttribute("number", 7);
        model.addAttribute("foods", java.util.List.of("Oatmeal", "Chicken Bowl", "Protein Bar"));
        model.addAttribute("overSugar", true);



        return "playground";
    }
}