package id.web.noxymon.redismigratetools.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis.source")
public class SourceRedisProperty extends AbstractRedisProperty
{
}
