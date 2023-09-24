package pl.bodzioch.damian.client;

import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Stream;

@Service
public class BurClient {

    private static final String BUR_URL = "https://uslugirozwojowe.parp.gov.pl/api";
    private static final String AUTHORIZATION_PATH = BUR_URL + "/autoryzacja/logowanie";
    private static final String GET_SCHEDULE_PATH = BUR_URL + "/usluga/1/harmonogram";

    private final AuthorisationRequestDTO authorisationRequestDTO;


    public BurClient(@Value("${bur.username}") String username, @Value("${bur.keyAuthorisation}") String keyAuthorisation) {
        this.authorisationRequestDTO = AuthorisationRequestDTO.builder()
                .nazwaUzytkownika(username)
                .kluczAutoryzacyjny(keyAuthorisation)
                .build();
    }

    private AuthorisationResponseDTO authorize() {
        return new RestTemplate().postForObject(AUTHORIZATION_PATH, authorisationRequestDTO, AuthorisationResponseDTO.class);
    }

    private ListOfServiceScheduleEntriesDTO getPageOfServiceScheduleEntries(int serviceId, int page) {
        Map<String, String> urlVariables = Map.of("id", Integer.toString(serviceId));
        URI uri = UriComponentsBuilder.fromHttpUrl(GET_SCHEDULE_PATH)
                .queryParam("strona", page)
                .build(urlVariables);

        return new RestTemplate().getForObject(uri, ListOfServiceScheduleEntriesDTO.class);
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
}
