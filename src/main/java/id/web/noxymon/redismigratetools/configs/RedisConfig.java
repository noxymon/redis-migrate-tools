package id.web.noxymon.redismigratetools.configs;

import id.web.noxymon.redismigratetools.properties.SourceRedisProperty;
import id.web.noxymon.redismigratetools.properties.TargetRedisProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisConfig
{
    private final SourceRedisProperty sourceRedisProperty;
    private final TargetRedisProperty targetRedisProperty;

    @Primary
    @Bean(name = "sourceRedisConnectionFactory")
    public RedisConnectionFactory sourceRedisConnectionFactory()
    {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(sourceRedisProperty.getHost(),
                                                                                      sourceRedisProperty.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "sourceRedisTemplate")
    public StringRedisTemplate sourceRedisTemplate(@Qualifier("sourceRedisConnectionFactory") RedisConnectionFactory source)
    {
        return new StringRedisTemplate(source);
    }

    @Bean(name = "targetRedisConnectionFactory")
    public RedisConnectionFactory targetRedisConnectionFactory()
    {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(targetRedisProperty.getHost(),
                                                                                      targetRedisProperty.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "targetRedisTemplate")
    public StringRedisTemplate targetRedisTemplate(@Qualifier("targetRedisConnectionFactory") RedisConnectionFactory target)
    {
        return new StringRedisTemplate(target);
    }
}
