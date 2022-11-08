package com.al3xkras.code_clipboard.model;

public enum ProgrammingLanguage {
    NOT_SPECIFIED("Any"),
    JAVA("Java"),
    KOTLIN("Kotlin"),
    JS("JavaScript"),
    PYTHON("python"),
    C("C"),
    CPP("C++"),
    CS("C#"),
    GOLANG("Go"),
    TS("TypeScript"),
    RUST("Rust"),
    RUBY("Ruby"),
    CSS("CSS"),
    XML("XML"),
    HTML("HTML"),
    MARKDOWN("Markdown"),
    LATEX("LaTeX"),
    PHP("PHP"),
    ERLANG("Erlang"),
    CMD("Batchfile"),
    BASH("bash"),
    ASSEMBLY("Assembly");

    private String prettyName;
    ProgrammingLanguage(String prettyName){
        this.prettyName=prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }
}
