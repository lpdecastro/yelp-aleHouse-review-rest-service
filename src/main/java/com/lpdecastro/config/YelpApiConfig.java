package com.lpdecastro.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "yelp.api")
@Component
@Validated
@Data
public class YelpApiConfig {

    @NotBlank
    private String apiKey;

    @NotBlank
    private String baseUrl;

    @NotBlank
    private String aleHouseId;
}
