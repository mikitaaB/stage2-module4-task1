package com.mjc.stage2.impl;

import com.mjc.stage2.ConnectionFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class H2ConnectionFactory implements ConnectionFactory {
    private final Properties properties = new Properties();

    @Override
    public Connection createConnection() {
        ClassLoader loader = H2ConnectionFactory.class.getClassLoader();
        try (InputStream stream = loader.getResourceAsStream("h2database.properties")) {
            if (stream == null) {
                throw new FileNotFoundException();
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load h2database.properties", e);
        }

        String driver = properties.getProperty("jdbc_driver");
        String url = properties.getProperty("db_url");
        String password = properties.getProperty("password");
        String name = properties.getProperty("user");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.fillInStackTrace();
        }

        try {
            return DriverManager.getConnection(url, name, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

