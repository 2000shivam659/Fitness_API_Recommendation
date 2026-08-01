package com.project.fitness.service;

import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.User;
import com.project.fitness.respository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserService userService;

    public ActivityResponse save(ActivityRequest activityRequest) {
        User user = userService.findById(activityRequest.getUserId());
        Activity activity = Activity.builder()
                .user(user)
                .activityType(activityRequest.getActivityType())
                .additionalActivity(activityRequest.getAdditionalActivity())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .build();
        activityRepository.save(activity);
        return mapToResponse(activity);
    }

    public ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = ActivityResponse.builder()
                .user(activity.getUser())
                .activityType(activity.getActivityType())
                .additionalActivity(activity.getAdditionalActivity())
                .duration(activity.getDuration())
                .caloriesBurned(activity.getCaloriesBurned())
                .startTime(activity.getStartTime())
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .build();
        return response;
    }

    public List<ActivityResponse> getAllActivitiees(String id) {
        List<Activity> activityList = activityRepository.findByUserId(id);
        return activityList.stream().map(activity -> mapToResponse(activity)).toList();
    }
}
