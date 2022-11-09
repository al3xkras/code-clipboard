package com.al3xkras.code_clipboard.repository;

import com.al3xkras.code_clipboard.entity.Code;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("hibernate")
public interface CodeRepositoryHibernate extends JpaRepository<Code,Long> {

    @Query(nativeQuery = true, value = "select * from code_samples where match(tag_string) against (?1 in boolean mode)")
    List<Code> findAllByTags(String tags, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from code_samples where programming_language=?2 and match(tag_string) against (?1 in boolean mode)")
    List<Code> findAllByTagsAndLanguage(String tags, String language, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from code_samples where match(search_string) against (?1 in boolean mode)")
    List<Code> findAllBySubstringInBooleanMode(String substring, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from code_samples where match(search_string) against (?1 in natural language mode with query expansion)")
    List<Code> findAllBySubstringInNaturalLanguageMode(String substring, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from code_samples where programming_language=?1 and match(search_string) against (?2 in boolean mode)")
    List<Code> findAllByLanguageAndSubstringInBooleanMode(String language, String substring, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from code_samples where programming_language=?1 and match(search_string) against (?2 in natural language mode with query expansion)")
    List<Code> findAllByLanguageAndSubstringInNaturalLanguageMode(String language, String substring, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from code_samples where match(tag_string) against (?1 in boolean mode) and match(search_string) against (?2 in boolean mode)")
    List<Code> findAllByTagsAndSubstringInBooleanMode(String tags, String substring, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from code_samples where match(tag_string) against (?1 in boolean mode) and match(search_string) against (?2 in natural language mode with query expansion)")
    List<Code> findAllByTagsAndSubstringInNaturalLanguageMode(String tags, String substring, Pageable pageable);
}
