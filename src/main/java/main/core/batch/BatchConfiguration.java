package main.core.batch;

import main.core.batch.listener.JobCompletionNotificationListener;
import main.core.batch.processor.PersonItemProcessor;
import main.core.entity.Person;
import main.core.utils.QueryUtils;
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

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private JobBuilderFactory _jobBuilderFactory;

    private StepBuilderFactory _stepBuilderFactory;

    private String[] entityStructure;


    public BatchConfiguration() {
        entityStructure = new String[]{StringUtils.FIRST_NAME, StringUtils.LAST_NAME};
    }

    private FlatFileItemReader<Person> reader(String pathToData) {
        FlatFileItemReaderBuilder<Person> itemReaderBuilder = new FlatFileItemReaderBuilder<>();
        itemReaderBuilder.name(StringUtils.PERSON_ITEM_READER);
        itemReaderBuilder.resource(new ClassPathResource(pathToData));
        itemReaderBuilder.delimited().delimiter(StringUtils.SEMICOLON).names(entityStructure).fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
            setTargetType(Person.class);
        }});
        return itemReaderBuilder.build();
    }

    private PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writerIn(DataSource dataSourceIn) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(QueryUtils.INSERT_TABLE_IN)
                .dataSource(dataSourceIn)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writerOut(DataSource dataSourceOut) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(QueryUtils.INSERT_TABLE_OUT)
                .dataSource(dataSourceOut)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step stepIn, Step stepOut) {
        return _jobBuilderFactory.get(StringUtils.IMPORT_USER_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepIn)
                .next(stepOut)
                .build();
    }

    @Bean
    public Step stepIn(JdbcBatchItemWriter<Person> writerIn) {
        return _stepBuilderFactory.get(StringUtils.STEP_IN)
                .<Person, Person>chunk(10)
                .reader(reader(StringUtils.SAMPLE_DATA_STEP_1))
                .processor(processor())
                .writer(writerIn)
                .build();
    }

    @Bean
    public Step stepOut(JdbcBatchItemWriter<Person> writerOut) {
        return _stepBuilderFactory.get(StringUtils.STEP_OUT)
                .<Person, Person>chunk(10)
                .reader(reader(StringUtils.SAMPLE_DATA_STEP_2))
                .processor(processor())
                .writer(writerOut)
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
