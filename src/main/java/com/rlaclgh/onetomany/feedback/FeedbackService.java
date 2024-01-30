package com.rlaclgh.onetomany.feedback;

import com.rlaclgh.onetomany.entity.Feedback;
import com.rlaclgh.onetomany.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

  @Autowired
  private FeedbackRepository feedbackRepository;

  public void createFeedback(String description) {
    feedbackRepository.save(new Feedback(description));
  }

}
