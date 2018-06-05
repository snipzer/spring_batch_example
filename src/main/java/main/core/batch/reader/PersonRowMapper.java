package main.core.batch.reader;

import main.core.entity.Person;
import main.core.utils.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setFirstName(rs.getString(StringUtils.COLUMN_FIRST_NAME));
        person.setLastName(rs.getString(StringUtils.COLUMN_LAST_NAME));
        return person;
    }
}
