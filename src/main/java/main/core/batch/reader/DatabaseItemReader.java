package main.core.batch.reader;

import main.core.entity.Person;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseItemReader extends JdbcCursorItemReader<Person> {

    private static final String READ_TABLE_IN = "SELECT first_name, last_name FROM person_in";

    public DatabaseItemReader() {
        super();
    }

    @Autowired
    public void setDataSourceIn(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Autowired
    public void setRowMapper(PersonRowMapper personRowMapper) {
        super.setRowMapper(personRowMapper);
    }

    @Autowired
    public void setSql() {
        super.setSql(READ_TABLE_IN);
    }

}
