package com.al3xkras.code_clipboard.repository;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CodeRepository {
    Optional<Code> findById(Long id);
    Code save(Code code,List<String> tags);
    Code deleteById(Long id);
    List<Code> findAllByTags(Collection<String> tags, Pageable pageable);
    List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language, Pageable pageable);
    List<Code> findAllBySubstring(String substring, Pageable pageable);
    default List<Code> findAllByTagsAndSubstring(Collection<String> tags, String substring, Pageable pageable){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"not implemented");
    }
    default void load(){};
    default void serialize() throws IOException{};
    List<Code> findAllByLanguageAndSubstring(ProgrammingLanguage language, String substring, Pageable pageable);
}
