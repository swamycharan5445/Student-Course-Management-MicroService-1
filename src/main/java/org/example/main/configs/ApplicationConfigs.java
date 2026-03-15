package org.example.main.configs;

import feign.Retryer;
import org.example.main.feign.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class ApplicationConfigs
{
    @Bean
    public WebClient webClient()
    {
        return WebClient.builder().build();
    }
    @Bean
    public RestTemplate restTemplate()
    {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(10));
        return new RestTemplate(requestFactory);
    }
    @Bean
    public Retryer retryer()
    {
        return new Retryer.Default(100, 2000, 3);
    }

    @Bean
    public CustomErrorDecoder errorDecoder()
    {
        return new CustomErrorDecoder();
    }

    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }



    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/student/**")
                        .allowedOrigins("http://localhost:3000","http://app.qa.company.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
                registry.addMapping("/course/**")
                        .allowedOrigins("http://localhost:3000","http://app.qa.company.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }

        };
    }
}
