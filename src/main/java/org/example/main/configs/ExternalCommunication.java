package org.example.main.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

@Service
public class ExternalCommunication
{
    @Value("${external.url}")
    private String url;

    @Value("${external.feature}")
    private boolean feature;

    private final RestTemplate restTemplate;
    public ExternalCommunication(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }


    public String StudentEnroll(Integer student_id)
    {
        if(!feature)
        {
            if(student_id%2==0)
                return "mail send";
            else
                return "mail Unsend";
        }
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class, student_id);
        return exchange.getBody();
    }
}
