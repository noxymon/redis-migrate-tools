package id.web.noxymon.redismigratetools.repositories;

import id.web.noxymon.redismigratetools.service.RedisMigrationSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SourceRepositorySimpleValueRedis
{
    private final RedisMigrationSaver redisMigrationSaver;
    private final ReactiveStringRedisTemplate sourceRedisTemplateReactive;

    public void migrate(String pattern)
    {
        ScanOptions options = buildScanOptions(pattern);
        sourceRedisTemplateReactive.getConnectionFactory()
                                   .getReactiveConnection()
                                   .keyCommands()
                                   .scan(options)
                                   .log()
                                   .subscribe(byteBuffer -> {
                                       try {
                                           log.info("Migrate " + new String(byteBuffer.array()));
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