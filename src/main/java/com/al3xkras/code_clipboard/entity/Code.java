package com.al3xkras.code_clipboard.entity;

import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "code_samples")
@Profile("hibernate")
public class Code implements Serializable {
    @Id
    @Column(name = "code_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codeId;
    @Column(name = "code_string", columnDefinition = "nvarchar(3000)", nullable = false)
    private String codeString;
    @Column(name = "search_string", columnDefinition = "text(3000)",nullable = false)
    private String searchString;
    @Column(name = "tag_string", columnDefinition = "text(512)", nullable = false)
    private String tagString;
    @Column(name = "programming_language")
    @Enumerated(EnumType.STRING)
    private ProgrammingLanguage language;
    @Lob
    @Column(name = "code_image", columnDefinition = "mediumblob")
    private byte[] codeImage;
    @Column(name = "hide_code")
    private Boolean hideCodeText;
}
