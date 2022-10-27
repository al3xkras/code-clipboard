package com.al3xkras.code_clipboard.repository.impl;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import com.al3xkras.code_clipboard.repository.CodeRepository;
import com.al3xkras.code_clipboard.repository.CodeRepositoryHibernate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Profile("hibernate")
public class CodeRepositoryJpaImpl implements CodeRepository {

    private static final int MYSQL_DUPLICATE_KEY_ERROR = 1061;

    @Autowired
    private CodeRepositoryHibernate codeRepositoryHibernate;

    public static List<String> validateTags(Collection<String> tags){
        return tags.stream().filter(t -> t.matches("^[a-zA-Z0-9 _\\-:/.!+]*$") && !t.isBlank())
                .map(x -> x.replaceAll(" ", "_")).toList();
    }

    public static List<String> parseTags(Collection<String> tags){
        return validateTags(tags).stream().map(x->"+*"+x+"*")
                .collect(Collectors.toList());
    }

    public static String parseSearchString(String string){
        String words = codeToSearchString(string);
        if (words.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return Arrays.stream(words.split(" ")).map(x->"*"+x+"*").collect(Collectors.joining(" "));
    }

    public static String codeToSearchString(String code){
        return code.replaceAll("[^a-zA-Z\\d\\s]"," ").toLowerCase();
    }

    @PostConstruct
    void postConstruct(){
        ResourceBundle bundle = ResourceBundle.getBundle("application-hibernate");
        String username=bundle.getString("spring.datasource.username");
        String password=bundle.getString("spring.datasource.password");
        String url=bundle.getString("spring.datasource.url");
        try (Connection conn = DriverManager.getConnection(url,username,password);
             PreparedStatement ps1 = conn.prepareStatement("ALTER TABLE code_samples " +
                     "ADD FULLTEXT KEY search_fulltext (search_string);")){
            ps1.execute();
        } catch (SQLSyntaxErrorException e){
            if (e.getErrorCode()==MYSQL_DUPLICATE_KEY_ERROR){
                log.warn("fulltext index for column code_samples.\"search_string\" already exists");
            } else {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(url,username,password);
             PreparedStatement ps2 = conn.prepareStatement("ALTER TABLE code_samples " +
                     "ADD FULLTEXT KEY tag_fulltext (tag_string);")){
            ps2.execute();
        } catch (SQLSyntaxErrorException e){
            if (e.getErrorCode()==MYSQL_DUPLICATE_KEY_ERROR){
                log.warn("fulltext index for column code_samples.\"tag_string\" already exists");
            } else{
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Code> findById(Long id) {
        return codeRepositoryHibernate.findById(id);
    }

    @Override
    public Code save(Code code, List<String> tags) {
        List<String> validTags = validateTags(tags);
        code.setTagString(String.join(" ",validTags));
        code.setSearchString(CodeRepositoryJpaImpl.codeToSearchString(code.getCodeString()));
        return codeRepositoryHibernate.saveAndFlush(code);
    }

    @Override
    @Transactional
    public Code deleteById(Long id) {
        Code c = findById(id).orElse(null);
        if (c==null)
            return null;
        codeRepositoryHibernate.deleteById(id);
        return c;
    }

    @Override
    @Transactional
    public List<Code> findAllByTags(Collection<String> tags, Pageable pageable) {
        List<String> validTags = parseTags(tags);
        if (validTags.isEmpty()){
            return Collections.emptyList();
        }
        return codeRepositoryHibernate.findAllByTags(String.join(" ",validTags), pageable);
    }

    @Override
    public List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language, Pageable pageable) {
        List<String> validTags = parseTags(tags);
        if (validTags.isEmpty()){
            return Collections.emptyList();
        }
        return codeRepositoryHibernate.findAllByTagsAndLanguage(String.join(" ",validTags), language.name(), pageable);
    }

    @Override
    public List<Code> findAllBySubstring(String substring, Pageable pageable) {
        String searchString = parseSearchString(substring);
        return codeRepositoryHibernate.findAllBySubstringInBooleanMode(searchString, pageable);
    }

    @Override
    public List<Code> findAllByLanguageAndSubstring(ProgrammingLanguage language, String substring, Pageable pageable) {
        String searchString = parseSearchString(substring);
        return codeRepositoryHibernate.findAllByLanguageAndSubstring(language.name(), searchString, pageable);
    }

    @Override
    public List<Code> findAllByTagsAndSubstring(Collection<String> tags, String substring, Pageable pageable) {
        List<String> validTags = parseTags(tags);
        String searchString = parseSearchString(substring);
        return codeRepositoryHibernate.findAllByTagsAndSubstring(String.join(" ",validTags), searchString, pageable);
    }
}
