package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
@Data
public class ApiConfigProperties {
    private String psId;
    private String urlPrefix;
    private String accessTokenUrl;
    private String permissionsUrl;
    private String authorizationAppKey;
    private String authorizationAppSecret;
}
