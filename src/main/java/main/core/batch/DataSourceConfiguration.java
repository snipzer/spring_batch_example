package main.core.batch;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    private Environment _env;

    @Bean
    public DataSource dataSourceOut() {
        return new HikariDataSource(createDataSourceConfig("out"));
    }

    @Bean
    public DataSource dataSourceIn() {
        return new HikariDataSource(createDataSourceConfig("in"));
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(createDataSourceConfig("batch"));
    }

    private HikariConfig createDataSourceConfig(String origin) {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("com.mysql.jdbc.Driver");
        dataSourceConfig.setJdbcUrl(_env.getProperty("datasource."+origin+".url"));
        dataSourceConfig.setUsername(_env.getProperty("datasource."+origin+".username"));
        dataSourceConfig.setPassword(_env.getProperty("datasource."+origin+".password"));
        dataSourceConfig.setAutoCommit(Boolean.valueOf(_env.getProperty("datasource."+origin+".autocommit")));
        dataSourceConfig.addDataSourceProperty("cachePrepStmts", _env.getProperty("datasource."+origin+".cachePrepStmts"));
        dataSourceConfig.addDataSourceProperty("prepStmtCacheSize", _env.getProperty("datasource."+origin+".prepStmtCacheSize"));
        dataSourceConfig.addDataSourceProperty("prepStmtCacheSqlLimit", _env.getProperty("datasource."+origin+".prepStmtCacheSqlLimit"));
        dataSourceConfig.addDataSourceProperty("useServerPrepStmts", _env.getProperty("datasource."+origin+".useServerPrepStmts"));
        dataSourceConfig.addDataSourceProperty("useUnicode", _env.getProperty("datasource."+origin+".useUnicode"));
        dataSourceConfig.addDataSourceProperty("characterEncoding", _env.getProperty("datasource."+origin+".characterEncoding"));
        dataSourceConfig.addDataSourceProperty("connectionCollation", _env.getProperty("datasource."+origin+".connectionCollation"));
        return dataSourceConfig;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setEnv(Environment environment) {
        _env = environment;
    }
}
