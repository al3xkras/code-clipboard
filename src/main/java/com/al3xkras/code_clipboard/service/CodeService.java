package com.al3xkras.code_clipboard.service;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import com.al3xkras.code_clipboard.repository.CodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
    public List<Code> findAllByTags(Collection<String> tags, Pageable pageable){
        return codeRepository.findAllByTags(tags,pageable);
    }
    public List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language,
                                               Pageable pageable){
        return codeRepository.findAllByTagsAndLanguage(tags,language,pageable);
    }
    public List<Code> findAllBySubstring(String substring, Pageable pageable, Object... additionalArgs){
        return codeRepository.findAllBySubstring(substring,pageable,additionalArgs);
    }
    public List<Code> findAllByLanguageAndSubstring(ProgrammingLanguage language, String substring, Pageable pageable, Object... additionalArgs){
        return codeRepository.findAllByLanguageAndSubstring(language,substring,pageable,additionalArgs);
    }
    public List<Code> findAllByTagsAndSubstring(List<String> tags, String substring, Pageable pageable, Object... additionalArgs) {
        return codeRepository.findAllByTagsAndSubstring(tags,substring,pageable,additionalArgs);
    }

}
