package pl.bodzioch.damian.client.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import pl.bodzioch.damian.exception.HttpClientException;
import pl.bodzioch.damian.exception.HttpServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        log.info("Headers: {}", response.getHeaders());
        log.info("Request Status: {}", response.getStatusCode());;
        log.info("Request Body: {}", getBody(response.getBody()));

        if (response.getStatusCode().is4xxClientError()) {
            throw new HttpClientException();
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new HttpServerException();
        }
    }

    private String getBody(InputStream body) {
        InputStreamReader inputStream = new InputStreamReader(body, StandardCharsets.UTF_8);
        return new BufferedReader(inputStream).lines().collect(Collectors.joining());
    }
}
