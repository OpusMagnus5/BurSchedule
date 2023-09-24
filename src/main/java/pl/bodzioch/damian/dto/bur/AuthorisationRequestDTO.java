package pl.bodzioch.damian.dto.bur;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class AuthorisationRequestDTO implements Serializable {

    private String nazwaUzytkownika;
    private String kluczAutoryzacyjny;
}
