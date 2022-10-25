package com.al3xkras.code_clipboard.repository.impl;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.entity.Tag;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import com.al3xkras.code_clipboard.repository.CodeRepository;
import com.al3xkras.code_clipboard.repository.CodeRepositoryHibernate;
import com.al3xkras.code_clipboard.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Repository
@Profile("hibernate")
public class CodeRepositoryJpaImpl implements CodeRepository {

    @Autowired
    private CodeRepositoryHibernate codeRepositoryHibernate;
    @Autowired
    private TagRepository tagRepository;

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
             PreparedStatement ps = conn.prepareStatement("ALTER TABLE code_samples " +
                     "ADD FULLTEXT(search_string);")){
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Code> findById(Long id) {
        return codeRepositoryHibernate.findById(id);
    }

    @Override
    public Code save(Code code, List<String> tags) {
        List<Tag> tagList = tags.stream().map(s->Tag.builder().value(s).build())
                        .collect(Collectors.toList());
        code.setTags(tagList);
        code.setSearchString(CodeRepositoryJpaImpl.codeToSearchString(code.getCodeString()));
        return codeRepositoryHibernate.saveAndFlush(code);
    }

    @Override
    public Code deleteById(Long id) {
        return codeRepositoryHibernate.deleteByCodeId(id);
    }

    @Override
    @Transactional
    public List<Code> findAllByTags(Collection<String> tags) {
        List<Tag> tagList = tagRepository.findAllByValueIn(tags);
        return codeRepositoryHibernate.findAllByTags(tagList);
    }

    @Override
    public List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language) {
        List<Tag> tagList = tagRepository.findAllByValueIn(tags);
        return codeRepositoryHibernate.findAllByTagsAndLanguage(tagList, language);
    }

    @Override
    public List<Code> findAllBySubstring(String substring) {
        return codeRepositoryHibernate.findAllBySubstring(substring);
    }

    @Override
    public List<Code> findAllByLanguageAndSubstring(ProgrammingLanguage language, String substring) {
        return codeRepositoryHibernate.findAllByLanguageAndSubstring(language,substring);
    }
}
