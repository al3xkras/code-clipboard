package com.al3xkras.code_clipboard.entity;

import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Code {
    private Long id;
    private String codeString;
    private ProgrammingLanguage language;
}
