package com.project.fitness.service;

import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.dto.RecommendationResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.Recommendation;
import com.project.fitness.model.User;
import com.project.fitness.respository.ActivityRepository;
import com.project.fitness.respository.RecommendationRespository;
import com.project.fitness.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRespository recommendationRespository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public RecommendationResponse request(RecommendationRequest recommendationRequest) {
        User user = userRepository.findById(recommendationRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Invalid User: " + recommendationRequest.getUserId()));
        Activity activity = activityRepository.findById(recommendationRequest.getActivityId())
                .orElseThrow(() -> new RuntimeException("Invalid Activity: " + recommendationRequest.getActivityId()));

        Recommendation recommendation = Recommendation.builder()
                .user(user)
                .activity(activity)
                .improvements(recommendationRequest.getImprovements())
                .suggestions(recommendationRequest.getSuggestions())
                .safety(recommendationRequest.getSafety())
                .build();
        recommendationRespository.save(recommendation);
        return mapToResponse(recommendation);
    }

    private RecommendationResponse mapToResponse(Recommendation recommendation) {
        RecommendationResponse response = RecommendationResponse.builder()
                .user(recommendation.getUser())
                .activity(recommendation.getActivity())
                .improvements(recommendation.getImprovements())
                .suggestions(recommendation.getSuggestions())
                .safety(recommendation.getSafety())
                .createdAt(recommendation.getCreatedAt())
                .updatedAt(recommendation.getUpdatedAt())
                .build();
        return response;
    }

    public List<RecommendationResponse> getUserRecommmendation(String id) {
        List<Recommendation> recommendationList = recommendationRespository.findByUserId(id);
        return recommendationList.stream().map(recommendation -> mapToResponse(recommendation)).toList();
    }

    public List<Recommendation> getActivityRecommmendation(String id) {
        return recommendationRespository.findByActivityId(id);
    }
}
