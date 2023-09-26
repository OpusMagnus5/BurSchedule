package pl.bodzioch.damian.client.conf;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import pl.bodzioch.damian.exception.HttpClientException;

import java.io.IOException;

public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            throw new HttpClientException();
        } else if (response.getStatusCode().is5xxServerError()) {

        }
    }
}
