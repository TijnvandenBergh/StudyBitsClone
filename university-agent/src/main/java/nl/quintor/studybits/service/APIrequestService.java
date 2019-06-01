package nl.quintor.studybits.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class APIrequestService {

    @Async
    public String request(int id, String url, String endpoint) {
        String fullUrl = url + endpoint;
        log.debug(fullUrl);
        String idVar = Integer.toString(id);
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(fullUrl, String.class, idVar);
        }
        catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
                return "Student not found in system";
            }
            return "Not known error";
        }
    }
}
