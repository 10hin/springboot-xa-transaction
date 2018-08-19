package com.example.distributedtransaction2.postgresql;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories( //
        basePackageClasses = {PostgreSQLConfig.class}, //
        entityManagerFactoryRef = "postgreSQLEntityManagerFactoryBean", //
        transactionManagerRef = "transactionManager")
public class PostgreSQLConfig {

}
