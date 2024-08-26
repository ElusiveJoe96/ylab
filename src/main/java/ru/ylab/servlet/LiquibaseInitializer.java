package ru.ylab.servlet;

import ru.ylab.config.DatabaseConfig;
import ru.ylab.config.LiquibaseConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class LiquibaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        LiquibaseConfig liquibaseConfig = new LiquibaseConfig(databaseConfig);
        liquibaseConfig.runMigrations();
    }
}
