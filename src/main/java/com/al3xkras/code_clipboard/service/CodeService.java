package com.al3xkras.code_clipboard.service;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import com.al3xkras.code_clipboard.repository.CodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CodeService {
    @Autowired
    private CodeRepository codeRepository;

    public Optional<Code> findById(Long id){
        return codeRepository.findById(id);
    }
    public Code save(Code code, List<String> tags){
        return codeRepository.save(code,tags);
    }
    public Code deleteById(Long id){
        return codeRepository.deleteById(id);
    }
    public List<Code> findAllByTags(Collection<String> tags){
        return codeRepository.findAllByTags(tags);
    }
    public List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language){
        return codeRepository.findAllByTagsAndLanguage(tags,language);
    }
    public List<Code> findAllBySubstring(String substring){
        return codeRepository.findAllBySubstring(substring);
    }
    public List<Code> findAllByLanguageAndSubstring(ProgrammingLanguage language,String substring){
        return codeRepository.findAllByLanguageAndSubstring(language,substring);
    }
}
