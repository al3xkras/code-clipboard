package com.al3xkras.code_clipboard.controller;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import com.al3xkras.code_clipboard.repository.CodeRepository;
import com.al3xkras.code_clipboard.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class CodeController {
    @Autowired
    private CodeService codeService;
    @PostMapping("/search")
    public ResponseEntity<List<Code>> searchCodeSamples(@RequestParam(name = "language",required = false) String language,
                                                        @RequestParam(name = "substring",required = false)String substring,
                                                        @RequestParam(name = "tags",required = false) String tagString) {
        List<String> tags;
        if (tagString!=null){
            tags = Arrays.asList(tagString.split(" "));
        } else {
            tags=Collections.emptyList();
        }
        log.info(tags.toString());
        ProgrammingLanguage lang=null;
        try {
            lang=ProgrammingLanguage.valueOf(language.toUpperCase());
        }catch (IllegalArgumentException | NullPointerException ignored){}
        log.info(""+lang);

        if (lang==null){
            if (substring!=null){
                if (!tags.isEmpty()){
                    return ResponseEntity.badRequest().build();
                }
                return ResponseEntity.ok(codeService.findAllBySubstring(substring));
            }
            if (!tags.isEmpty()){
                return ResponseEntity.ok(codeService.findAllByTags(tags));
            }
            return ResponseEntity.badRequest().build();
        }
        if (substring==null && !tags.isEmpty()){
            return ResponseEntity.ok(codeService.findAllByTagsAndLanguage(tags, lang));
        }
        return ResponseEntity.badRequest().build();
    }
}
