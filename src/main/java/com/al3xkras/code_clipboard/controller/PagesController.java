package com.al3xkras.code_clipboard.controller;

import com.al3xkras.code_clipboard.config.ShutdownManager;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PagesController {

    @Autowired
    private ShutdownManager shutdownManager;

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

    @PostMapping("/quit")
    public String exit(){
        new Thread(()-> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                shutdownManager.initiateShutdown(0);
                return;
            }
            shutdownManager.initiateShutdown(0);
        }).start();
        return "quit";
    }
}
