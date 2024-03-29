package integration.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.mkrasikoff.contactbook.repos.JdbcPersonRepository;
import com.mkrasikoff.contactbook.services.GenerateService;

import javax.sql.DataSource;

@Configuration
public class IntegrationTestConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("sql/schema.sql")
                .addScript("sql/data.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public GenerateService generateService() {
        return new GenerateService();
    }

    @Bean
    public JdbcPersonRepository personRepository(JdbcTemplate jdbcTemplate, GenerateService generateService) {
        return new JdbcPersonRepository(jdbcTemplate, generateService);
    }
}
