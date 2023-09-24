package pl.bodzioch.damian.client.conf;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(@Nonnull HttpRequest request, @Nonnull byte[] body, ClientHttpRequestExecution execution) throws IOException {

        logRequestDetails(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        if (response.getStatusCode().isError()) {
            InputStreamReader inputStream = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
            String responseBody = new BufferedReader(inputStream).lines().collect(Collectors.joining());
            log.warn("Response body: {}", responseBody);
        }
        return response;
    }

    private void logRequestDetails(HttpRequest request, byte[] body) {
        log.info("Headers: {}", request.getHeaders());
        log.info("Request Method: {}", request.getMethod());
        log.info("Request URI: {}", request.getURI());
        log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
    }
}
