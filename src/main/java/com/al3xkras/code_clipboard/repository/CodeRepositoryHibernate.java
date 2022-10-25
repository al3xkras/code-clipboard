package com.al3xkras.code_clipboard.repository;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.entity.Tag;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("hibernate")
public interface CodeRepositoryHibernate extends JpaRepository<Code,Long> {
    Code deleteByCodeId(Long id);
    @Query(value = "select c from Code c where ?1 in c.tags")
    List<Code> findAllByTags(List<Tag> tags);

    @Query(value = "select c from Code c where c.language=?2 and ?1 in c.tags")
    List<Code> findAllByTagsAndLanguage(List<Tag> tagList, ProgrammingLanguage language);

    @Query(nativeQuery = true, value = "select * from code_samples where match(search_string) against (?1 in natural language mode)")
    List<Code> findAllBySubstring(String substring);

    @Query(nativeQuery = true, value = "select * from code_samples where match(search_string) against (?1 in boolean mode)")
    List<Code> findAllBySubstringInBooleanMode(String substring);

    @Query(nativeQuery = true, value = "select * from code_samples where programming_language=?1 and match(search_string) against (?2 in natural language mode)")
    List<Code> findAllByLanguageAndSubstring(ProgrammingLanguage language, String substring);
}
