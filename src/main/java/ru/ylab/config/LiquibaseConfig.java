package ru.ylab.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Configuration class for managing Liquibase database migrations.
 * Sets up and executes Liquibase migrations using the provided {@link DatabaseConfig}.
 */
public class LiquibaseConfig {
    private final DatabaseConfig databaseConfig;
    private final Properties properties;

    /**
     * Constructs a {@link LiquibaseConfig} instance with the specified {@link DatabaseConfig}.
     * Initializes the properties from the provided {@link DatabaseConfig}.
     *
     * @param databaseConfig the {@link DatabaseConfig} instance used to obtain database connection details
     */
    public LiquibaseConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
        this.properties = databaseConfig.loadProperties("liquibase.properties");
    }

    /**
     * Runs database migrations using Liquibase.
     * - Establishes a connection to the database.
     * - Creates the schema if it does not exist.
     * - Sets the default schema name for Liquibase.
     * - Loads and applies the changes specified in the Liquibase changelog.
     *
     * @throws RuntimeException if an error occurs during migration or database setup
     */
    public void runMigrations() {
        try (Connection connection = databaseConfig.getConnection();
             Database database = DatabaseFactory.getInstance()
                     .findCorrectDatabaseImplementation(new JdbcConnection(connection));
             Statement stmt = connection.createStatement()) {
            String createSchemaSQL = "CREATE SCHEMA IF NOT EXISTS car_shop_schema";
            stmt.execute(createSchemaSQL);

            String changeLogTableSchema = properties.getProperty("liquibase.change-log-table-schema",
                    "public");

            database.setDefaultSchemaName(changeLogTableSchema);

            String changeLog = properties.getProperty("liquibase.change-log",
                    "db/changelog/changelog.xml");

            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());
            Liquibase liquibase = new Liquibase(changeLog, resourceAccessor, database);

            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException("Failed to run Liquibase migrations", e);
        }
    }
}
