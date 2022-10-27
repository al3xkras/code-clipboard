package com.al3xkras.code_clipboard.controller;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import com.al3xkras.code_clipboard.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class CodeController {
    @Autowired
    private CodeService codeService;

    @PostMapping("/search")
    public ResponseEntity<List<Code>> searchCodeSamples(@RequestParam(name = "language",required = false) String language,
                                                        @RequestParam(name = "substring",required = false)String substring,
                                                        @RequestParam(name = "tags",required = false) String tagString,
                                                        @RequestParam(name = "page", required = false) Integer page,
                                                        @RequestParam(name = "size", required = false) Integer size) {

        if (page==null || size==null){
            page=0;
            size=5;
        }
        Pageable pageable = PageRequest.of(page,size);

        List<String> tags;
        if (tagString!=null){
            tags = Arrays.asList(tagString.split(" "));
        } else {
            tags=Collections.emptyList();
        }

        if (substring!=null)
            substring=substring.toLowerCase();
        tags=tags.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        ProgrammingLanguage lang=null;
        try {
            lang=ProgrammingLanguage.valueOf(language.toUpperCase());
            if (lang.equals(ProgrammingLanguage.NOT_SPECIFIED))
                lang=null;
        }catch (IllegalArgumentException | NullPointerException ignored){
            log.error(language);
        }

        if (lang==null){
            if (substring!=null){
                if (!tags.isEmpty()){
                    return ResponseEntity.ok(codeService.findAllByTagsAndSubstring(tags,substring, pageable));
                }
                return ResponseEntity.ok(codeService.findAllBySubstring(substring, pageable));
            }
            if (!tags.isEmpty()){
                return ResponseEntity.ok(codeService.findAllByTags(tags, pageable));
            }
            return ResponseEntity.badRequest().build();
        }
        if (substring==null && !tags.isEmpty()){
            return ResponseEntity.ok(codeService.findAllByTagsAndLanguage(tags, lang, pageable));
        } else if (substring!=null){
            return ResponseEntity.ok(codeService.findAllByLanguageAndSubstring(lang,substring, pageable));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/send-code")
    public ResponseEntity<Code> receiveCodeSample(@RequestParam(name = "language",required = false) String language,
                                                  @RequestParam(name = "code")String codeString,
                                                  @RequestParam(name = "tags",required = false) String tagString){

        if (codeString.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"code sample is empty");


        ProgrammingLanguage lang=ProgrammingLanguage.NOT_SPECIFIED;
        try {
            lang=ProgrammingLanguage.valueOf(language.toUpperCase());
        }catch (IllegalArgumentException | NullPointerException ignored){}
        Code code = Code.builder()
                .codeString(codeString)
                .language(lang)
                .build();

        if (tagString!=null){
            List<String> tags = Arrays.asList(tagString.split(" "));
            codeService.save(code,tags);
        } else {
            codeService.save(code,Collections.emptyList());
        }
        return ResponseEntity.ok(code);
    }

    @PostMapping("/delete/{id}")
    void deleteCodeEntity(@PathVariable("id") Long id){
        codeService.deleteById(id);
    }
}
