package com.pailsom.flare.batch.configuration;

import com.pailsom.flare.batch.listener.DepartmentFileSelectionListener;
import com.pailsom.flare.batch.model.Department;
import com.pailsom.flare.batch.repository.DepartmentRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

@Configuration
public class DepartmentLoader {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public Job job(DepartmentFileSelectionListener departmentFileSelectionListener) throws Exception {
        return jobBuilderFactory.get("load-department")
                .start(load())
                .listener(departmentFileSelectionListener)
                .build();
    }

    @Bean
    public Step load() throws Exception {
        return stepBuilderFactory.get("load")
                .<Department,Department>chunk(100)
                .reader(reader())
                .writer(writer(null))
                .build();
    }


    @Bean
    public ItemReader<Department> reader() throws Exception {
        return new FlatFileItemReaderBuilder<Department>()
                .name("departmentReader")
                .resource(applicationProperties.getDepartmentResource().getResource())
                .delimited()
                .names(new String[]{"id", "departmentName", "headOfDepartment"})
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Department>() {{
                    setTargetType(Department.class);
                }})
                .build();

    }

    @Bean
    public ItemWriter<Department> writer(DepartmentRepository departmentRepository) {
        return new RepositoryItemWriterBuilder<Department>()
                .methodName("save")
                .repository(departmentRepository)
                .build();
    }
}
