package pl.bodzioch.damian.dto.bur;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthorisationResponseDTO implements Serializable {

    private String token;
}
