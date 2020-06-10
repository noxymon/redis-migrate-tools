package id.web.noxymon.redismigratetools.repositories;

import id.web.noxymon.redismigratetools.service.RedisMigrationSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SourceRepositorySimpleValueRedis
{
    private final RedisMigrationSaver redisMigrationSaver;
    private final ReactiveRedisTemplate reactiveRedisTemplate;

    public void migrate(String pattern)
    {
        ScanOptions options = buildScanOptions(pattern);
        reactiveRedisTemplate.getConnectionFactory()
                             .getReactiveConnection()
                             .keyCommands()
                             .scan(options)
                             .subscribe(byteBuffer -> {
                                 try {
                                     redisMigrationSaver.migrateKey(byteBuffer.array());
                                 } catch (IOException e) {
                                     log.error("Error : " + e.getMessage());
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