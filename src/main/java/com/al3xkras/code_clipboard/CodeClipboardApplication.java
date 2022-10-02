package com.al3xkras.code_clipboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeClipboardApplication {
	public static final String delimiter="\u058C";

	public static void main(String[] args) {
		SpringApplication.run(CodeClipboardApplication.class, args);
	}

}
