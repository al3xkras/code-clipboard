package com.al3xkras.code_clipboard.repository.impl;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import com.al3xkras.code_clipboard.repository.CodeRepository;
import com.al3xkras.code_clipboard.repository.CodeRepositoryHibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("hibernate")
public class CodeRepositoryJpaImpl implements CodeRepository {

    @Autowired
    private CodeRepositoryHibernate codeRepositoryHibernate;

    public static List<String> validateTags(Collection<String> tags){
        return tags.stream().filter(t -> t.matches("^[a-zA-Z0-9 ]*$") && !t.isBlank())
                .map(x -> x.replaceAll(" ", "_")).toList();
    }

    public static String codeToSearchString(String code){
        return code.replaceAll("[^a-zA-Z\\d\\s]","").toLowerCase();
    }

    @PostConstruct
    void postConstruct(){
        ResourceBundle bundle = ResourceBundle.getBundle("application-hibernate");
        String username=bundle.getString("spring.datasource.username");
        String password=bundle.getString("spring.datasource.password");
        String url=bundle.getString("spring.datasource.url");
        try (Connection conn = DriverManager.getConnection(url,username,password);
             PreparedStatement ps1 = conn.prepareStatement("ALTER TABLE code_samples " +
                     "ADD FULLTEXT(search_string);");
             PreparedStatement ps2 = conn.prepareStatement("ALTER TABLE code_samples " +
                     "ADD FULLTEXT(tag_string);")){
            ps1.execute();
            ps2.execute();
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
    public List<Code> findAllByTags(Collection<String> tags) {
        List<String> validTags = validateTags(tags).stream().map(x->"+"+x)
                .collect(Collectors.toList());
        if (validTags.isEmpty()){
            return Collections.emptyList();
        }
        return codeRepositoryHibernate.findAllByTags(String.join(" ",validTags));
    }

    @Override
    public List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language) {
        List<String> validTags = validateTags(tags).stream().map(x->"+"+x)
                .collect(Collectors.toList());
        if (validTags.isEmpty()){
            return Collections.emptyList();
        }
        return codeRepositoryHibernate.findAllByTagsAndLanguage(String.join(" ",validTags), language);
    }

    @Override
    public List<Code> findAllBySubstring(String substring) {
        if (substring.isBlank()){
            return Collections.emptyList();
        }
        return codeRepositoryHibernate.findAllBySubstringInBooleanMode("*"+substring+"*");
    }

    @Override
    public List<Code> findAllByLanguageAndSubstring(ProgrammingLanguage language, String substring) {
        if (substring.isBlank()){
            return Collections.emptyList();
        }
        return codeRepositoryHibernate.findAllByLanguageAndSubstring(language,"*"+substring+"*");
    }
}
