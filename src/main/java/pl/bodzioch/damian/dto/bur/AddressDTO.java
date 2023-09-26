package pl.bodzioch.damian.dto.bur;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private String nazwaMiejscowosci;
    private String nazwaUlicy;
    private String kodPocztowy;
    private String numerBudynku;
    private String numerLokalu;
}
