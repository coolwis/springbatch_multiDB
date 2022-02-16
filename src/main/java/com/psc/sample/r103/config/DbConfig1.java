package com.psc.sample.r103.config;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.db1.datasource")
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory1",
        transactionManagerRef ="transactionManager1",
        basePackages = {"com.psc.sample.r103.db1"}

)
public class DbConfig1 extends HikariConfig {
    @Bean
    @Primary // Main db
    public DataSource dataSource1() {
        //arguments null ??
        return new LazyConnectionDataSourceProxy(new HikariDataSource(this));
    }

    @Bean (name="entityManagerFactory1")
    @Primary
    public EntityManagerFactory entityManagerFactory1(){
        JpaVendorAdapter vendorAdapter =new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factoryBean  = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(this.dataSource1());
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJpaPropertyMap(ImmutableMap.of(
"hibernate.hbm2ddl.auto", "update","hibernate.dialect",
                "org.hibernate.dialect.MySQL5InnoDBDialect",
                "hibernate.show_sql", "true"
        ));
        factoryBean.setPackagesToScan("com.psc.sample.r103.db1");
        factoryBean.setPersistenceUnitName("db1");
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager1(){
        JpaTransactionManager tm =new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory1());
        return  tm;
    }
}
