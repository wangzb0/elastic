package com.elastic.es.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ESConfig {
    @Value("${genochain.elasticsearch.host}")
    private String host;
    @Value("${genochain.elasticsearch.port}")
    private Integer port;
    @Value("${genochain.elasticsearch.scheme}")
    private String scheme;
    @Value("${genochain.elasticsearch.userName}")
    private String userName;
    @Value("${genochain.elasticsearch.password}")
    private String password;
    @Value("${genochain.elasticsearch.index_suffix}")
    private String suffix;
}
