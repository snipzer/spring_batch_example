package main.core.batch;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSourceOut() {
        return new HikariDataSource(createDataSourceConfig("batch_test_out"));
    }

    @Bean
    public DataSource dataSourceIn() {
        return new HikariDataSource(createDataSourceConfig("batch_test_in"));
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(createDataSourceConfig("batch_test"));
    }

    private HikariConfig createDataSourceConfig(String tableName) {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("com.mysql.jdbc.Driver");
        dataSourceConfig.setJdbcUrl("jdbc:mysql://localhost/" + tableName + "?autoReconnect=true&useSSL=false");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("");
        dataSourceConfig.setAutoCommit(false);
        dataSourceConfig.addDataSourceProperty("cachePrepStmts", "true");
        dataSourceConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        dataSourceConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSourceConfig.addDataSourceProperty("useServerPrepStmts", "true");
        dataSourceConfig.addDataSourceProperty("useUnicode", "yes");
        dataSourceConfig.addDataSourceProperty("characterEncoding", "UTF-8");
        dataSourceConfig.addDataSourceProperty("connectionCollation", "utf8_general_ci");
        return dataSourceConfig;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
