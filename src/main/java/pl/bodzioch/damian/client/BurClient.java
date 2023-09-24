package pl.bodzioch.damian.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.bodzioch.damian.dto.bur.AuthorisationRequestDTO;
import pl.bodzioch.damian.dto.bur.AuthorisationResponseDTO;
import pl.bodzioch.damian.dto.bur.ListOfServiceScheduleEntriesDTO;
import pl.bodzioch.damian.mapper.BurMapper;
import pl.bodzioch.damian.model.ScheduleEntry;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BurClient {

    private static final String BUR_URL = "https://uslugirozwojowe.parp.gov.pl/api";
    private static final String AUTHORIZATION_PATH = BUR_URL + "/autoryzacja/logowanie";
    private static final String GET_SCHEDULE_PATH = BUR_URL + "/usluga/1/harmonogram";

    private final AuthorisationRequestDTO authorisationRequestDTO;
    private final RestTemplate restTemplate;


    public BurClient(@Value("${bur.username}") String username, @Value("${bur.keyAuthorisation}") String keyAuthorisation,
                     RestTemplateBuilder restTemplateBuilder) {
        this.authorisationRequestDTO = AuthorisationRequestDTO.builder()
                .nazwaUzytkownika(username)
                .kluczAutoryzacyjny(keyAuthorisation)
                .build();
        this.restTemplate = restTemplateBuilder.build();

    }

    public List<ScheduleEntry> getScheduleForService(int serviceId) {
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
                .toList();
    }

    private ListOfServiceScheduleEntriesDTO getPageOfServiceScheduleEntries(int serviceId, int page) {
        String jwtToken = authorize().getToken();

        Map<String, String> urlVariables = Map.of("id", Integer.toString(serviceId));
        URI uri = UriComponentsBuilder.fromHttpUrl(GET_SCHEDULE_PATH)
                .queryParam("strona", page)
                .build(urlVariables);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

        return new RestTemplate().exchange(uri, HttpMethod.GET, requestEntity, ListOfServiceScheduleEntriesDTO.class).getBody();
    }

    private AuthorisationResponseDTO authorize() {
        return new RestTemplate().postForObject(AUTHORIZATION_PATH, authorisationRequestDTO, AuthorisationResponseDTO.class);
    }


}
