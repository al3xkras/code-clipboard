package com.al3xkras.code_clipboard.repository;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CodeRepository {

    Optional<Code> findById(Long id);
    Code save(Code code,List<String> tags);
    Code deleteById(Long id);
    List<Code> findAllByTags(Collection<String> tags);
    List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language);
    List<Code> findAllBySubstring(String substring);
    default void load(){};
    default void serialize() throws IOException{};
    List<Code> findAllByLanguageAndSubstring(ProgrammingLanguage language, String substring);
}
