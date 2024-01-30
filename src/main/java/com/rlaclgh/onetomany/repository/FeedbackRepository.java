package com.rlaclgh.onetomany.repository;

import com.rlaclgh.onetomany.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


}
