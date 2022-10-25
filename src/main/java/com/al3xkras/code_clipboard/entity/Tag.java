package com.al3xkras.code_clipboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = "value"))
@Profile("hibernate")
public class Tag {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "value",columnDefinition = "varchar(32)", nullable = false)
    private String value;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    private List<Code> codeSamples;
}
