package com.rlaclgh.onetomany.image;

import com.rlaclgh.onetomany.aws.AWSService;
import com.rlaclgh.onetomany.dto.CreatePreSignedUrlDto;
import com.rlaclgh.onetomany.dto.CreatePreSignedUrlResponse;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {


  @Autowired
  private AWSService awsService;

  @PostMapping("/pre-signed")
  public ResponseEntity<CreatePreSignedUrlResponse> createPreSignedImage(
      @RequestBody CreatePreSignedUrlDto createPreSignedUrlDto) {

    URL presignedPutObjectRequest = awsService.createSignedUrlForStringPut(
        createPreSignedUrlDto);

    return ResponseEntity.ok(new CreatePreSignedUrlResponse(presignedPutObjectRequest));
  }

}
