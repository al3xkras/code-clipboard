package com.al3xkras.code_clipboard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PagesController {

    @GetMapping("/")
    public String mainPage(){
        return "index";
    }

    @GetMapping("/search")
    public String codeSearchPage(){
        return "search";
    }

    @GetMapping("/submit")
    public String codeSubmissionPage(){
        return "submit";
    }
}
