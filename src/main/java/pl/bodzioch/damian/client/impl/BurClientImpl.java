package pl.bodzioch.damian.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.bodzioch.damian.client.BurClient;
import pl.bodzioch.damian.dto.bur.*;
import pl.bodzioch.damian.mapper.BurMapper;
import pl.bodzioch.damian.model.ScheduleEntry;
import pl.bodzioch.damian.model.ServiceModel;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BurClientImpl implements BurClient {

    private static final String BUR_URL = "https://uslugirozwojowe.parp.gov.pl/api";
    private static final String AUTHORIZATION_PATH = BUR_URL + "/autoryzacja/logowanie";
    private static final String GET_SCHEDULE_PATH = BUR_URL + "/usluga/{id}/harmonogram";
    private static final String GET_SERVICES_PATH = BUR_URL + "/usluga";
    private static final String PAGE_PARAM = "strona";
    private static final String NIP_PARAM = "nipDostawcyUslug";

    private String jwtToken;

    private final AuthorisationRequestDTO authorisationRequestDTO;
    private final RestTemplate restTemplate;


    public BurClientImpl(@Value("${bur.username}") String username, @Value("${bur.keyAuthorisation}") String keyAuthorisation,
                         RestTemplateBuilder restTemplateBuilder) {
        this.authorisationRequestDTO = AuthorisationRequestDTO.builder()
                .nazwaUzytkownika(username)
                .kluczAutoryzacyjny(keyAuthorisation)
                .build();
        this.restTemplate = restTemplateBuilder.build();

    }

    @Override
    public List<ScheduleEntry> getScheduleForService(long serviceId) {
        int page = 1;
        int downloadedElements = 0;
        List<ListOfServiceScheduleEntriesDTO> responses = new ArrayList<>();
        ListOfServiceScheduleEntriesDTO response;

        do {
            response = getPageOfServiceScheduleEntries(serviceId, page);
            responses.add(response);
            downloadedElements += response.getElementyNaStrone();
            page++;
        } while (downloadedElements < response.getWszystkieElementy());

        return responses.stream()
                .map(ListOfServiceScheduleEntriesDTO::getLista)
                .flatMap(List::stream)
                .map(BurMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceModel> getServiceById(long serviceId) {
        return getServicesBy(serviceId, "id");
    }

    @Override
    public List<ServiceModel> getServicesByNip(long nip) {
        return getServicesBy(nip, NIP_PARAM);
    }

    private List<ServiceModel> getServicesBy(long paramValue, String paramName) {
        int page = 1;
        int downloadedElements = 0;
        List<ServiceListDTO> responses = new ArrayList<>();
        ServiceListDTO response;

        do {
            response = getPageOfServices(paramName, paramValue, page);
            responses.add(response);
            downloadedElements += response.getElementyNaStrone();
            page++;
        } while (downloadedElements < response.getWszystkieElementy());

        return responses.stream()
                .map(ServiceListDTO::getLista)
                .flatMap(List::stream)
                .map(BurMapper::map)
                .toList();
    }

    private ServiceListDTO getPageOfServices(String params, long paramValue, int page) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GET_SERVICES_PATH)
                .queryParam(params, paramValue)
                .queryParam(PAGE_PARAM, page)
                .build()
                .toUri();

        HttpEntity<Object> requestEntity = getHeaders();

        try {
            return restTemplate.exchange(uri, HttpMethod.GET, requestEntity, ServiceListDTO.class).getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            return restTemplate.exchange(uri, HttpMethod.GET, getHeadersWithAuth(), ServiceListDTO.class).getBody();
        }
    }

    private ListOfServiceScheduleEntriesDTO getPageOfServiceScheduleEntries(long serviceId, int page) {
        Map<String, String> urlVariables = Map.of("id", Long.toString(serviceId));
        URI uri = UriComponentsBuilder.fromHttpUrl(GET_SCHEDULE_PATH)
                .queryParam(PAGE_PARAM, page)
                .build(urlVariables);
        HttpEntity<Object> requestEntity = getHeaders();

        try {
            return restTemplate.exchange(uri, HttpMethod.GET, requestEntity, ListOfServiceScheduleEntriesDTO.class).getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            return restTemplate.exchange(uri, HttpMethod.GET, getHeadersWithAuth(), ListOfServiceScheduleEntriesDTO.class).getBody();
        }
    }

    private HttpEntity<Object> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Object> getHeadersWithAuth() {
        AuthorisationResponseDTO response = restTemplate.postForObject(AUTHORIZATION_PATH, authorisationRequestDTO, AuthorisationResponseDTO.class);
        jwtToken = response.getToken();
        return getHeaders();
    }


}
