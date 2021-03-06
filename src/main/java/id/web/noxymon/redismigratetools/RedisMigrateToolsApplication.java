package id.web.noxymon.redismigratetools;

import id.web.noxymon.redismigratetools.repositories.SourceRepositorySimpleValueRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class RedisMigrateToolsApplication
{
    @Autowired
    SourceRepositorySimpleValueRedis sourceRepositorySimpleValueRedis;

    @Value("${max.thread}")
    Integer maxThread;

    @Value("${max.queue}")
    Integer maxQueue;

    @Value("${pattern}")
    String pattern;

    public static void main(String[] args)
    {
        SpringApplication.run(RedisMigrateToolsApplication.class, args);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(maxThread);
        executor.setThreadNamePrefix("RedisMigrate-");
        executor.initialize();
        return executor;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void method()
    {
        sourceRepositorySimpleValueRedis.migrate(pattern);
    }

}
