package id.web.noxymon.redismigratetools.configs;

import id.web.noxymon.redismigratetools.properties.SourceRedisProperty;
import id.web.noxymon.redismigratetools.properties.TargetRedisProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class ReactiveRedisConfig
{
    private final SourceRedisProperty sourceRedisProperty;
    private final TargetRedisProperty targetRedisProperty;

    @Bean(name = "sourceRedisConnectionFactoryReactive")
    public ReactiveRedisConnectionFactory sourceRedisConnectionFactory()
    {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(sourceRedisProperty.getHost(),
                                                                                      sourceRedisProperty.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "sourceRedisTemplateReactive")
    public ReactiveStringRedisTemplate sourceRedisTemplate(@Qualifier("sourceRedisConnectionFactoryReactive") ReactiveRedisConnectionFactory source)
    {
        return new ReactiveStringRedisTemplate(source);
    }

    @Bean(name = "targetRedisConnectionFactoryReactive")
    public ReactiveRedisConnectionFactory targetRedisConnectionFactory()
    {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(targetRedisProperty.getHost(),
                                                                                      targetRedisProperty.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "targetRedisTemplateReactive")
    public ReactiveStringRedisTemplate targetRedisTemplate(@Qualifier("targetRedisConnectionFactoryReactive") ReactiveRedisConnectionFactory target)
    {
        return new ReactiveStringRedisTemplate(target);
    }
}
