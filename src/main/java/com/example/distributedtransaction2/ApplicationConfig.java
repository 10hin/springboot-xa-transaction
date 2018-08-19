package com.example.distributedtransaction2;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.example.distributedtransaction2.mysql.MySQLConfig;
import com.example.distributedtransaction2.postgresql.PostgreSQLConfig;
import com.mysql.cj.jdbc.MysqlXADataSource;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
@Slf4j
public class ApplicationConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.db.mysql.datasource")
    public DataSourceProperties mySQLDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "app.db.postgresql.datasource")
    public DataSourceProperties postgreSQLDataSourceProperties() {
        return new DataSourceProperties();
    }

    @DependsOn("transactionManager")
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource mySQLXADataSource( //
            @Autowired //
            @Qualifier("mySQLDataSourceProperties") //
            final DataSourceProperties mySQLDataSourceProperties) {
        final MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(mySQLDataSourceProperties.determineUrl());
        mysqlXADataSource.setUser(mySQLDataSourceProperties.determineUsername());
        mysqlXADataSource.setPassword(mySQLDataSourceProperties.determinePassword());

        final AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXADataSource);
        xaDataSource.setUniqueResourceName("mySQLXaDataSource");
        return xaDataSource;

    }

    @Primary
    @DependsOn("transactionManager")
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource postgreSQLXADataSource( //
            @Autowired //
            @Qualifier("postgreSQLDataSourceProperties") //
            final DataSourceProperties postgreSQLDataSourceProperties) {
        final PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(postgreSQLDataSourceProperties.determineUrl());
        pgxaDataSource.setUser(postgreSQLDataSourceProperties.determineUsername());
        pgxaDataSource.setPassword(postgreSQLDataSourceProperties.determinePassword());

        final AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(pgxaDataSource);
        xaDataSource.setUniqueResourceName("postgreSQLXaDataSource");
        return xaDataSource;

    }

    @Bean
    @ConfigurationProperties(prefix = "app.db.mysql.jpa")
    public JpaProperties mySQLJpaProperties() {
        return new JpaProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "app.db.postgresql.jpa")
    public JpaProperties postgreSQLJpaProperties() {
        return new JpaProperties();
    }

    @DependsOn("transactionManager")
    @Bean
    public LocalContainerEntityManagerFactoryBean mySQLEntityManagerFactoryBean( //
            @Autowired //
            @Qualifier("mySQLJpaProperties") //
            final JpaProperties mySQLJpaProperties, //
            @Autowired //
            @Qualifier("mySQLXADataSource") //
            final DataSource mySQLXADataSource) {
        final LocalContainerEntityManagerFactoryBean mySQLEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        mySQLEntityManagerFactoryBean.setJtaDataSource(mySQLXADataSource);
        mySQLEntityManagerFactoryBean.setPackagesToScan(MySQLConfig.class.getPackage().getName());
        mySQLEntityManagerFactoryBean.setPersistenceUnitName("mySQLPersistenceUnit");

        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        if (mySQLJpaProperties.getDatabase() != null) {
            adapter.setDatabase(mySQLJpaProperties.getDatabase());
        }
        if (mySQLJpaProperties.getDatabasePlatform() != null) {
            adapter.setDatabasePlatform(mySQLJpaProperties.getDatabasePlatform());
        }
        adapter.setGenerateDdl(mySQLJpaProperties.isGenerateDdl());
        adapter.setShowSql(mySQLJpaProperties.isShowSql());
        mySQLEntityManagerFactoryBean.setJpaVendorAdapter(adapter);

        final HibernateSettings settings = new HibernateSettings() //
                .ddlAuto(() -> "none");
        mySQLEntityManagerFactoryBean.setJpaPropertyMap(mySQLJpaProperties.getHibernateProperties(settings));

        return mySQLEntityManagerFactoryBean;
    }

    @Primary
    @DependsOn("transactionManager")
    @Bean
    public LocalContainerEntityManagerFactoryBean postgreSQLEntityManagerFactoryBean( //
            @Autowired //
            @Qualifier("postgreSQLJpaProperties") //
            final JpaProperties postgreSQLJpaProperties, //
            @Autowired //
            @Qualifier("postgreSQLXADataSource") //
            final DataSource postgreSQLDataSource) {
        final LocalContainerEntityManagerFactoryBean postgreSQLEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        postgreSQLEntityManagerFactoryBean.setDataSource(postgreSQLDataSource);
        postgreSQLEntityManagerFactoryBean.setPackagesToScan(PostgreSQLConfig.class.getPackage().getName());
        postgreSQLEntityManagerFactoryBean.setPersistenceUnitName("postgreSQLPersistenceUnit");

        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        if (postgreSQLJpaProperties.getDatabase() != null) {
            adapter.setDatabase(postgreSQLJpaProperties.getDatabase());
        }
        if (postgreSQLJpaProperties.getDatabasePlatform() != null) {
            adapter.setDatabasePlatform(postgreSQLJpaProperties.getDatabasePlatform());
        }
        adapter.setGenerateDdl(postgreSQLJpaProperties.isGenerateDdl());
        adapter.setShowSql(postgreSQLJpaProperties.isShowSql());
        postgreSQLEntityManagerFactoryBean.setJpaVendorAdapter(adapter);

        final HibernateSettings settings = new HibernateSettings() //
                .ddlAuto(() -> "none");
        postgreSQLEntityManagerFactoryBean.setJpaPropertyMap(postgreSQLJpaProperties.getHibernateProperties(settings));

        return postgreSQLEntityManagerFactoryBean;
    }

    @Bean
    public UserTransaction userTransaction() {
        final UserTransaction userTransaciton =  new UserTransactionImp();
        AtomikosJtaPlatform.transaction = userTransaciton;
        return userTransaciton;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public TransactionManager atomikosTransactionManager() {

        final UserTransactionManager userTransactionManager = new UserTransactionManager();
        AtomikosJtaPlatform.transactionManager = userTransactionManager;

        return userTransactionManager;

    }

    @Bean
    public PlatformTransactionManager transactionManager( //
            @Autowired //
            @Qualifier("userTransaction") //
            final UserTransaction userTransaction, //
            @Autowired //
            @Qualifier("atomikosTransactionManager") //
            final TransactionManager atomikosTransactionManager) {

        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);

    }

}
