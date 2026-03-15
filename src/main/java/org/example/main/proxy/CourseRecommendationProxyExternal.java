package org.example.main.proxy;

import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
@Profile("prod")
public class CourseRecommendationProxyExternal implements CourseRecommendationProxy
{

    private final WebClient webClient;

    public CourseRecommendationProxyExternal(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<List<String>> recommend(Integer studentId)
    {
         return webClient.get().uri("http://localhost:9999/recommend/{id}", studentId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                 .onStatus(HttpStatusCode::is5xxServerError, res->Mono.error((new RuntimeException("5xx server error"))))
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                 .timeout(Duration.ofSeconds(10))
                 .retryWhen(
                         Retry.fixedDelay(3, Duration.ofSeconds(5))
                                 .filter(ex->ex instanceof WebClientRequestException)
                 );
    }
}
