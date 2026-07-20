package com.project.fitness.service;

import com.project.fitness.model.Activity;
import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.UserRepository;
import com.project.fitness.dto.ActivityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityResponse trackActivity(ActivityRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("invalid user" + request.getUserId()));
        Activity activity = Activity.builder()
                .user(user)
                .type(request.getType())
                .additionalMetrics((request.getAdditionalMetrics()))
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime()).build();
        Activity savedActivity =  activityRepository.save(activity);
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUser().getId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCreatedAt(activity.getCreatedAt());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setUpdatedAt(activity.getUpdatedAt());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        return response;
    }

    public List<ActivityResponse> getUserActivity(String userId) {
        List<Activity> activityList = activityRepository.findByUserId(userId);
        return activityList.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
}
