package com.al3xkras.code_clipboard.model;

public enum ProgrammingLanguage {
    NOT_SPECIFIED("Any"),
    JAVA("Java"),
    JS("JavaScript"),
    CSS("CSS"),
    PYTHON("python"),
    C("c"),
    CPP("c++"),
    XML("XML"),
    HTML("html"),
    YML("yaml");

    private String prettyName;
    ProgrammingLanguage(String prettyName){
        this.prettyName=prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }
}
