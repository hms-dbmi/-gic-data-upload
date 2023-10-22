package edu.harvard.dbmi.avillach.dataupload.aws;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
public class S3StateVerifier {

    private static final String testTempPrefix = "S3_DAEMON_INIT_TEST";
    private static final Logger LOG = LoggerFactory.getLogger(S3StateVerifier.class);

    @Value("${aws.s3.home_institute_bucket}")
    private String bucketName;

    @Value("${aws.s3.access_key_id}")
    private String keyId;

    @Autowired
    private SelfRefreshingS3Client client;

    @PostConstruct
    private void verifyS3Status() {
        Thread.ofVirtual().start(this::asyncVerify);
    }

    private void asyncVerify() {
        LOG.info("Checking S3 connection...");
        createTempFileWithText()
            .map(this::uploadFileFromPath)
            .map(this::waitABit)
            .flatMap(this::deleteFileFromBucket)
            .orElseThrow();
        LOG.info("S3 connection verified.");
    }

    private Optional<String> deleteFileFromBucket(String s) {
        LOG.info("Verifying delete capabilities");
        DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName).key(s).build();
        DeleteObjectResponse deleteObjectResponse = client.getS3Client().deleteObject(request);
        return deleteObjectResponse.deleteMarker() ? Optional.of(s) : Optional.empty();
    }

    private String waitABit(String s) {
        try {
            Thread.sleep(Duration.of(10, ChronoUnit.SECONDS));
        } catch (InterruptedException e) {
            LOG.warn("Error sleeping: ", e);
        }
        return s;
    }

    private String uploadFileFromPath(Path p) {
        LOG.info("Verifying upload capabilities");
        RequestBody body = RequestBody.fromFile(p.toFile());
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .serverSideEncryption(ServerSideEncryption.AWS_KMS)
            .key(p.getFileName().toString())
            .build();
        client.getS3Client().putObject(request, body);
        return p.getFileName().toString();
    }

    private Optional<Path> createTempFileWithText() {
        try {
            Path path = Files.createTempFile(testTempPrefix + "_" + keyId, null);
            Files.writeString(path, "Howdy!");
            return Optional.of(path);
        } catch (IOException e) {
            LOG.error("Failed to create temp file. Daemon will likely be unable to write to S3!");
            return Optional.empty();
        }
    }
}
