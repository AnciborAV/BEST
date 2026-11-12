package ru.aav.opora.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("projectName", "ОПОРА");
        model.addAttribute("projectIdea", "Пережить. Осмыслить. Создавать.");
        return "home";
    }
    // Отдельная страница открывается и по карточке, и по прямому адресу.
    @GetMapping("/stability")
    public String stability() {
        return "stability";
    }

    // Страница поддержки в трудной ситуации с переходом на «Вернуть устойчивость».
    @GetMapping("/hardship")
    public String hardship() {
        return "hardship";
    }
}
