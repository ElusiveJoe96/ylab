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

public class LiquibaseConfig {
    private final DatabaseConfig databaseConfig;
    private final Properties properties;

    public LiquibaseConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
        this.properties = databaseConfig.loadProperties();
    }

    public void runMigrations() {
        try (Connection connection = databaseConfig.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            String changeLogTableSchema = properties.getProperty("liquibase.change-log-table-schema",
                    "public");

            database.setDefaultSchemaName(changeLogTableSchema);

            try (Statement stmt = connection.createStatement()) {
                String createSchemaSQL = "CREATE SCHEMA IF NOT EXISTS car_shop_log_schema";
                stmt.execute(createSchemaSQL);
            }

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
