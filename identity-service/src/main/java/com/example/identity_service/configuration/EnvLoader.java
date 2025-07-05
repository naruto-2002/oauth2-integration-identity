package com.example.identity_service.configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./identity-service") // thư mục gốc của project
                .filename(".env") // tên file
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}
