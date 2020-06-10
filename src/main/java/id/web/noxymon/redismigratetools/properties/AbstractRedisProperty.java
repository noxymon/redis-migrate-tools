package id.web.noxymon.redismigratetools.properties;

import lombok.Data;

@Data
public class AbstractRedisProperty
{
    private String host;
    private int port;
    private int database;
}