package com.rlaclgh.onetomany.feedback;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {


  @Autowired
  private FeedbackService feedbackService;


  @PostMapping("")
  public ResponseEntity<Void> createFeedback(
      @RequestBody String description
  ) {

    feedbackService.createFeedback(description);
    return ResponseEntity.created(null).body(null);

  }


}
