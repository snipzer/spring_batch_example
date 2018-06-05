package main.core.utils;

public class QueryUtils {
    public static final String INSERT_TABLE_IN = "INSERT INTO person_in (first_name, last_name) VALUES (:firstName, :lastName)";
    public static final String READ_TABLE_IN = "SELECT first_name, last_name FROM person_in";
    public static final String INSERT_TABLE_OUT = "INSERT INTO person_out (first_name, last_name) VALUES (:firstName, :lastName)";
}
