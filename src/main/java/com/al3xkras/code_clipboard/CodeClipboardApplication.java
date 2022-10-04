package com.al3xkras.code_clipboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@SpringBootApplication
public class CodeClipboardApplication {
	public static final String delimiter="\u0001";

	public static void main(String[] args) throws URISyntaxException, IOException {
		SpringApplication.run(CodeClipboardApplication.class, args);

		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")){
			Runtime rt = Runtime.getRuntime();
			String url = "http://localhost:10000";
			rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
		}

	}

}
