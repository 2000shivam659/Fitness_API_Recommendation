package com.project.fitness.respository;

import com.project.fitness.dto.RecommendationResponse;
import com.project.fitness.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRespository extends JpaRepository<Recommendation, String > {
    List<Recommendation> findByUserId(String id);

    List<Recommendation> findByActivityId(String id);
}
