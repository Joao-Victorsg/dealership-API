package com.example.api.dealership.config.aws;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

@Configuration
public class SnsConfig {

    @Value("${spring.cloud.aws.sns.endpoint}")
    private String snsEndpoint;

    @Value("${spring.cloud.aws.sns.region}")
    private String region;

    @Bean
    public SnsClient getSnsClient(){
        return SnsClient.builder()
                .endpointOverride(URI.create(snsEndpoint))
                //.region(Region.of(region))
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public SnsTemplate getSnsTemplate(SnsClient snsClient, MessageConverter messageConverter){
        return new SnsTemplate(snsClient,messageConverter);
    }

    @Bean
    public MessageConverter messageConverter() {
        return payloadConverter();
    }

    private MappingJackson2MessageConverter payloadConverter() {
        final var payloadConverter = new MappingJackson2MessageConverter();
        payloadConverter.setObjectMapper(getObjectMapper());
        payloadConverter.setSerializedPayloadClass(String.class);
        return payloadConverter;
    }

    private ObjectMapper getObjectMapper(){
        return new ObjectMapper()
                .findAndRegisterModules()
                .registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }


}
