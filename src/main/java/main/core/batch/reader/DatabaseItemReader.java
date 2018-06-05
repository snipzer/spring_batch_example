package main.core.batch.reader;

import main.core.entity.Person;
import main.core.utils.QueryUtils;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseItemReader extends JdbcCursorItemReader<Person> {

    private DataSource _dataSourceIn;
    private String _sql;

    DatabaseItemReader() {
        _sql = QueryUtils.READ_TABLE_IN;
    }

    @Autowired
    public void setDataSourceIn(DataSource dataSourceIn) {
        _dataSourceIn = dataSourceIn;
    }



}
