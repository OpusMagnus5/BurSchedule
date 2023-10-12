package pl.bodzioch.damian;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import pl.bodzioch.damian.client.conf.CustomRestTemplateCustomizer;
import pl.bodzioch.damian.controller.conf.CustomRequestLoggingFilter;

import javax.sql.DataSource;
import java.util.Locale;

@EntityScan(basePackages = {"pl.bodzioch.damian.entity"})
@EnableJpaRepositories(basePackages = "pl.bodzioch.damian.dao.impl")
@EnableTransactionManagement
@SpringBootApplication
@RequestMapping("/app")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CustomRestTemplateCustomizer customRestTemplateCustomizer() {
        return new CustomRestTemplateCustomizer();
    }

    @Bean
    @DependsOn(value = "customRestTemplateCustomizer")
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder(customRestTemplateCustomizer());
    }

    @Bean
    public LocaleResolver localeResolver() {
        FixedLocaleResolver resolver = new FixedLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("pl"));
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:i18n/messages");
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        reloadableResourceBundleMessageSource.setUseCodeAsDefaultMessage(false);
        return reloadableResourceBundleMessageSource;
    }

    @Bean
    public CustomRequestLoggingFilter logFilter() {
        CustomRequestLoggingFilter filter = new CustomRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setBeforeMessagePrefix("REQUEST DATA: ");
        return filter;
    }

    @Bean
    @Profile("local")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource localDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Profile("docker")
    @ConfigurationProperties(prefix = "docker.spring.datasource")
    public DataSource dockerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }
}