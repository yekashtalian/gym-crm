package org.example.gymcrm.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class JpaConfig {

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl("jdbc:postgresql://localhost:5432/gym-crm");
    dataSource.setUsername("postgres");
    dataSource.setPassword("postgres");
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("org.example.gymcrm.entity");
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    properties.setProperty("hibernate.show_sql", "true");
    properties.setProperty("hibernate.format_sql", "true");
    properties.setProperty("hibernate.highlight_sql", "true");
//    properties.setProperty("hibernate.hbm2ddl.auto", "create");
    properties.setProperty(
        "hibernate.physical_naming_strategy",
        "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");

    em.setJpaProperties(properties);
    return em;
  }

  @Bean
  public JpaTransactionManager transactionManager(
      LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory.getObject());
  }
}
