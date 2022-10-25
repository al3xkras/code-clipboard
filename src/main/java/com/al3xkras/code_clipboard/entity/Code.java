package com.al3xkras.code_clipboard.entity;

import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
    @Column(name = "code_string", columnDefinition = "nvarchar(2000)", nullable = false)
    private String codeString;
    @Column(name = "search_string", columnDefinition = "text(2048)",nullable = false)
    private String searchString;
    @Enumerated(EnumType.STRING)
    @Column(name = "programming_language")
    private ProgrammingLanguage language;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "code_tags",joinColumns = @JoinColumn(name = "code_id",referencedColumnName = "code_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id",referencedColumnName = "tag_id"))
    private List<Tag> tags;
}
