package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sso")
@Data
public class SsoConfigProperties {
    private String serverUrlPrefix;

    private String clientHostUrl;

    private String validationUrlPrefix;
}
