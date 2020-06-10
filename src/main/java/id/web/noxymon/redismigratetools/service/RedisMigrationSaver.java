package id.web.noxymon.redismigratetools.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMigrationSaver
{
    private final StringRedisTemplate sourceRedisTemplate;

    @Async
    public Future<Void> migrateKey(byte[] content) throws IOException
    {
        final byte[] dump = sourceRedisTemplate.dump(new String(content));
        FileUtils.writeByteArrayToFile(new File("generated/gen.dump"), dump, true);
        return new AsyncResult<Void>(null);
    }
}
