package com.pailsom.flare.batch.configuration;

import com.pailsom.flare.batch.listener.DepartmentFileSelectionListener;
import com.pailsom.flare.batch.model.Department;
import com.pailsom.flare.batch.model.EmployeeEnriched;
import com.pailsom.flare.batch.model.EmployeeRaw;
import com.pailsom.flare.batch.repository.DepartmentRepository;
import com.pailsom.flare.batch.repository.EmployeeEnrichedRepository;
import com.pailsom.flare.batch.repository.EmployeeRawRepository;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Configuration
public class EmployeeReportGenerator {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Bean
    public Job jobEmp() throws Exception {
        return jobBuilderFactory.get("employee-report")
                .start(loadEmp())
                .next(generate())
                .build();
    }


    @Bean
    public Step loadEmp() throws Exception {
        return stepBuilderFactory.get("generate")
                .<EmployeeRaw,EmployeeRaw>chunk(100)
                .reader(readerEmp())
                .writer(writerEmpRaw(null))
                .build();
    }

    private Step generate() throws Exception {
        return stepBuilderFactory.get("generate")
                .<EmployeeRaw, EmployeeEnriched>chunk(100)
                .reader(readerEmployee(null))
                .processor((Function<? super EmployeeRaw, ? extends EmployeeEnriched>) employeeRaw -> {
                    final Optional<Department> department = departmentRepository.findById(employeeRaw.getId());
                    return EmployeeEnriched.builder()
                             .firstName(employeeRaw.getFirstName())
                             .lastName(employeeRaw.getLastName())
                             .departmentName(department.map(Department::getDepartmentName).orElse("Unknown Department"))
                             .departmentHead(department.map(Department::getHeadOfDepartment).orElse("Unknown Head Of Department"))
                             .build();
                })
                .writer(enrichedEmployeeWriter(null))
                .build();
    }

    @Bean
    public ItemWriter<EmployeeEnriched> enrichedEmployeeWriter(EmployeeEnrichedRepository employeeEnrichedRepository) throws JAXBException {

        return new CompositeItemWriterBuilder<EmployeeEnriched>()
                .delegates(new RepositoryItemWriterBuilder<EmployeeEnriched>().methodName("save").repository(employeeEnrichedRepository)
                        .build(),
                        new StaxEventItemWriterBuilder<EmployeeEnriched>()
                                .name("xml-output")
                                .resource(new FileSystemResource("xml/result.xml"))
                                .marshaller(marshaller())
                                .build()
                )
                .build();
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.pailsom");
        marshaller.setMappedClass(EmployeeEnriched.class); // ADD THIS LINE
        return marshaller;
    }


    @Bean
    public ItemReader<EmployeeRaw> readerEmp() throws Exception {
        return new FlatFileItemReaderBuilder<EmployeeRaw>()
                .name("employeeReader")
                .resource(applicationProperties.getEmployeeResource().getResource())
                .delimited()
                .names(new String[]{"firstName", "lastName", "departmentId"})
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<EmployeeRaw>() {{
                    setTargetType(EmployeeRaw.class);
                }})
                .build();

    }

    @Bean
    public ItemWriter<EmployeeRaw> writerEmpRaw(EmployeeRawRepository employeeRawRepository) {
        return new RepositoryItemWriterBuilder<EmployeeRaw>()
                .methodName("save")
                .repository(employeeRawRepository)
                .build();
    }

    @Bean
    public ItemReader<EmployeeRaw> readerEmployee(EmployeeRawRepository employeeRawRepository){
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("firstName", Sort.Direction.ASC);
        return new RepositoryItemReaderBuilder<EmployeeRaw>()
                .name("employee-reader")
                .methodName("findAll")
                .repository(employeeRawRepository)
                .sorts(sorts)
                .build();
    }
}
