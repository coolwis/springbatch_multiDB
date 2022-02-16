package com.psc.sample.r103.batch;


import com.psc.sample.r103.db1.Dept1;
import com.psc.sample.r103.db2.Dept2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class JpaPageJob2 {

    @Autowired
    private JobBuilderFactory  jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Resource(name="entityManageFactory1")
    private EntityManagerFactory entityManagerFactory1;

    @Resource(name="dataSource2")
    private DataSource dataSource2;

    private  int chuckSize =10;
    
    @Bean 
    public Job jpaPageJob2_batchBuild() {
        return jobBuilderFactory.get("JpaPageJob2")
                .start(jpaPageJob2_batchStep1())
                .build();
    }

    @Bean
    @JobScope
    private Step jpaPageJob2_batchStep1() {
        return stepBuilderFactory.get("JpaPageJob1_Step")
                .<Dept1, Dept2>chunk(chuckSize)
                .reader(jpaPageJob2_dbItemReader())
                .processor(jpaPageJob2_processor())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    @Bean
    @StepScope
    private JpaPagingItemReader<Dept1> jpaPageJob2_dbItemReader() {

        return  new JpaPagingItemReaderBuilder<Dept1>()
                .name("JpaPageJob1_Reader")
                .entityManagerFactory(entityManagerFactory1)
                .pageSize(chuckSize)
                .queryString("select d from Dept1 d order by deptno asc")  //정렬 필수
                .build();

    }

    @Bean
    @StepScope
    public ItemProcessor<Dept1 ,Dept2> jpaPageJob2_processor() {
//        return dept1-> new Dept2(dept1.getDeptNo(), "new_"+dept1.getDName(), "new_"+ dept1.getLoc());
        return dept1-> {
            if(dept1.getDeptNo() %2 ==0) {
                return  new Dept2(dept1.getDeptNo(), "new_"+dept1.getDName(), "new_"+ dept1.getLoc());
            } else {
                return null;
            }
        };

    }

    @Bean
    public JdbcBatchItemWriter<Dept2> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<Dept2>()
                .dataSource(dataSource2)
                .sql("insert into dept2 (deptno, dname, loc) values (:deptno, :dname, :loc")
                .beanMapped()
                .build();
    }

}
