package com.rlaclgh.onetomany.aws;


import com.rlaclgh.onetomany.dto.CreatePreSignedUrlDto;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@Slf4j
public class AWSService {


  /**
   * Create a presigned URL for uploading a String object.
   *
   * @param bucketName - The name of the bucket.
   * @param keyName    - The name of the object.
   * @return - The presigned URL for an HTTP PUT.
   */

  @Value("${aws.accessKeyId}")
  private String accessKeyId;

  @Value("${aws.secretKey}")
  private String secretKey;


  private static String BUCKET_NAME = "onetomany-image";

  public URL createSignedUrlForStringPut(CreatePreSignedUrlDto createPreSignedUrlDto) {

    String contentType = createPreSignedUrlDto.getContentType();

    String keyName = "raw/" + UUID.randomUUID() + "." + contentType.split("/")[1];

    try (S3Presigner presigner = S3Presigner.builder()
        .region(Region.AP_NORTHEAST_2).credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretKey))
        ).build()) {

      PutObjectRequest objectRequest = PutObjectRequest.builder()
          .bucket(BUCKET_NAME)
          .key(keyName)
          .contentType(contentType)
          .build();

      PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
          .signatureDuration(Duration.ofSeconds(10))
          .putObjectRequest(objectRequest)
          .build();

      PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

      log.info(presignedRequest.signedHeaders().toString());
      log.info(presignedRequest.url().toString());
      log.info(presignedRequest.signedPayload().toString());
//      log.info(presignedRequest.);

      return presignedRequest.url();
//      String myURL = presignedRequest.url().toString();
//      log.info("Presigned URL to upload to: [{}]", myURL);
//      log.info("Which HTTP method needs to be used when uploading: [{}]",
//          presignedRequest.httpRequest().method());
//
//      return presignedRequest.url();
    }
  }


}
