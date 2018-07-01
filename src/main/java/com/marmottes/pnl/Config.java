package com.marmottes.pnl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:quotes-pnl.properties")
@ConfigurationProperties(prefix = "quotes-pnl")
public class Config {
    private String username;
    private String password;
    private String url;
}
