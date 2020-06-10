package id.web.noxymon.redismigratetools.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMigrationSaver
{
    private final StringRedisTemplate sourceRedisTemplate;
    private final StringRedisTemplate targetRedisTemplate;

    @Async
    public void migrateKey(byte[] content)
    {
        final String redisKey = new String(content);
        final byte[] dump = sourceRedisTemplate.dump(redisKey);
        if(dump != null || dump.length > 0){
            targetRedisTemplate
                    .restore(
                            redisKey, dump,
                            0, TimeUnit.DAYS,
                            true
                    );
            log.info("Hasilnya : " + redisKey);
        }
    }
}
