package com.project.fitness.controller;

import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.dto.RecommendationResponse;
import com.project.fitness.model.Recommendation;
import com.project.fitness.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/request")
    public ResponseEntity<RecommendationResponse> request(@RequestBody RecommendationRequest recommendationRequest) {
        return ResponseEntity.ok(recommendationService.request(recommendationRequest));
    }

    @PostMapping("/generate")
    public ResponseEntity<RecommendationResponse> generateAiRecommendation(@RequestBody RecommendationRequest recommendationRequest) {
        return ResponseEntity.ok(recommendationService.generateAiRecommendation(recommendationRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<List<RecommendationResponse>> getUserRecommmendation(@RequestHeader(value = "userId") String id) {
        return ResponseEntity.ok(recommendationService.getUserRecommmendation(id));
    }

    @GetMapping("/activity")
    public ResponseEntity<List<Recommendation>> getActivityRecommmendation(@RequestHeader(value = "ActivityId") String id) {
        return ResponseEntity.ok(recommendationService.getActivityRecommmendation(id));
    }
}
