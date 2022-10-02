package com.al3xkras.code_clipboard.repository;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;

import java.util.Collection;
import java.util.List;

public interface CodeRepository {
    Code save(Code code);
    void delete(Code code);
    void deleteById(Long id);
    List<Code> findAllByTags(Collection<String> tags);
    List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language);
    List<Code> findAllBySubstring(String substring);
}
