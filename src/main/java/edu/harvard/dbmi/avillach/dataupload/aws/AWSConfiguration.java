package edu.harvard.dbmi.avillach.dataupload.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ConditionalOnProperty(name = "production", havingValue = "true")
@Configuration
public class AWSConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(AWSConfiguration.class);

    @Value("${aws.s3.access_key_secret:}")
    private List<String> secrets;

    @Value("${aws.s3.access_key_id:}")
    private List<String> keyIds;

    @Value("${aws.s3.session_token:}")
    private List<String> tokens;

    @Value("${aws.s3.institution:}")
    private List<String> institutions;

    @Autowired
    private ConfigurableApplicationContext context;

    @Bean
    @ConditionalOnProperty(name = "production", havingValue = "true")
    public Map<String, StsClient> createStsClient(@Autowired Map<String, AwsCredentials> credentials) {
        return credentials.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> StsClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(StaticCredentialsProvider.create(e.getValue()))
                    .build())
            );
    }

    @Bean
    @ConditionalOnProperty(name = "production", havingValue = "true")
    Map<String, AwsCredentials> credentials() {
        if (
            secrets.size() != keyIds.size() ||
            secrets.size() != institutions.size() ||
            (secrets.size() != tokens.size() && !tokens.isEmpty()) // empty token = user auth instead of account
        ) {
            LOG.error("Mismatched aws credentials");
            context.close();
        }

        HashMap<String, AwsCredentials> creds = new HashMap<>();
        for (int i = 0; i < institutions.size(); i++) {
            AwsCredentials cred = tokens.isEmpty() ?
                AwsBasicCredentials.create(keyIds.get(i), secrets.get(i)) :
                AwsSessionCredentials.create(keyIds.get(i), secrets.get(i), tokens.get(i));
            creds.put(institutions.get(i), cred);
        }
        return creds;
    }
}
