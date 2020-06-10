package id.web.noxymon.redismigratetools.repositories;

import id.web.noxymon.redismigratetools.service.RedisMigrationSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SourceRepositorySimpleValueRedis
{
    private final RedisMigrationSaver redisMigrationSaver;
    private final StringRedisTemplate sourceRedisTemplate;

    public void migrate(String pattern)
    {
        ScanOptions options = ScanOptions.scanOptions()
                                         .match(pattern)
                                         .build();
        sourceRedisTemplate
                .getConnectionFactory()
                .getConnection()
                .scan(options)
                .forEachRemaining(content -> {
                    redisMigrationSaver.migrateKey(content);
                });
    }
}