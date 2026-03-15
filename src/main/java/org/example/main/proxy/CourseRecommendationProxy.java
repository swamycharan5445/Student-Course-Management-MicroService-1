package org.example.main.proxy;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public interface CourseRecommendationProxy
{
    public Mono<List<String>> recommend(Integer studentId);
}
