package com.project.fitness.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.fitness.model.ActivityType;
import com.project.fitness.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityResponse {
    private User user;
    private ActivityType activityType;
    private Map<String, Object> additionalActivity;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
