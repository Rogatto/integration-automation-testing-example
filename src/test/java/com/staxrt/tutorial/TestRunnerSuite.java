package com.staxrt.tutorial;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;

@RunWith(Suite.class)
@Suite.SuiteClasses({ApplicationTests.class})
public class TestRunnerSuite {

    @ClassRule
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7.31")
            .withUsername("root")
            .withPassword("root")
            .withDatabaseName("users_database");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + mysql.getJdbcUrl() + "?useSSL=false",
                    "spring.datasource.username=" + mysql.getUsername(),
                    "spring.datasource.password=" + mysql.getPassword(),
                    "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect",
                    "spring.jpa.hibernate.ddl-auto = update"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}