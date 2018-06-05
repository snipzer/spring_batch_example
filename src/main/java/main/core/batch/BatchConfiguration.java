package main.core.batch;

import main.core.batch.listener.JobCompletionNotificationListener;
import main.core.batch.processor.PersonItemProcessor;
import main.core.batch.reader.DatabaseItemReader;
import main.core.entity.Person;
import main.core.utils.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final String INSERT_TABLE_OUT = "INSERT INTO person_out (first_name, last_name) VALUES (:firstName, :lastName)";

    private JobBuilderFactory _jobBuilderFactory;

    private StepBuilderFactory _stepBuilderFactory;

    private String[] _entityStructure;


    public BatchConfiguration() {
        _entityStructure = new String[]{StringUtils.FIRST_NAME, StringUtils.LAST_NAME};
    }

    private FlatFileItemReader<Person> reader(String pathToData) {
        FlatFileItemReaderBuilder<Person> itemReaderBuilder = new FlatFileItemReaderBuilder<>();
        itemReaderBuilder.name(StringUtils.PERSON_ITEM_READER);
        itemReaderBuilder.resource(new ClassPathResource(pathToData));
        itemReaderBuilder.delimited().delimiter(StringUtils.SEMICOLON).names(_entityStructure).fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
            setTargetType(Person.class);
        }});
        return itemReaderBuilder.build();
    }

    private PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSourceOut) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_TABLE_OUT)
                .dataSource(dataSourceOut)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step) {
        return _jobBuilderFactory.get(StringUtils.IMPORT_USER_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(JdbcBatchItemWriter<Person> writer, DataSource dataSourceIn, RowMapper<Person> personRowMapper) {
        DatabaseItemReader reader = new DatabaseItemReader();
        reader.setDataSourceIn(dataSourceIn);
        reader.setRowMapper(personRowMapper);
        reader.setSql();
        return _stepBuilderFactory.get(StringUtils.TRANSFERT)
                .<Person, Person>chunk(10)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        _jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        _stepBuilderFactory = stepBuilderFactory;
    }
}
