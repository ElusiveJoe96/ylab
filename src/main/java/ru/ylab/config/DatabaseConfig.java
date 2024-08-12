package ru.ylab.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database configuration class.
 * Provides methods to load database settings and establish a connection to the database.
 */
public class DatabaseConfig {
    private final String url;
    private final String username;
    private final String password;

    /**
     * Default constructor.
     * Loads properties from the {@code application.properties} configuration file,
     * and initializes the database URL, username, and password.
     */
    public DatabaseConfig() {
        Properties properties = loadProperties();
        this.url = properties.getProperty("database.url");
        this.username = properties.getProperty("database.username");
        this.password = properties.getProperty("database.password");
        loadJdbcDriver(properties.getProperty("database.driver-class-name"));
    }

    /**
     * Constructor with parameters.
     * Initializes the database URL, username, and password with the provided values.
     *
     * @param url      the database URL
     * @param username the database username
     * @param password the database password
     */
    public DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Loads database properties from the {@code application.properties} configuration file.
     *
     * @return a {@link Properties} object containing database settings
     * @throws RuntimeException if the configuration file cannot be found or loaded
     */
    public Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new IOException("Unable to find application.properties");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load properties", ex);
        }
        return properties;
    }

    /**
     * Loads the JDBC driver class specified by the driver class name from the properties.
     *
     * @param driverClassName the fully qualified name of the JDBC driver class
     * @throws RuntimeException if the JDBC driver class cannot be loaded
     */
    private void loadJdbcDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    /**
     * Establishes a connection to the database using the configured settings.
     *
     * @return a {@link Connection} object for connecting to the database
     * @throws SQLException if a database access error occurs or the URL is null
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
