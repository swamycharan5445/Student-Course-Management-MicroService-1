package org.example.main.proxy;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("dev")
public class CourseRecommendationProxyInternal implements CourseRecommendationProxy
{

    @Override
	public Mono<List<String>> recommend(Integer studentId)
    {
        List<String> list = new ArrayList<String>();
        if(studentId%2==0)
        {
            list.add("java");
            list.add("python");
        }
        else
        {
            list.add("Oracle");
            list.add("MySQL");
        }
        return Mono.just(list);
    }
}
