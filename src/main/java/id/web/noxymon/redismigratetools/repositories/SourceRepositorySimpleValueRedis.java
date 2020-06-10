package id.web.noxymon.redismigratetools.repositories;

import id.web.noxymon.redismigratetools.service.RedisMigrationSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SourceRepositorySimpleValueRedis
{
    private final RedisMigrationSaver redisMigrationSaver;
    private final StringRedisTemplate stringRedisTemplate;
    private final ReactiveStringRedisTemplate sourceRedisTemplateReactive;

    public void migrate(String pattern)
    {
        ScanOptions options = buildScanOptions(pattern);
        stringRedisTemplate.getConnectionFactory()
                           .getConnection()
                           .keyCommands()
                           .scan(options)
                           .forEachRemaining(bytes -> {
                               log.info("Migrate " + new String(bytes));
                               try {
                                   redisMigrationSaver.migrateKey(bytes);
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           });
    }

    private ScanOptions buildScanOptions(String pattern)
    {
        return ScanOptions.scanOptions()
                          .match(pattern)
                          .build();
    }
}