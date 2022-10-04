package com.al3xkras.code_clipboard.controller;

import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PagesController {

    @GetMapping("/")
    public String mainPage(){
        return "index";
    }

    @GetMapping("/search")
    public String codeSearchPage(Model model){
        model.addAttribute("languages", Arrays.stream(ProgrammingLanguage.values()).skip(1).collect(Collectors.toList()));
        return "search";
    }

    @GetMapping("/submit")
    public String codeSubmissionPage(Model model){
        model.addAttribute("languages", Arrays.asList(ProgrammingLanguage.values()));
        return "submit";
    }
}
