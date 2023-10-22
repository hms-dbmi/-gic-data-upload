package edu.harvard.dbmi.avillach.dataupload.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;

@Configuration
public class AWSConfiguration {
    @Value("${aws.s3.access_key_secret}")
    private String secret;

    @Value("${aws.s3.access_key_id}")
    private String keyId;

    @Value("${aws.s3.session_token}")
    private String token;

    @Bean
    public StsClient createStsClient(@Autowired AwsCredentialsProvider provider) {
        return StsClient.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(provider)
            .build();
    }

    @Bean
    public AwsCredentialsProvider createCredentials() {
        return StaticCredentialsProvider.create(AwsSessionCredentials.create(keyId, secret, token));
    }
}
